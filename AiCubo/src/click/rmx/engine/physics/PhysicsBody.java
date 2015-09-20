package click.rmx.engine.physics;




import click.rmx.engine.Node;
import click.rmx.engine.ANodeComponent;
import click.rmx.engine.math.Vector3;
import static click.rmx.engine.physics.CollisionBody.*;
public class PhysicsBody extends ANodeComponent {

	private float mass = 1.0f; 
	private float friction = 0.1f; 
	private float rollingFriction = 0.5f;
	private float damping = 0.1f; 
	private float rotationalDamping = 1.0f;
	private float restitution = 0.2f;
	private boolean effectedByGravity = true;
	private PhysicsBodyType type;
	private Vector3 forces, torque, velocity, rotationalVelocity;
	private CollisionBody collisionBody;

	public static PhysicsBody newStaticBody() {
		PhysicsBody body = new PhysicsBody();
		body.setType(PhysicsBodyType.Static);

		return body;
	}

	@Override
	public void setNode(Node node) {
		super.setNode(node);
		switch (this.type) {
		case Dynamic:
			this.collisionBody = new CollisionBody(this);
			this.collisionBody.setCollisionGroup(DEFAULT_COLLISION_GROUP);
			break;
		case Static:
			this.collisionBody = new CollisionBody(this);
			this.collisionBody.setCollisionGroup(DEFAULT_COLLISION_GROUP);
			break;
		case Kinematic:
			this.collisionBody = new CollisionBody(this);
			this.collisionBody.setCollisionGroup(EXCLUSIVE_COLLISION_GROUP);
			break;
		case Transient:
			this.collisionBody = new CollisionBody(this);
			this.collisionBody.setCollisionGroup(NO_COLLISIONS);
			break;
		}
	}

	public static PhysicsBody newDynamicBody() {
		PhysicsBody body = new PhysicsBody();
		body.setType(PhysicsBodyType.Dynamic);

		return body;
	}

	public static PhysicsBody newKinematicBody() {
		PhysicsBody body = new PhysicsBody();
		body.setType(PhysicsBodyType.Kinematic);

		return body;
	}

	public static PhysicsBody newTransientBody() {
		PhysicsBody body = new PhysicsBody();
		body.setType(PhysicsBodyType.Transient);

		return body;
	}

	private PhysicsBody() {
		//		this.lastPosition = new Vector3();
		//		this.type = PhysicsBodyType.Dynamic;
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
		//		this.velocity.set(this.getNode().transform().position());
		//		this.velocity.sub(this.lastPosition);
		return this.velocity;
	}
	public float getMass() {
		return mass;
	}

	public float TotalMass() {
		return this.getNode().transform().mass();
	}



	public void setMass(float mass) {
		this.mass = mass;
	}

	public void applyForce(float force, Vector3 direction, Vector3 atPoint) {
		if (this.type != PhysicsBodyType.Dynamic)
			return;
		force /= (1 + this.damping);
		this.forces.x += direction.x * force;
		this.forces.y += direction.y * force;
		this.forces.z += direction.z * force;
	}


	public void applyTorque(float force, Vector3 axis, Vector3 atPoint) {
		if (this.type != PhysicsBodyType.Dynamic)
			return;
		force /= (1 + this.rotationalDamping);
		this.torque.x += axis.x * force;
		this.torque.y += axis.y * force;
		this.torque.z += axis.z * force;
	}

	public static final String 
	PITCH = "pitch",
	YAW = "yaw",
	ROLL = "roll",
	FORWARD = "pitch",
	LEFT = "yaw",
	UP = "roll",
	X = "x",
	Y = "y",
	Z = "z";
	
	public void applyTorque(float force, String axis, Vector3 atPoint) {
		switch (axis) {
		case PITCH:
			applyTorque(force, this.transform().left(), atPoint);
			break;
		case YAW:
			applyTorque(force, this.transform().up(), atPoint);
			break;
		case ROLL:
			applyTorque(force, this.transform().forward(), atPoint);
			break;
		case X:
			applyTorque(force, Vector3.X, atPoint);
			break;
		case Y:
			applyTorque(force, Vector3.Y, atPoint);
			break;
		case Z:
			applyTorque(force, Vector3.Z, atPoint);
			break;
		}
	}

	public void applyForce(float force, String axis, Vector3 atPoint) {
		switch (axis) {
		case LEFT:
			applyForce(force, this.transform().left(), atPoint);
			break;
		case UP:
			applyForce(force, this.transform().up(), atPoint);
			break;
		case FORWARD:
			applyForce(force, this.transform().forward(), atPoint);
			break;
		case X:
			applyForce(force, Vector3.X, atPoint);
			break;
		case Y:
			applyForce(force, Vector3.Y, atPoint);
			break;
		case Z:
			applyForce(force, Vector3.Z, atPoint);
			break;
		}
	}

	public void updatePhysics(PhysicsWorld physics){
		if (this.type != PhysicsBodyType.Dynamic)
			return;
		Node node = this.getNode();


		float mass = TotalMass();
		//Update velocity with forces
		this.velocity.x += this.forces.x / mass;
		this.velocity.y += this.forces.y / mass;
		this.velocity.z += this.forces.z / mass;
		this.forces.set(0,0,0);

		//Updaye rotational velocity with torque
		this.rotationalVelocity.x += this.torque.x / mass;
		this.rotationalVelocity.y += this.torque.y / mass;
		this.rotationalVelocity.z += this.torque.z / mass;
		this.torque.set(0,0,0);



		//apply velocities to translation
		node.transform().translate(this.velocity);
		node.transform().rotate(this.rotationalVelocity.x, 1, 0, 0);
		node.transform().rotate(this.rotationalVelocity.y, 0, 1, 0);
		node.transform().rotate(this.rotationalVelocity.z, 0, 0, 1);

		//lose energy
		this.velocity.scale(1 / (1 + this.friction));
		this.rotationalVelocity.scale(1 / (1 + this.rollingFriction));

	}


	//	public static final String
	//	ApplyForce = "applyForce";



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

	public PhysicsBodyType getType() {
		return type;
	}

	public void setType(PhysicsBodyType type) {
		this.type = type;
	}

	public CollisionBody getCollisionBody() {
		return collisionBody;
	}

	public void setCollisionBody(CollisionBody collisionBody) {
		this.collisionBody = collisionBody;
	}

	public void setEffectedByGravity(Boolean effectedByGravity) {
		this.effectedByGravity = effectedByGravity;
	}
	
	public boolean isEffectedByGravity() {
		return this.effectedByGravity;
	}

}
