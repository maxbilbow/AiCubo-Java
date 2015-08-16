package rmx.engine;



import rmx.RMXObject;
import rmx.engine.math.Matrix4;

public class Scene extends RMXObject {
	
	

	public final Node rootNode = new Node();
	static node current = new node(null,null,null);
	
	public Scene() {

	}
	
//	private static Scene current;
	public static Scene getCurrent() {
		if (current.scene != null)
			return current.scene;
		else
			current.scene = new Scene();
		return current.scene;
	}
	
	public static Scene setCurrent(Scene scene) {
		current.next = new node(current, scene,null);
		current = current.next;
		return current.prev.scene;
	}
	
	public void makeCurrent() {
		setCurrent(this);
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
	

	public void renderScene(Camera cam) {
//		cam.look();
		Matrix4 modelMatrix = (Matrix4) rootNode.transform.localMatrix().clone();
		modelMatrix.negate();
		for (Node child : this.rootNode.getChildren()) {
			child.draw(modelMatrix);
		}
	}


	public void updateSceneLogic() {
		this.rootNode.updateLogic();
	}

}
