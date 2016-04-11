package com.maxbilbow.aicubo.engine.collision.process;

import com.maxbilbow.aicubo.config.RMX;
import com.maxbilbow.aicubo.engine.math.Vector3;
import com.maxbilbow.aicubo.engine.physics.PhysicsBodyType;
import com.maxbilbow.aicubo.model.Scene;
import com.maxbilbow.aicubo.model.collision.CollisionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Max on 11/04/2016.
 */
public class BodySeparator
{
  private Logger mLogger = LoggerFactory.getLogger(BodySeparator.class);

  public void separateBodies(CollisionEvent aCollisionEvent)
  {
    //apply force relative to overlapping areas
    final Vector3 aToB = aCollisionEvent.getData().getVectorFromAToB();
    float min;
    float dist = aCollisionEvent.getData().getDistanceBetweenCenters();
    //		if (PhysicsWorld.UseBoundingBox)
    min = aCollisionEvent.getCollisionBodyA().boundingSphere.radius() + aCollisionEvent
            .getCollisionBodyB().boundingSphere.radius();
    //		else
    //			min = nodeA.transform.radius() + nodeB.transform.radius();
//    log += "\n       A to B: " + AtoB;

    final float delta = min - dist / 2;
//    log += "\n     Distance: " + dist + " < " + min + ", delta == " + delta;
    if (dist > min || dist < 0)
    {
      mLogger.warn("Distance" + dist + " should be less than min: " + min + "\n");//+log
      //			throw new IllegalArgumentException("Distance" + dist + " should be less than min: " + min + "\n" + log);
    }
    //			System.err.println("Distance" + dist + " should be less than min: " + min);

    aToB.normalize();
    aToB.scale(-delta);
    if (aToB.isZero())
    {
      aToB.set(-min, 0, 0);
    }

    float diff = aCollisionEvent.getDistanceBetweenPlanes();
    final String axis = aCollisionEvent.getAxis();
    if (Scene.getCurrent().tick() > 0)
    {
      if (aCollisionEvent.getPhysicsBodyTypeA() == PhysicsBodyType.Dynamic && !aCollisionEvent.getVelocityA().isZero())
      {
        aCollisionEvent.getTransformA().rootTransform().stepBack(axis);// -diff * sign);
      }
      else if (aCollisionEvent.getPhysicsBodyTypeB() == PhysicsBodyType.Dynamic && !aCollisionEvent.getVelocityB()
              .isZero())
      {
        aCollisionEvent.getTransformB().stepBack(axis);//diff * sign);
      }
    }

    if (aCollisionEvent.getCollisionBodyA().boundingBox
            .intersects(aCollisionEvent.getCollisionBodyB().boundingBox))
    {

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

      final float massA = aCollisionEvent.getMassA();
      final float massB = aCollisionEvent.getMassB();
      aCollisionEvent.getPhysicsBodyA()
              .applyForce(escapeForce * (massA + diff), dir, aCollisionEvent.getHitPointA());//.translate(AtoB);

      aCollisionEvent.getPhysicsBodyB()
              .applyForce(-escapeForce * (massB + diff), dir, aCollisionEvent.getHitPointB());//translate(AtoB);

    }
  }
}
