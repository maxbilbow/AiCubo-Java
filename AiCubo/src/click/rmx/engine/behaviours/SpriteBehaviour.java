package click.rmx.engine.behaviours;

import click.rmx.engine.Behaviour;
import click.rmx.engine.Transform;

import click.rmx.engine.math.Vector3;

public class SpriteBehaviour extends Behaviour {

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void lateUpdate() {
//		this.getNode().transform.localMatrix().setRo
	}
	
	private boolean crouching = false;
	public void crouch() {
		Vector3 scale = this.getNode().transform.scale();
		crouching = !crouching;
		this.getNode().transform.setScale(scale.x, 
				scale.y * (crouching ? 0.9f : 1/0.9f), 
				scale.z);
	}
	
	public void jump() {
		if (crouching) {
			crouch();
		}
		float force = this.getNode().transform.mass() * 20.0f; //TODO: base this on gravity
		this.getNode().physicsBody().applyForce(force, Vector3.Y, Vector3.Zero);
	}
	
	public void applyForce (String message) {
		Transform m = this.getNode().transform;//..localMatrix();
		String[] args = message.split(":");
		String direction = args[0];
		float force = Float.parseFloat(args[1]);
		switch (direction) {
		case "forward":
			this.getNode().physicsBody().applyForce(force, m.forward(), Vector3.Zero);
			break;
		case "left":
			this.getNode().physicsBody().applyForce(force, m.left(), Vector3.Zero);
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
		Transform m = this.getNode().transform;//.localMatrix();
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

	}
	
	
	
}