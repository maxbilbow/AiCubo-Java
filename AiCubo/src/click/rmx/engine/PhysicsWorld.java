package click.rmx.engine;

import static click.rmx.RMX.getCurrentFramerate;

import click.rmx.RMXObject;
import click.rmx.engine.math.Matrix4;
import click.rmx.engine.math.Vector3;

public class PhysicsWorld extends RMXObject {
	private Vector3 gravity = new Vector3(0f,-9.8f,0f);

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
				applyGravityTo(node);
			}
		}
	}

	private void applyGravityTo(Node node) {
		Matrix4 m = node.transform.localMatrix();
		float g = 0;
		float mass = node.transform.mass();
		float framerate = getCurrentFramerate();
//		System.out.println(node.getName() + " >> BEFORE: " + m.position());
		m.m30 += this.gravity.x * framerate * mass;
		m.m31 += this.gravity.y * framerate * mass;
		m.m32 += this.gravity.z * framerate * mass;
//		System.out.println(node.getName() + " >>  AFTER: " + m.position());
//		m.translate(x, y, z);
		if (m.m31 < g)
			m.m31 = g;
	}
	
}