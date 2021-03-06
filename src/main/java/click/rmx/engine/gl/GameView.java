package click.rmx.engine.gl;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import click.rmx.Bugger;
import click.rmx.RMXObject;
import click.rmx.engine.Camera;
import click.rmx.engine.GLShaderView;
import click.rmx.engine.GLViewPerFrameLog;
import click.rmx.engine.GameView1;
import click.rmx.engine.Node;
import click.rmx.engine.Nodes;
import click.rmx.engine.Scene;
import click.rmx.engine.behaviours.Behaviour;
import click.rmx.engine.math.Vector4;

public abstract class GameView extends RMXObject implements GLView {

	private GLFWWindowSizeCallback windowSizeCallback;

	private final Vector4 clearColor = new Vector4();
	
	private Node _pointOfView;
	private int height = 720, width = 1280;

	// We need to strongly reference callback instances.
	private GLFWErrorCallback errorCallback;
	private KeyCallback  keyCallback;
	// The window handle
	private long window;
	private CursorCallback cursorCallback;

//	protected Scene scene;

	private boolean debugging = false;
	
	private int framesPerUpdate = 60;

	private GLViewPerFrameLog perFramDebugInfo = () -> "";

	protected GameView() {
		this.init();
		this.onAwake();
	}

	@Override
	public void enterGameLoop(long window) {
		

		
		LinkedList<Long> frameTime = null;
		int counter = 0;
		if (debugging) {
			frameTime = new LinkedList<>();
			for (int i = 0; i < framesPerUpdate ; ++i)
				frameTime.add((long) 0);
		}
		Instant start = null, end = null;
		
		while ( glfwWindowShouldClose(window) == GL_FALSE ) {

			if (debugging)
				start = Instant.now();

			clearColor();
			
			renderFrame(getScene());
	
			// swap the color buffers
			glfwSwapBuffers(window());
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
			
			if (debugging) {
				end = Instant.now();
				Duration duration = Duration.between(start, end);
				frameTime.addLast(duration.toMillis());
				frameTime.removeFirst();

				Long total = (long) 0;
				if (++counter == framesPerUpdate) {
					for (Long t : frameTime) {
						total += t;
					}
					String s = perFramDebugInfo.getLog();
					Bugger.logAndPrint(s + "\n Average time per loop in milliseconds: " + total / framesPerUpdate, false);
					counter = 0;
				}
			}
		}
	}


	protected void clearColor() {
		glClearColor(
				clearColor.x,
				clearColor.y,
				clearColor.z,
				clearColor.w
				);
	}

	@Override
	public GLFWErrorCallback errorCallback() {
		return errorCallback;
	}

	protected GLViewPerFrameLog getPerFramDebugInfo() {
		return perFramDebugInfo;
	}


	public Scene getScene() {
		return Scene.getCurrent();
	}



	@Override
	public int height() {
		return this.height;
	}

	protected void init() {
		keyCallback = KeyCallback.getInstance();
		cursorCallback = CursorCallback.getInstance();
		this.windowSizeCallback = new GLFWWindowSizeCallback() {

			@Override
			public void invoke(long arg0, int width, int height) {
				setSize(width,height);
			}

		};
	}


	@Override
	public void initGLCallbacks(long window) {
		glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
		glfwSetKeyCallback(window, keyCallback);
		glfwSetCursorPosCallback(window, cursorCallback);
		glfwSetWindowSizeCallback(window, this.windowSizeCallback);
	}

	public boolean isDebugging() {
		return debugging;
	}

	@Override
	public KeyCallback keyCallback() {
		return keyCallback;
	}

	protected abstract void onAwake();

	@Override
	public Node pointOfView() {
		if (_pointOfView != null || this.setPointOfView(Nodes.getCurrent()))
			return _pointOfView;
		Bugger.logAndPrint("ERROR: Could Not Set _pointOfView", true);
		return null;
	}

	protected abstract void renderFrame(Scene scene);

	public void setDebugging(boolean debugging, int framesPerUpdate) {
		this.debugging = debugging;
		this.framesPerUpdate = framesPerUpdate;
	}

	public void setPerFramDebugInfo(GLViewPerFrameLog perFramDebugInfo) {
		this.perFramDebugInfo = perFramDebugInfo;
	}
	

	@Override
	public boolean setPointOfView(Node pointOfView) {
		if (pointOfView.camera() == null) {
			pointOfView.setCamera(new Camera());
			pointOfView.addBehaviour(new Behaviour(){

				@Override
				public void broadcastMessage(String message, Object args) {

					if (message == "lookUp") {
						//					Bugger.logAndPrint(message + ": "+ args , false);
						pointOfView.transform().rotate("pitch", (float) args);
					}
				}
				@Override
				public boolean hasLateUpdate() {
					return false;
				}
				@Override
				protected void onAwake() {

				}

				@Override
				public void update(Node node) {
					// TODO Auto-generated method stub

				}

			});
		} else if (_pointOfView == pointOfView) 
			return false;
		if (_pointOfView != null)
			_pointOfView.camera().stopListening();

		_pointOfView = pointOfView;
		_pointOfView.camera().startListening();
		return true;
	}

	public void setScene(Scene scene) {
		scene.makeCurrent();
	}

	public void setSize(int width, int height) {
		this.width = width; this.height = height;
	}
	
	@Override
	public void setWindow(long window) {
		this.window = window;
		if ( this.window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");
	}
	
	@Override
	public void setWindowSizeCallback(GLFWWindowSizeCallback windowSizeCallback) {
		this.windowSizeCallback = windowSizeCallback;
	}

	@Override
	public int width() {
		return this.width;
	}

	@Override
	public long window() {
		return window;
	}

	public Vector4 getClearColor() {
		return clearColor;
	}

	public void setClearColor(float r, float g, float b, float a) {
		this.clearColor.x = r;
		this.clearColor.y = g;
		this.clearColor.z = b;
		this.clearColor.w = a;
	}
	
	public static final int DEFAULT = 0, OLD = 1, SHADER = 2;
	
	public static GLView newInstance() {
		return newInstance(DEFAULT);
	}
	
	@SuppressWarnings("deprecation")
	public static GLView newInstance(int type) {
		switch (type) {
		case OLD:
			return new GameView1();
		case SHADER:
		case DEFAULT:
		default:
			return new GLShaderView();
		}
	}
}
