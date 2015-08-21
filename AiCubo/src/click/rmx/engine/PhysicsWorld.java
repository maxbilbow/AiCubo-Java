package click.rmx.engine;

import static click.rmx.RMX.getCurrentFramerate;

import click.rmx.RMXObject;
import click.rmx.engine.math.Matrix4;
import click.rmx.engine.math.Vector3;

public class PhysicsWorld extends RMXObject {
	private Vector3 gravity = new Vector3(0f,-9.8f,0f);
	private float friction = 0.3f;
	public Vector3 getGravity() {
		return gravity;
	}

	public void setGravity(Vector3 gravity) {
		this.gravity.set(gravity);
	}
	
	public void setGravity(float x, float y, float z) {
		this.gravity.x = x;
		this.gravity.y = y;
		this.gravity.z = z;
	}
	
	public void updatePhysics(Node rootNode) {
		for (Node node : rootNode.getChildren()) {
			if (node.physicsBody() != null) {
				node.physicsBody().updatePhysics(this);
			}
		}
	}

	

	public float getFriction() {
		return friction;
	}

	public void setFriction(float friction) {
		this.friction = friction;
	}
	
	
	
}
