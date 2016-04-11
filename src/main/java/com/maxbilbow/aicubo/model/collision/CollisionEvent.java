package com.maxbilbow.aicubo.model.collision;


import com.maxbilbow.aicubo.engine.collision.type.CollisionBody;
import com.maxbilbow.aicubo.engine.collision.type.CollisionData;
import com.maxbilbow.aicubo.engine.math.Vector3;
import com.maxbilbow.aicubo.engine.physics.PhysicsBody;
import com.maxbilbow.aicubo.engine.physics.PhysicsBodyType;
import com.maxbilbow.aicubo.model.Node;
import com.maxbilbow.aicubo.model.Transform;

public class CollisionEvent
{
  private CollisionData mCollisionData;
  public final Node mNodeA, mNodeB;
  public final Vector3 mHitPointA = Vector3.Zero;
  public final Vector3 mHitPointB = Vector3.Zero;//// TODO: 11/04/2016
//  public Float   mDistanceBetweenCenters;
//  public Float   mDistanceBetweenPlanes;
  private boolean mPrevented = false;

  private Integer    key;
  private String mAxis;

  public CollisionEvent(CollisionData aCollisionData)
  {
    mNodeA = aCollisionData.getRootNodeA();
    mNodeB = aCollisionData.getRootNodeB();
    onCollisionStart();
    mCollisionData = aCollisionData;
  }

  private void onCollisionStart()
  {
    mNodeA.broadcastMessage("onCollisionStart", this);
    mNodeB.broadcastMessage("onCollisionStart", this);
  }

  public boolean isPrevented()
  {
    return mPrevented;
  }

  public void preventCollision(boolean aPrevented)
  {
    this.mPrevented = aPrevented;
  }

  public Node getNodeA()
  {
    return mNodeA;
  }

  public Node getNodeB()
  {
    return mNodeB;
  }


  public void setAxis(String aAxis)
  {
    mAxis = aAxis;
  }

  public Transform getTransformA()
  {
    return mNodeA.transform();
  }

  public Transform getTransformB()
  {
    return mNodeB.transform();
  }

  public Vector3 getHitPointA()
  {
    return mHitPointA;
  }

  public Vector3 getHitPointB()
  {
    return mHitPointB;
  }

  public void onCollisionEnd()
  {
    mNodeA.broadcastMessage("onCollisionEnd",this);
  }

  public CollisionData getData()
  {
    return mCollisionData;
  }

  public CollisionBody getCollisionBodyA()
  {
    return mNodeA.collisionBody();
  }

  public CollisionBody getCollisionBodyB()
  {
    return mNodeB.collisionBody();
  }

  public Float getDistanceBetweenPlanes()
  {
    return mCollisionData.getDistanceBetweenPlanes();
  }

  public String getAxis()
  {
    return mAxis;
  }

  public PhysicsBodyType getPhysicsBodyTypeA()
  {
    return mNodeA.physicsBody().getType();
  }

  public PhysicsBodyType getPhysicsBodyTypeB()
  {
    return mNodeB.physicsBody().getType();
  }

  public Vector3 getVelocityA()
  {
    return mNodeA.physicsBody().getVelocity();
  }

  public Vector3 getVelocityB()
  {
    return mNodeB.physicsBody().getVelocity();
  }

  public PhysicsBody getPhysicsBodyA()
  {
    return mNodeA.physicsBody();
  }

  public PhysicsBody getPhysicsBodyB()
  {
    return mNodeB.physicsBody();
  }

  public float getMassA()
  {
    return getPhysicsBodyA().getMass();
  }

  public float getMassB()
  {
    return getPhysicsBodyB().getMass();
  }

  public Node getOther(Node aNode)
  {
    return aNode == mNodeA ? mNodeB : mNodeA;
  }
}
