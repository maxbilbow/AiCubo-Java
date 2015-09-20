package click.rmx.engine.physics;

import click.rmx.RMX;

import click.rmx.engine.Node;
import click.rmx.engine.Scene;
import click.rmx.engine.Transform;
import click.rmx.engine.math.Vector3;

public final class CollisionEvent {
	public final Node nodeA, nodeB;
	public final Vector3 hitPointA;
	public final Vector3 hitPointB;
	public final Vector3 AtoB;
	public final float startingDistance;
	public final float planeDistance;
	private boolean isPrevented = false;
	public String axis;
	public String log;
	private final int key;
	public CollisionEvent(Node nodeA, Node nodeB, int key) {
		this.key = key;
		this.log = "\nNew Collision Event: " + nodeA.uniqueName() + " vs. " + nodeB.uniqueName();
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.hitPointA = Vector3.Zero;
		this.hitPointB = Vector3.Zero;
		this.planeDistance = this.getPlaneDistance();

		AtoB = nodeA.transform().position().getVectorTo(nodeB.transform().position());
		startingDistance = this.getDistance(); // A.localPosition().getDistanceTo(B.localPosition());
		if (Float.isNaN(startingDistance))
			System.err.println(AtoB + " is not a number - " + this);
		nodeA.broadcastMessage("onCollisionStart",this);
		nodeB.broadcastMessage("onCollisionStart",this);
	}

	/**
	 * 
	 * @param key prevents this method being called illegally
	 */
	public void processCollision(int key) {
		if (this.isPrevented)
			return;
		if (this.key != key)
			throw new IllegalArgumentException("key mismatch: " + key + " += " + this.key);

		this.seperateBodies(nodeA.transform(),nodeB.transform());
		this.processMomentum(nodeA.transform(),nodeB.transform());
		nodeA.broadcastMessage("onCollisionEnd", this);
		nodeB.broadcastMessage("onCollisionEnd", this);
	}

	private void seperateBodies(Transform A, Transform B)  {

		//apply force relative to overlapping areas 
		float min;
		float dist = this.startingDistance;
		//		if (PhysicsWorld.UseBoundingBox)
		min = nodeA.collisionBody().boundingSphere.radius() + nodeB.collisionBody().boundingSphere.radius();
		//		else
		//			min = nodeA.transform.radius() + nodeB.transform.radius();
		log += "\n       A to B: " + AtoB ;

		final float delta = min - dist / 2;
		log += "\n     Distance: " + dist + " < " + min + ", delta == " + delta;
		if (dist > min || dist < 0) {
			System.err.println("Distance" + dist + " should be less than min: " + min + "\n" + log);
			//			throw new IllegalArgumentException("Distance" + dist + " should be less than min: " + min + "\n" + log);
		}
		//			System.err.println("Distance" + dist + " should be less than min: " + min);

		AtoB.normalize();
		AtoB.scale(-delta);
		if (AtoB.isZero()) {
			AtoB.set(-min,0,0);
		}

		float diff = this.planeDistance;
		if (Scene.getCurrent().tick() > 0) {
			if (A.physicsBody().getType() == PhysicsBodyType.Dynamic && !A.physicsBody().getVelocity().isZero())
				A.rootTransform().stepBack(axis);// -diff * sign);
			else if (B.physicsBody().getType() == PhysicsBodyType.Dynamic && !B.physicsBody().getVelocity().isZero())
				B.rootTransform().stepBack(axis);//diff * sign);
		}
		
		if (A.collisionBody().boundingBox.intersects(B.collisionBody().boundingBox) ) { 
			
//			diff = this.getPlaneDistance();
//			if (A.physicsBody().getType() == PhysicsBodyType.Dynamic)
//				A.rootTransform().moveAlongAxis(axis, -diff);// -diff * sign);
//			else if (B.physicsBody().getType() == PhysicsBodyType.Dynamic)
//				B.rootTransform().moveAlongAxis(axis, diff);//diff * sign);

					float time = RMX.rmxGetCurrentFramerate();
					float escapeForce = time;// * AtoB.length();
					Vector3 dir = //AtoB.getNormalized();
							new Vector3(axis == "x" ? 1 : 0, axis == "y" ? 1 : 0, axis == "z" ? 1 : 0
									);

								A.physicsBody().applyForce(escapeForce * (A.mass() + diff), dir, hitPointA);//.translate(AtoB);

								B.physicsBody().applyForce(-escapeForce * (B.mass() + diff), dir, hitPointB);//translate(AtoB);


		}





	}

