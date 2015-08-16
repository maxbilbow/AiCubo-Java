package rmx.engine;




import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Q;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_E;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

import org.lwjgl.Sys;


import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;

import rmx.RMXObject;
import rmx.gl.GLView;
import rmx.gl.KeyCallback;
import rmx.gl.KeyStates;
import rmx.gl.SharedLibraryLoader;



public class GameController extends RMXObject implements RenderDelegate, Runnable  {
	private GLView view;

	protected GameController() {	
		this.setView(new GameView());
		
	}
	
	public static GameController getInstance() {
		if(singleton == null) {
			  synchronized(GameController.class) {
		    	 if(singleton == null) {
		    	   singleton = new GameController();
		    	   singleton.setView(new GameView());
		       }
		     }
		  }
		  return singleton;
	}
	
	private static GameController singleton = new GameController();
	
	public void setup() {		
		Scene scene = Scene.getCurrent();
		Node n = Node.getCurrent();
		
//		n.setGeometry(new Geometry());
//		n.addBehaviour(new ABehaviour());
		scene.rootNode.addChild(n);
//		Node child = new Node();
//		child.transform.setPosition(10,4,12);
//		n.addChild(child);
		
		Node cube = new Node();
		cube.transform.setPosition(0,0,5);
		cube.setGeometry(Geometry.cube());
		scene.rootNode.addChild(cube);
//		Node cube2 = new Node();
//		cube.transform.setPosition(-10,0,-10);
//		scene.rootNode.addChild(cube2);
//		cube2.setGeometry(Geometry.cube());
//		Node cube3 = new Node();
//		cube.transform.setPosition(-10,0,10);
//		cube3.addChild(cube2);
//		scene.rootNode.addChild(cube3);
//		cube3.setGeometry(Geometry.cube());
//		Node cube4 = new Node();
//		cube.transform.setPosition(10,-5,10);
//		scene.rootNode.addChild(cube4);
//		cube4.setGeometry(Geometry.cube());
		
	}

	public void run() {
        System.out.println("Hello LWJGL " + Sys.getVersion() + "!");
       
        try {
        	SharedLibraryLoader.load();
        	this.setup();
            this.view.initGL();
            this.view.enterGameLoop();
 
            // Release window and window callbacks
            glfwDestroyWindow(view.window());
            view.keyCallback().release();
        } finally {
            // Terminate GLFW and release the GLFWerrorfun
            glfwTerminate();
            
			view.errorCallback().release();
        }
    }

	@Override
	public void updateBeforeScene() {
		this.repeatedKeys();	
	}

	@Override
	public void updateAfterScene() {
		
	}
	
	KeyStates keys = KeyCallback.getInstance().keyStates;
	
	private void repeatedKeys() {
		Node player = Node.getCurrent();
	
		if (this.keys.getOrDefault(GLFW_KEY_W, false)) {
			player.broadcastMessage("move","forward:-1");
		}
		
		if (this.keys.getOrDefault(GLFW_KEY_S, false)) {
			player.broadcastMessage("move","forward:1");
		}
		
		if (this.keys.getOrDefault(GLFW_KEY_A, false)) {
			player.broadcastMessage("move","left:-1");
		}
		
		if (this.keys.getOrDefault(GLFW_KEY_D, false)) {
			player.broadcastMessage("move","left:1");
		}
		
		if (this.keys.getOrDefault(GLFW_KEY_Q, false)) {
			player.broadcastMessage("move","up:1");
		}
		
		if (this.keys.getOrDefault(GLFW_KEY_E, false)) {
			player.broadcastMessage("move","up:-1");
		}
		if (this.keys.getOrDefault(GLFW_KEY_SPACE, false)) {
			player.broadcastMessage("jump");
		}
		
		if (this.keys.getOrDefault(GLFW_KEY_RIGHT, false)) {
			player.broadcastMessage("move","yaw:1");
		}
		
		if (this.keys.getOrDefault(GLFW_KEY_LEFT, false)) {
			player.broadcastMessage("move","yaw:-1");
		}
		
		if (this.keys.getOrDefault(GLFW_KEY_UP, false)) {
			player.broadcastMessage("move","pitch:1");
		}
		
		if (this.keys.getOrDefault(GLFW_KEY_DOWN, false)) {
			player.broadcastMessage("move","pitch:-1");
		}
		
		if (this.keys.getOrDefault(GLFW_KEY_X, false)) {
			player.broadcastMessage("move","roll:1");
		}
		
		if (this.keys.getOrDefault(GLFW_KEY_Z, false)) {
			player.broadcastMessage("move","roll:-1");
		}
	}
	
	
	public void setView(GLView view) {
		this.view = view;
		this.view.setDelegate(this);
	}
//	private Thread thread;
	public void Start() {
//			thread = new Thread(this,"Game");
//			thread.start();
			this.run();
	}
	

}

