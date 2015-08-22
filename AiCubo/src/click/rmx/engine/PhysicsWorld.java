package click.rmx.engine;



import static click.rmx.RMX.getCurrentFramerate;

import java.util.Iterator;
import java.util.LinkedList;


import click.rmx.RMXObject;

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
				this.applyGravityToNode(node);
				node.physicsBody().updatePhysics(this);
			}
		}
	}

	private void applyGravityToNode(Node node) {
		float ground = node.transform.scale().y / 2;
		float mass = node.transform.mass();
		float framerate = getCurrentFramerate();
		float height = node.transform.worldMatrix().m31;
		if (height > ground) {
			//			System.out.println(node.getName() + " >> BEFORE: " + m.position());
			node.physicsBody().applyForce(framerate * mass, this.gravity, Vector3.Zero);
//			node.forces.x += g.x * framerate * mass;
//			this.forces.y += g.y * framerate * mass;
//			this.forces.z += g.z * framerate * mass;
		} else if (node.getParent().getParent() == null) {
			node.transform.localMatrix().m31 = ground;
		}
		
	}
	
	public void updateCollisionEvents(Node rootNode) {
//		LinkedList<CollisionBody> unchecked = new LinkedList<>();
		unchecked.clear();
//		checked.clear();
		if (unchecked.isEmpty())
			for (Node node : rootNode.getChildren()) {
				if (node.collisionBody() != null) {
					unchecked.add(node.collisionBody());			
				}
			}
		
		if (!unchecked.isEmpty()) {
			checkForCollisions(unchecked, null);
//			System.out.println(count + " collisions found in " + checks + " checks");
			count = checks = 0;
//			swapLists();
		} 
			
	}


//	LinkedList<CollisionBody> checked = new LinkedList<>();
	LinkedList<CollisionBody> unchecked = new LinkedList<>();
	int count = 0; int checks = 0;
	public void checkForCollisions(LinkedList<CollisionBody> unchecked, LinkedList<CollisionBody> checked) {
		if (unchecked.isEmpty()) {
//			System.out.println(count + " collisions found in " + checks + " checks");
			return;
//			count = checks = 0;
//			swapLists();
		} else {
			CollisionBody A = unchecked.removeFirst();
			Iterator<CollisionBody> i = unchecked.iterator();
			while (i.hasNext()) {
				CollisionBody B = i.next();
				checks++;
				if (this.checkForCollision(A,B)) {
					count++;
//					if (unchecked.remove(A))
//						System.err.println(A.uniqueName() + " removed twie");//checked.addLast(A);
					if (!unchecked.remove(B))
						System.err.println(B.uniqueName() + " was not removed from unchecked");//checked.addLast(B);
					if (!unchecked.isEmpty()) {
						this.checkForCollisions(unchecked, checked);
						return;
					}
				}
			}
			if (!unchecked.isEmpty()) {
				this.checkForCollisions(unchecked, checked);
			}
		}
	}
	Vector3 collisionDistance = new Vector3();
	private boolean checkForCollision(CollisionBody A, CollisionBody B) {
		float min = A.getNode().transform.radius() +
				B.getNode().transform.radius();
		collisionDistance.set(
				A.getNode().transform.position());
		collisionDistance.sub(
				B.getNode().transform.position());
		float distance = Math.abs(collisionDistance.length());
//				A.getNode().transform.position().length() +
//				A.getNode().transform.position().length()
//				);
	
		boolean isHit = distance < min;
		if (isHit) {
			CollisionEvent e = new CollisionEvent(A.getNode(),B.getNode());
			if (collisionDelegate != null)
				collisionDelegate.handleCollision(A.getNode(), B.getNode(), e);
//			System.out.println("HIT at distance: " + distance + " < " + min);
		} else {
//			System.out.println("MISS at distance: " + distance + " > " + min);
		}
		return isHit;
	}
	
	private CollisionDelegate collisionDelegate;
	
	

	public float getFriction() {
		return friction;
	}

	public void setFriction(float friction) {
		this.friction = friction;
	}

	public CollisionDelegate getCollisionDelegate() {
		return collisionDelegate;
	}

	public void setCollisionDelegate(CollisionDelegate collisionDelegate) {
		this.collisionDelegate = collisionDelegate;
	}
	
	
	
}
