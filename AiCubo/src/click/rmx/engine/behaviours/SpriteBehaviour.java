package click.rmx.engine.behaviours;

import click.rmx.engine.Behaviour;
import click.rmx.engine.math.Matrix4;
import click.rmx.engine.math.Vector3;

public class SpriteBehaviour extends Behaviour {

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	public void jump() {
		this.getNode().physicsBody().applyForce(10, Vector3.Y, Vector3.Zero);
	}
	
	public void applyForce (String message) {
		Matrix4 m = this.getNode().transform.localMatrix();
		String[] args = message.split(":");
		String direction = args[0];
		float force = Float.parseFloat(args[1]);
		switch (direction) {
		case "forward":
			this.getNode().physicsBody().applyForce(-force, m.forward(), Vector3.Zero);
			break;
		case "left":
			this.getNode().physicsBody().applyForce(-force, m.left(), Vector3.Zero);
			break;
		case "up":
			this.getNode().physicsBody().applyForce(force, m.up(), Vector3.Zero);
			break;
		default:
			throw new IllegalArgumentException("\"" + direction + "\" not recognised");
		}
//		System.out.println(direction);
	}
	
	public void applyTorque (String message) {
		Matrix4 m = this.getNode().transform.localMatrix();
		String[] args = message.split(":");
		String direction = args[0];
		float force = Float.parseFloat(args[1]);
		switch (direction) {
		case "roll":
			this.getNode().physicsBody().applyTorque(force, m.forward(), Vector3.Zero);
			break;
		case "pitch":
			this.getNode().physicsBody().applyTorque(force, m.left(), Vector3.Zero);
			break;
		case "yaw":
			this.getNode().physicsBody().applyTorque(force, m.up(), Vector3.Zero);
			break;
		default:
			throw new IllegalArgumentException("\"" + direction + "\" not recognised");
		}
		System.out.println(direction);
	}
	
	
	
}