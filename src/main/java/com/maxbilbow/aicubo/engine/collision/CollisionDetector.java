package com.maxbilbow.aicubo.engine.collision;


import com.maxbilbow.aicubo.engine.collision.type.CollisionBody;
import com.maxbilbow.aicubo.engine.collision.type.CollisionData;
import com.maxbilbow.aicubo.model.collision.CollisionEvent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.maxbilbow.aicubo.engine.collision.type.CollisionBody.EXCLUSIVE_COLLISION_GROUP;
import static com.maxbilbow.aicubo.engine.collision.type.CollisionBody.NO_COLLISIONS;

public class CollisionDetector
{

  private BlockingQueue<CollisionEvent> mActiveCollisions;

  private ThreadLocal<CollisionProcessor> mCollisionProcessor = new ThreadLocal()
  {
    @Override
    protected CollisionProcessor initialValue()
    {
      return new CollisionProcessor();
    }
  };

  public CollisionDetector()
  {
    mActiveCollisions = new LinkedBlockingQueue<>();
  }

  public boolean detectCollision(CollisionBody A, CollisionBody B)
  {
    if (isCollision(A, B))
    {
      final CollisionData data = new CollisionData(A, B);

      mActiveCollisions.offer(new CollisionEvent(data));
      mCollisionProcessor.get().processQueue(mActiveCollisions);
      return true;
    }
    return false;
  }


  private boolean isCollision(CollisionBody A, CollisionBody B)
  {
    if (A == B)//todo should this just compare the bodies?
    {
      return false;
    }
    if (A.getCollisionGroup() != B.getCollisionGroup())
    {
      if (A.getCollisionGroup() != EXCLUSIVE_COLLISION_GROUP &&
          B.getCollisionGroup() != EXCLUSIVE_COLLISION_GROUP)
      {
        return false;
      }
    }

    switch (A.getCollisionGroup())
    {
      case NO_COLLISIONS:
      case EXCLUSIVE_COLLISION_GROUP:
        return false;
    }

    return A.intersects(B);
//    if (isHit)
//    {
//      CollisionEvent e = new CollisionEvent(A.getNode(), B.getNode(), securityKey);
//      if (collisionDelegate != null)
//      {
//        collisionDelegate.doBeforeCollision(A.getNode(), B.getNode(), e);
//      }
//      e.processCollision(securityKey);
//      if (collisionDelegate != null)
//      {
//        collisionDelegate.doAfterCollision(A.getNode(), B.getNode(), e);
//      }
//
//    }
//    return isHit;

  }

  public BlockingQueue<CollisionEvent> getActiveCollisions()
  {
    return mActiveCollisions;
  }
}
