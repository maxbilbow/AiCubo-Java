package click.rmx.engine.physics;

import click.rmx.engine.ANodeComponent;

public class CollisionBody extends ANodeComponent {

	public static final int 
	NO_COLLISIONS = -1,
	DEFAULT_COLLISION_GROUP = 0,
	EXCLUSIVE_COLLISION_GROUP = 01234;
	;
	private int collisionGroup = DEFAULT_COLLISION_GROUP;
	public final PhysicsBody physicsBody;
	
	public final BoundingBox boundingBox;
	public final BoundingSphere boundingSphere;
//	public final node;
	public CollisionBody(PhysicsBody body) {
		this.physicsBody = body;
		this.setNode(body.getNode());
		this.boundingBox = new BoundingBox(body.getNode());
		this.boundingSphere = new BoundingSphere(body.getNode());
	}
	public int getCollisionGroup() {
		return collisionGroup;
	}
	public void setCollisionGroup(int collisionGroup) {
		this.collisionGroup = collisionGroup;
	}
	

	public boolean intersects(CollisionBody other) {
		return this.boundingBox.intersects(other.boundingBox);
	}
	

}

