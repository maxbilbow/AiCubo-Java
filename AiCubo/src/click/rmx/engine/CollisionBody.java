package click.rmx.engine;

import java.util.LinkedList;

public class CollisionBody extends NodeComponent {

	public final PhysicsBody physicsBody;
//	public final node;
	public CollisionBody(PhysicsBody body) {
		this.physicsBody = body;
		this.setNode(body.getNode());
	}
	public Object getBoundingBox() {
		return null;
	}
	
	public Object getBoundingSphere() {
		return null;
	}

}
