package rmx.engine;




import org.lwjgl.glfw.GLFW;

import rmx.RMXObject;
import rmx.gl.GameView;
import rmx.gl.SharedLibraryLoader;


public class GameController extends RMXObject implements RenderDelegate  {
	private GameView view;
	protected GameController() {	
		this.setView(new GameView());
	}
	
	public static GameController getInstance() {
		if(singleton == null) {
			  synchronized(GameController.class) {
		    	 if(singleton == null) {
		    	   singleton = new GameController();
		       }
		     }
		  }
		  return singleton;
	}
	
	private static GameController singleton = new GameController();
	
	public void Start() {
		this.view.run();
	}
	
	
	
	

	public static void main(String[] args) {
    	SharedLibraryLoader.load();
    	GameController game = GameController.getInstance();
		game.Start();
    }

	@Override
	public void renderScene() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateLogic() {
		System.out.println(GLFW.glfwGetTime());
	}

	public GameView getView() {
		return view;
	}

	public void setView(GameView view) {
		this.view = view;
		view.setRenderDelegate(this);
	}

}