	public float getPlaneDistance() {
		BoundingBox boxA = nodeA.collisionBody().boundingBox; BoundingBox boxB = nodeB.collisionBody().boundingBox;
		Vector3 posA = nodeA.transform().localPosition(); Vector3 posB = nodeB.transform().localPosition();


		float dx = boxA.xMax() + posA.x - boxB.xMin() - posB.x; //left
		float dx2 = boxB.xMax() + posB.x - boxA.xMin() - posA.x;//right
		float dy = boxA.yMax() + posA.y - boxB.yMin() - posB.y; //top
		float dy2 = boxB.yMax() + posB.y - boxA.yMin() - posA.y;//bottom
		float dz = boxA.zMax() + posA.z - boxB.zMin() - posB.z; //front
		float dz2 = boxB.zMax() + posB.z - boxA.zMin() - posA.z;//back
		String axis = "x"; float diff = dx; float sign = 1;

		//		System.out.println(" dx: " + dx + ",  dy: " + dy + ",  dz: " + dz);
		//		System.out.println("dx2: " + dx2 + ", dy2: " + dy2 + ", dz2: " + dz2);
		//		System.out.println("dx2: " + dx2 + ", dy2: " + dy2 + ", dz2: " + dz2);

		if (dx2 < diff) {
			diff = dx2;
			axis = "x";
			sign = -1;
		}
		if (dy < diff) {
			diff = dy;
			axis = "y";
			sign = 1;
		}
		if (dy2 < diff) {
			diff = dy2;
			axis = "y";
			sign = -1;
		}
		if (dz < diff) {
			diff = dz;
			axis = "z";
			sign = 1;
		}
		if (dz2 < diff) {
			diff = dz2;
			axis = "z";
			sign = -1;
		}
		this.axis = axis;
		return diff * sign;
	}

	private void processMomentum(Transform A, Transform B)  {
	
		log += "\n\nCollision Momentum Report: " + nodeA.uniqueName() + " vs. " + nodeB.uniqueName();

		Vector3 Va = A.node.physicsBody().getVelocity();
		Vector3 Vb = B.node.physicsBody().getVelocity();
		Vector3 direction = Vector3.makeSubtraction(Va, Vb);
		if (direction.isZero())
			return;
		else
			direction.normalize();

		float m1 = A.mass();
		float m2 = B.mass();

		//		float res = (1 - A.node.physicsBody().getRestitution()) * (1 - B.node.physicsBody().getRestitution());
		//		float resA = 1 - A.node.physicsBody().getRestitution();
		//		float resB = 1 - B.node.physicsBody().getRestitution();




		float lossA = 1 - A.node.physicsBody().getRestitution();
		float lossB = 1 - B.node.physicsBody().getRestitution();
		float v1 = Va.length();
		float v2 = Vb.length();

		float mass = m1 + m2 + 0.01f;
		float diffMass = m1 - m2;
		float forceOnA = -m2;// (diffMass * v1 + 2 * m2 * v2 ) / mass;

		float forceOnB = m1;//(2 * m1 * v1 - diffMass * v2 ) / mass;

		//Transfer of forces

		
		nodeA.physicsBody().applyForce( forceOnA * lossA , direction, hitPointA);
		nodeB.physicsBody().applyForce( forceOnB * lossB , direction, hitPointB);

		//Loss of energy
		//				nodeA.physicsBody().applyForce(-m1 * lossA, direction, hitPointA);
		//				nodeA.physicsBody().applyForce(-m2 * lossB, direction, hitPointA);


		//		System.out.println(log);
		//System.out.println("Velocities: " +
		//				"\n     "+ nodeA.uniqueName() + ": " + va + nodeB.uniqueName() + ": " + vb);
	}


	public float getDistance() {

		return Vector3.makeSubtraction(nodeA.transform().position(), nodeB.transform().position()).length();
	}

	public boolean isPrevented() {
		return this.isPrevented;
	}

	public void preventCollision(boolean isPrevented) {
		this.isPrevented = isPrevented;
	}

	public Node getOther(Node node) {
		return node == nodeA ? nodeB : nodeA;
	}

}
