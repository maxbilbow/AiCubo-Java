package click.rmx.engine.behaviours;

import click.rmx.engine.Node;
import click.rmx.engine.Scene;
import click.rmx.engine.Transform;

import click.rmx.engine.math.Vector3;

public class SpriteBehaviour extends Behaviour {

	
	public final static String 
	KEY_IS_STUCK = "KEY_IS_STUCK";
	
	public static final int
	STUCK_TRUE = 1, STUCK_FALSE = 0, STUCK_MAYBE = 2;
	
	protected void setStuck(int state) {
		this.setValue(KEY_IS_STUCK, state);
		
	}

	
	
	
	public int stuckState() {
		Object result = this.getValue(KEY_IS_STUCK);
		return result != null ? (int) result : 0;
	}
	
	private Vector3 stuckPosition = new Vector3();
	private long stuckTime = 0;

	public void setMightBeStuck(long tick) {
		this.stuckPosition.set(this.transform().position());
		this.setStuck(STUCK_MAYBE);
		this.stuckTime = tick;
	}
	
	public void setNotStuck() {
		this.setStuck(STUCK_FALSE);
	}
	
	public boolean isStuck(long tick) {
		int state = stuckState();
		if (state == STUCK_MAYBE && tick - stuckTime > interval() ) {
			Transform root = this.transform().rootTransform();
			Vector3 diff = root.position().getVectorTo(stuckPosition);
				state = diff.x < root.getWidth() &&
						diff.y < root.getHeight() &&
						diff.z < root.getLength() ? STUCK_TRUE : STUCK_FALSE;
				this.setValue(KEY_IS_STUCK, state);
				
		} 
		
		return state == STUCK_TRUE ? true : false;
	}
	
	@Override
	public void update(Node node) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void lateUpdate() {
//		this.getNode().transform().localMatrix().setRo
	}
	
	private boolean crouching = false;
	public void crouch() {
		Vector3 scale = this.getNode().transform().scale();
		crouching = !crouching;
		this.getNode().transform().setScale(scale.x, 
				scale.y * (crouching ? 0.9f : 1/0.9f), 
				scale.z);
	}
	
	public void jump(float force) {
		if (crouching) {
			crouch();
		}
		
		this.getNode().physicsBody().applyForce(force, Vector3.Y, Vector3.Zero);
	}
	public void jump() {
		float force = -this.getNode().transform().mass() * Scene.getCurrent().getPhysicsWorld().getGravity().y; //TODO: base this on gravity
		this.jump(force);
	}
	
	public void applyForce (String message) {
		Transform m = this.getNode().transform();//..localMatrix();
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
		Transform m = this.getNode().transform();//.localMatrix();
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

	@Override
	protected void onAwake() {
		// TODO Auto-generated method stub
		
	}




	@Override
	public boolean hasLateUpdate() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}