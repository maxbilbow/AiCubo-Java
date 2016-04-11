package com.maxbilbow.aicubo.engine.physics.type;

import com.maxbilbow.aicubo.engine.math.Vector3;

/**
 * Created by Max on 11/04/2016.
 */
public class PhysicsWorldConstants
{
  private Vector3 mGravity  = new Vector3();

  private float mFriction = 0;
  public static PhysicsWorldConstants defaultConstants()
  {
    PhysicsWorldConstants constants = new PhysicsWorldConstants();
    constants.setFriction(0.3f);
    constants.setGravity(0f,-9.8f,0f);
    return constants;
  }

  public void setGravity(float x, float y, float z)
  {
    mGravity.set(x,y,z);
  }

  public Vector3 getGravity()
  {
    return mGravity;
  }

  public void setGravity(Vector3 aGravity)
  {
    mGravity = aGravity;
  }

  public float getFriction()
  {
    return mFriction;
  }

  public void setFriction(float aFriction)
  {
    mFriction = aFriction;
  }
}
