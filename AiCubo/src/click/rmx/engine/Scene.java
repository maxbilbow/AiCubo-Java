package click.rmx.engine;



import click.rmx.Bugger;
import click.rmx.RMXObject;
import click.rmx.engine.math.Matrix4;

public class Scene extends RMXObject {
	
	
	private PhysicsWorld physicsWorld = new PhysicsWorld();
	public final Node rootNode;
	private static Scene _current;// = new node(null,null,null);
	
	public Scene() {
		Bugger.log("Scene initializing...");
		this.rootNode = new Node();
		if (_current == null)
			_current = this;
	}
	
//	private static Scene current;
	public static Scene getCurrent() {
		Bugger.log("Get current scene...");
		if (_current != null)
			return _current;
		else
			_current = new Scene();
		return _current;
	}
	
//	public static Scene setCurrent(Scene scene) {
//		Scene old = _current;
//		_current = scene;
//		return old;
//	}
	
	public void makeCurrent() {
		_current = this;
	}
	
	
	
	protected static class node {
        Scene scene;
        node next;
        node prev;

        node(node prev, Scene scene, node next) {
            this.scene = scene;
            this.next = next;
            this.prev = prev;
        }
     
    }
	
	private RenderDelegate renderDelegate;
	

	public void renderScene(Camera cam) {
		 if (this.renderDelegate != null) 
     		this.renderDelegate.updateBeforeSceneRender(cam);
//		cam.look();
		Matrix4 modelMatrix = cam.modelViewMatrix();
		//modelMatrix.negate();
		for (Node child : this.rootNode.getChildren()) {
			child.draw(modelMatrix);
		}
		
	}


	public void updateSceneLogic() {
		 if (this.renderDelegate != null) 
	     		this.renderDelegate.updateBeforeSceneLogic();
		this.rootNode.updateLogic();
		this.physicsWorld.updatePhysics(this.rootNode);
		this.physicsWorld.updateCollisionEvents(this.rootNode);
		this.rootNode.updateAfterPhysics();
	}

	public RenderDelegate getRenderDelegate() {
		return renderDelegate;
	}

	public void setRenderDelegate(RenderDelegate renderDelegate) {
		Bugger.log("Setting render delegate: " + renderDelegate);
		this.renderDelegate = renderDelegate;
	}

}
