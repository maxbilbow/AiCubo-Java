package click.rmx.engine.gl;

import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

import click.rmx.Bugger;
import click.rmx.engine.Node;

public interface GLView {
	void setWindowSizeCallback(GLFWWindowSizeCallback cbfun);

	void initGL(long window);
	void enterGameLoop(long window);
	long window();
	GLFWErrorCallback errorCallback();
	KeyCallback keyCallback();
	int height();
	int width();
//	void setSize(int width, int height);
	boolean setPointOfView(Node cam);
	Node pointOfView();
	default void run() {

		try {

			Bugger.log("Load Shared Libraries");
			SharedLibraryLoader.load();

			// Initialize GLFW. Most GLFW functions will not work before doing this.
			if ( glfwInit() != GL11.GL_TRUE )
				throw new IllegalStateException("Unable to initialize GLFW");
			long window;
			this.setWindow(window = this.initWindow());

			this.initGLCallbacks(window);

			Bugger.log("Init GL");
			
			GLContext.createFromCurrent();
			this.initGL(window);

			Bugger.log("Enter Gameloop");
			this.enterGameLoop(window);

			// Release window and window callbacks
			glfwDestroyWindow(window);
			keyCallback().release();
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			// Terminate GLFW and release the GLFWerrorfun
			glfwTerminate();
			errorCallback().release();
		}
	}

	default long initWindow(){
		long window;
		this.setWindow(window = glfwCreateWindow(width(), height(), "Hello World!", NULL, NULL));
		// Configure our window
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable


		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		// Center our window
		glfwSetWindowPos(
				this.window(),
				(GLFWvidmode.width(vidmode) - width()) / 2,
				(GLFWvidmode.height(vidmode) - height()) / 2
				);

		// Make the OpenGL context current
		glfwMakeContextCurrent(window);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(window);
		return this.window();
	}

	void setWindow(long window);
	void initGLCallbacks(long window);

}
