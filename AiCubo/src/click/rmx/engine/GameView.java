package click.rmx.engine;


import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
//import org.lwjgl.util.Display;
//import org.lwjgl.util.glu.GLU;

import click.rmx.Bugger;
import click.rmx.RMXObject;
import click.rmx.engine.behaviours.Behaviour;
import click.rmx.engine.gl.CursorCallback;
import click.rmx.engine.gl.GLView;
import click.rmx.engine.gl.KeyCallback;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;

import static click.rmx.RMX.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
//import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
//import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
//import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
//import static org.lwjgl.opengl.GL15.glBindBuffer;
//import static org.lwjgl.opengl.GL15.glBufferData;
//import static org.lwjgl.opengl.GL15.glGenBuffers;
//import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
//import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
//import static org.lwjgl.opengl.GL30.glBindVertexArray;
//import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.*;

public class GameView extends RMXObject implements GLView{

	private Node _pointOfView;
	//	private int _height = 340, _width = 600;
	private int _height = 720, _width = 1280;

	public void setSize(int width, int height) {
		this._width = width; this._height = height;
	}


	// We need to strongly reference callback instances.
	private GLFWErrorCallback _errorCallback;
	private KeyCallback  _keyCallback = KeyCallback.getInstance();

	// The window handle
	private long _window;

	@Override
	public void initGL() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		glfwSetErrorCallback(_errorCallback = errorCallbackPrint(System.err));

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( glfwInit() != GL11.GL_TRUE )
			throw new IllegalStateException("Unable to initialize GLFW");


		// Configure our window
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable



		// Create the window
		_window = glfwCreateWindow(_width, _height, "Hello World!", NULL, NULL);
		if ( _window == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(_window, _keyCallback);

		glfwSetCursorPosCallback(_window, CursorCallback.getInstance());
		// Get the resolution of the primary monitor
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(
				_window,
				(GLFWvidmode.width(vidmode) - _width) / 2,
				(GLFWvidmode.height(vidmode) - _height) / 2
				);

		GLFW.glfwSetWindowSizeCallback(_window, windowSizeCallback = new GLFWWindowSizeCallback() {

			@Override
			public void invoke(long arg0, int width, int height) {
				GameController.getInstance().getView().setSize(width,height);
			}

		});
		// Make the OpenGL context current
		glfwMakeContextCurrent(_window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(_window);


	}
	private static GLFWWindowSizeCallback windowSizeCallback;

	private static void setUpLighting() {
		glShadeModel(GL_SMOOTH);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);
		FloatBuffer fb = BufferUtils.createFloatBuffer(4);
		fb.put(new float[]{0.05f, 0.05f, 0.05f, 1f});
		fb.flip();
		glLightModelfv(GL_LIGHT_MODEL_AMBIENT, fb);
		FloatBuffer fp = BufferUtils.createFloatBuffer(4);
		fp.put(new float[]{50, 100, 50, 1});
		fp.flip();
		glLightfv(GL_LIGHT0, GL_POSITION, fp);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_COLOR_MATERIAL);
		glColorMaterial(GL_FRONT, GL_DIFFUSE);
	}
	@Override
	public void enterGameLoop() {

		//    	glfwGenuffers(1, frameBuffer);

		GLContext.createFromCurrent();


		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); 
		GL11.glClearDepth(1.0); 
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL); 

		setUpLighting();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		pointOfView().camera().perspective(this);
		//   
		LinkedList<Long> time = new LinkedList<>();
		final int rollingAverageCount = 60;
		int counter = 0;
		for (int i = 0; i < rollingAverageCount ; ++i)
			time.add((long) 0);
		//        this.pointOfView().camera().setAspect(_width, _height);
		
		while ( glfwWindowShouldClose(_window) == GL_FALSE ) {
			Instant start = Instant.now();
			Scene scene = Scene.getCurrent();
			Camera camera = pointOfView().camera();
//TODO: Add timer element to Bugger
			Thread sceneLogic = new Thread(() -> Scene.getCurrent().updateSceneLogic());
			sceneLogic.start();
//			scene.updateSceneLogic();

			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			camera.perspective(this);



			glClearColor(0.3f, 0.3f, 0.3f, 0.3f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
			glMatrixMode(GL_MODELVIEW);
			glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
			glLoadIdentity();  

			//            camera.look();
			scene.renderScene(camera);

			glfwSwapBuffers(_window);

			glMatrixMode(GL_PROJECTION);
			// swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
			try {
				sceneLogic.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
			Instant end = Instant.now();
			Duration duration = Duration.between(start, end);
			time.addLast(duration.toMillis());
			time.removeFirst();

			Long total = (long) 0;
			if (++counter == rollingAverageCount) {
				for (Long t : time) {
					total += t;
				}
				Bugger.logAndPrint(total / rollingAverageCount, false);
				counter = 0;
			}
			//            this.didCauseEvent(END_OF_GAMELOOP);
		}
	}

	private RenderDelegate delegate = null;



	public boolean setPointOfView(Node pointOfView) {
		if (pointOfView.camera() == null) {
			pointOfView.setCamera(new Camera());
			pointOfView.addBehaviour(new Behaviour(){

				//			public void lookUp(String speed) {
				//				
				//			}

				@Override
				public void broadcastMessage(String message, Object args) {

					if (message == "lookUp") {
						//					Bugger.logAndPrint(message + ": "+ args , false);
						pointOfView.transform.rotate("pitch", (float) args);
					}
				}
				@Override
				public void update(Node node) {
					// TODO Auto-generated method stub

				}
				@Override
				protected void onAwake() {

				}

				@Override
				public boolean hasLateUpdate() {
					return false;
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

	public RenderDelegate getDelegate() {
		return this.delegate;
	}

	@Override
	public void setDelegate(RenderDelegate delegate) {
		this.delegate = delegate;
	}




	@Override
	public long window() {
		// TODO Auto-generated method stub
		return _window;
	}

	@Override
	public GLFWErrorCallback errorCallback() {
		// TODO Auto-generated method stub
		return _errorCallback;
	}

	@Override
	public KeyCallback keyCallback() {
		// TODO Auto-generated method stub
		return _keyCallback;
	}

	public int width() {
		return _width;
	}

	public void setWidth(int width) {
		this._width = width;
	}

	public int height() {
		return _height;
	}

	public void setHeight(int height) {
		this._height = height;
	}

	@Override
	public Node pointOfView() {
		if (_pointOfView != null || this.setPointOfView(Node.getCurrent()))
			return _pointOfView;
		Bugger.logAndPrint("ERROR: Could Not Set _pointOfView", true);
		return null;
	}

}