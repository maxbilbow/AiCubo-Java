package com.maxbilbow.aicubo.engine.collision.type;

import com.maxbilbow.aicubo.engine.math.Vector3;
import com.maxbilbow.aicubo.model.Node;

/**
 * Created by Max on 11/04/2016.
 */
public class CollisionData
{
//  private Vector3 mPointOfImpact;
//  private Vector3 mDirectionOfImpact;
//  private Float   mForceOfImpact;

  private final Vector3 mVelocityOfBodyA, mVelocityOfBodyB;

  private Vector3 mVectorFromAToB;

  private final CollisionBody mBodyA, mBodyB;
  private final  float  mDistanceBetweenCenters;
  private Float mDistanceBetweenPlanes;

  public CollisionData(CollisionBody aBodyA, CollisionBody aBodyB)
  {
    mBodyA = aBodyA;
    mBodyB = aBodyB;

    mVelocityOfBodyA = aBodyA.getVelocity();
    mVelocityOfBodyB = aBodyB.getVelocity();
    mDistanceBetweenCenters = Vector3.makeSubtraction(
            aBodyA.getCenterOfGravity(),
            aBodyB.getCenterOfGravity()
    ).length();

  }

//  public void setDistanceFromCenters(float aDistanceFromCenters)
//  {
//    mDistanceFromCenters = aDistanceFromCenters;
//  }

  public Vector3 getVectorFromAToB()
  {
    return mVectorFromAToB;
  }

  public void setVectorFromAToB(Vector3 aVectorFromAToB)
  {
    mVectorFromAToB = aVectorFromAToB;
  }

//  public Float getDistanceFromCenters()
//  {
//    return mDistanceFromCenters;
//  }

  public Node getRootNodeA()
  {
    return mBodyA.getNode().getControlNode();
  }

  public Node getRootNodeB()
  {
    return mBodyB.getNode().getControlNode();
  }

  public Vector3 getVelocityOfBodyA()
  {
    return mVelocityOfBodyA;
  }

  public Vector3 getVelocityOfBodyB()
  {
    return mVelocityOfBodyB;
  }

  public Float getDistanceBetweenPlanes()
  {
    return mDistanceBetweenPlanes;
  }

  public float getDistanceBetweenCenters()
  {
    return mDistanceBetweenCenters;
  }

  public void setDistanceBetweenPlanes(Float aDistanceBetweenPlanes)
  {
    mDistanceBetweenPlanes = aDistanceBetweenPlanes;
  }
}
