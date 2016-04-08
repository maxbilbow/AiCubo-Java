package com.maxbilbow.aicubo.engine.physics;

import com.maxbilbow.aicubo.engine.Node;
import com.maxbilbow.aicubo.engine.math.Vector3;

public class BoundingSphere extends CollisionBounds
{

  public BoundingSphere(Node node)
  {
    super(node);
    // TODO Auto-generated constructor stub
  }

  public float radius()
  {
    return this.transform.scale().length();
//		new Vector3(
//				this.transform.collisionBody().boundingBox.xMax(),
//				this.transform.collisionBody().boundingBox.yMax(),
//				this.transform.collisionBody().boundingBox.zMax()
//				).length();
  }

  @Override
  public boolean intersects(CollisionBounds other)
  {
    BoundingSphere B = (BoundingSphere) other;
    float min = this.radius() +
                B.radius();
    Vector3 collisionDistance = new Vector3();
    collisionDistance.set(
            this.transform.position());
    collisionDistance.sub(
            B.transform.position());
    float distance = collisionDistance.length();

    return distance < min;
  }

}
