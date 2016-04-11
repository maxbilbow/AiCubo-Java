package com.maxbilbow.aicubo.engine.collision.type;

import com.maxbilbow.aicubo.engine.math.Vector3;
import com.maxbilbow.aicubo.model.ANodeComponent;
import com.maxbilbow.aicubo.engine.physics.PhysicsBody;

public class CollisionBody extends ANodeComponent
{

  public static final int
          NO_COLLISIONS             = -1,
          DEFAULT_COLLISION_GROUP   = 0,
          EXCLUSIVE_COLLISION_GROUP = 01234;
  ;
  private int collisionGroup = DEFAULT_COLLISION_GROUP;
  public final PhysicsBody physicsBody;

  public final BoundingBox    boundingBox;
  public final BoundingSphere boundingSphere;

  //	public final node;
  public CollisionBody(PhysicsBody body)
  {
    this.physicsBody = body;
    this.setNode(body.getNode());
    this.boundingBox = new BoundingBox(body.getNode());
    this.boundingSphere = new BoundingSphere(body.getNode());
  }

  public int getCollisionGroup()
  {
    return collisionGroup;
  }

  public void setCollisionGroup(int collisionGroup)
  {
    this.collisionGroup = collisionGroup;
  }


  public boolean intersects(CollisionBody other)
  {
    return this.boundingBox.intersects(other.boundingBox);
  }

  /**
   *
   * @return A clone of this body's velocity.
   */
  public Vector3 getVelocity()
  {
    return physicsBody.getVelocity().clone();
  }

  public Vector3 getCenterOfGravity()
  {
    return transform().position();
  }
}

