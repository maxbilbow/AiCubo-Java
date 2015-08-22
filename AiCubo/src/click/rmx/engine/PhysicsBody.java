package click.rmx.engine;

import static click.rmx.RMX.getCurrentFramerate;


import click.rmx.engine.math.Vector3;

public class PhysicsBody extends NodeComponent {

	private float mass = 1.0f; 
	private float friction = 0.1f; 
	private float rollingFriction = 0.5f;
	private float damping = 0.1f; 
	private float rotationalDamping = 1.0f;
	private float restitution = 0.2f;
	
	private Vector3 forces, torque, velocity, rotationalVelocity;
	
	
	public PhysicsBody() {
//		this.lastPosition = new Vector3();
		this.forces = new Vector3();
		this.torque = new Vector3();
		this.velocity = new Vector3();
		this.rotationalVelocity = new Vector3();
	}
	
	public void setDamping(float damping) {
		this.damping = damping < 0 ? 0 : damping;
	}
	public float getDamping() {
		return this.damping;
	}
	
	public void setRotationalDamping(float damping) {
		this.rotationalDamping = damping < 0 ? 0 : damping;
	}
	
	public void setFriction(float friction) {
		this.friction = friction < 0 ? 0 : friction;
	}
	
	public void setRollingFriction(float friction) {
		this.rollingFriction = friction < 0 ? 0 : friction;
	}
	
	
	public Vector3 getVelocity() {
//		this.velocity.set(this.getNode().transform.position());
//		this.velocity.sub(this.lastPosition);
		return this.velocity;
	}
	public float getMass() {
		return mass;
	}

	public float TotalMass() {
		return this.getNode().transform.mass();
	}
	
	
	
	public void setMass(float mass) {
		this.mass = mass;
	}

	public void applyForce(float force, Vector3 direction, Vector3 atPoint) {
//		if (Math.abs(force) > this.damping) {
			force /= (1 + this.damping);
			this.forces.x += direction.x * force;
			this.forces.y += direction.y * force;
			this.forces.z += direction.z * force;
//		}
	}
	
	public void applyTorque(float force, Vector3 axis, Vector3 atPoint) {
//		if (Math.abs(force) > this.rotationalDamping) {
		force /= (1 + this.rotationalDamping);
		this.torque.x += axis.x * force;
		this.torque.y += axis.y * force;
		this.torque.z += axis.z * force;
//		}
	}

	public void updatePhysics(PhysicsWorld physics){
		Node node = this.getNode();
		
		
		this.applyGravity(physics.getGravity());

		this.updateVelocity();
		
		node.transform.translate(this.velocity);
		node.transform.rotate(this.rotationalVelocity.x, 1, 0, 0);
		node.transform.rotate(this.rotationalVelocity.y, 0, 1, 0);
		node.transform.rotate(this.rotationalVelocity.z, 0, 0, 1);
		
		
	}
	
	private void updateVelocity() {
		float mass = TotalMass();
		
		this.velocity.x += this.forces.x / mass;
		this.velocity.y += this.forces.y / mass;
		this.velocity.z += this.forces.z / mass;
		this.forces.set(0,0,0);
		
		this.rotationalVelocity.x += this.torque.x / mass;
		this.rotationalVelocity.y += this.torque.y / mass;
		this.rotationalVelocity.z += this.torque.z / mass;
		this.torque.set(0,0,0);
		
		this.velocity.scale(1 / (1 + this.friction));
		this.rotationalVelocity.scale(1 / (1 + this.rollingFriction));
	}
	
//	public static final String
//	ApplyForce = "applyForce";

	private void applyGravity(Vector3 g) {
		float ground = this.getNode().transform.scale().y / 2;
		float mass = getNode().transform.mass();
		float framerate = getCurrentFramerate();
		float height = this.getNode().transform.worldMatrix().m31;
		if (height > ground) {
			//			System.out.println(node.getName() + " >> BEFORE: " + m.position());
			this.forces.x += g.x * framerate * mass;
			this.forces.y += g.y * framerate * mass;
			this.forces.z += g.z * framerate * mass;
		} else if (this.getNode().getParent().getParent() == null) {
			this.transform().localMatrix().m31 = ground;
		}
		
	}

	public float getRestitution() {
		return restitution;
	}

	public void setRestitution(float restitution) {
		if (restitution > 1)
			this.restitution = 1;
		else if (restitution < 0)
			this.restitution = 0;
		else
			this.restitution = restitution;
	}
}
