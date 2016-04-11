package com.maxbilbow.aicubo.engine.collision.process;

import com.maxbilbow.aicubo.engine.math.Vector3;
import com.maxbilbow.aicubo.model.Transform;
import com.maxbilbow.aicubo.model.collision.CollisionEvent;

/**
 * Created by Max on 11/04/2016.
 */
public class MomentumCalculator
{
  public void processMomentum(CollisionEvent aCollisionEvent)
  {

    final Transform A = aCollisionEvent.getTransformA();
    final Transform B = aCollisionEvent.getTransformB();
//    log += "\n\nCollision Momentum Report: " + nodeA.uniqueName() + " vs. " + nodeB.uniqueName();

    Vector3 Va = A.node.physicsBody().getVelocity();
    Vector3 Vb = B.node.physicsBody().getVelocity();
    Vector3 direction = Vector3.makeSubtraction(Va, Vb);
    if (direction.isZero())
    {
      return;
    }
    else
    {
      direction.normalize();
    }

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


    aCollisionEvent.getNodeA().physicsBody().applyForce(forceOnA * lossA, direction, aCollisionEvent.getHitPointA());
    aCollisionEvent.getNodeB().physicsBody().applyForce(forceOnB * lossB, direction, aCollisionEvent.getHitPointB());

    //Loss of energy
    //				nodeA.physicsBody().applyForce(-m1 * lossA, direction, hitPointA);
    //				nodeA.physicsBody().applyForce(-m2 * lossB, direction, hitPointA);


    //		System.out.println(log);
    //System.out.println("Velocities: " +
    //				"\n     "+ nodeA.uniqueName() + ": " + va + nodeB.uniqueName() + ": " + vb);
  }
}
