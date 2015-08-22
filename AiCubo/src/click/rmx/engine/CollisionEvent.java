package click.rmx.engine;

import click.rmx.RMX;
import click.rmx.engine.math.Vector3;

public final class CollisionEvent {
	public final Node nodeA, nodeB;
	public final Vector3 hitPointA;
	public final Vector3 hitPointB;
	public final Vector3 AtoB;
	public final float dist;
	public String log;
	public CollisionEvent(Node nodeA, Node nodeB) {
		this.log = "\nNew Collision Event: " + nodeA.uniqueName() + " vs. " + nodeB.uniqueName();
		this.nodeA = nodeA;
		this.nodeB = nodeB;
		this.hitPointA = Vector3.Zero;
		this.hitPointB = Vector3.Zero;
		Transform A = nodeA.transform.rootTransform();
		Transform B = nodeB.transform.rootTransform();


		Vector3 posA = A.localPosition();
		Vector3 posB = B.localPosition();
		log += "\n        Pos A: " + posA ;
		log += "\n        Pos B: " + posB ;
		AtoB = posA.getVectorTo(posB);
		dist = A.localPosition().getDistanceTo(B.localPosition());
		try {
			this.seperateBodies(A,B);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
			System.exit(1);
		} finally {
			this.processMomentum(A,B);
		}
	}

	private void seperateBodies(Transform A, Transform B) throws Exception {
		//apply force relative to overlapping areas 
		float min = nodeA.transform.radius() + nodeB.transform.radius();
		log += "\n       A to B: " + AtoB ;

		final float delta = min - dist / 2;
		log += "\n     Distance: " + dist + " < " + min + ", delta == " + delta;
		if (dist > min || dist < 0)
			throw new Exception("Distance" + dist + " should be less than min: " + min + "\n" + log);		

		AtoB.normalize();
		AtoB.scale(-delta);
		if (AtoB.isZero()) {
			AtoB.set(-min,0,0);
		}
		log += "\n Translating A: " + AtoB;
		float time = RMX.getCurrentFramerate();
		A.physicsBody().applyForce(time , AtoB, hitPointA);//.translate(AtoB);

		log += "\n Translating B: " + AtoB;
		B.physicsBody().applyForce(-time, AtoB, hitPointB);//translate(AtoB);
	}


	private void processMomentum(Transform A, Transform B)  {

		log += "\n\nCollision Momentum Report: " + nodeA.uniqueName() + " vs. " + nodeB.uniqueName();

		Vector3 va = A.node.physicsBody().getVelocity().clone();
		Vector3 vb = B.node.physicsBody().getVelocity().clone();

		float massA = A.mass();
		float massB = B.mass();

		float resA = A.node.physicsBody().getRestitution();
		float resB = B.node.physicsBody().getRestitution();


		//Transfer of forces
		va.sub(vb); //vb.sub(va);
		nodeA.physicsBody().applyForce(-massB 			  , va, hitPointA);
		nodeB.physicsBody().applyForce( massA 			  , va, hitPointB);

		//Loss of energy
		nodeA.physicsBody().applyForce( massA * (0 - resA), va, hitPointA);
		nodeA.physicsBody().applyForce(-massB * (0 - resB), va, hitPointA);


		//		System.out.println(log);
		//System.out.println("Velocities: " +
		//				"\n     "+ nodeA.uniqueName() + ": " + va + nodeB.uniqueName() + ": " + vb);
	}
}
