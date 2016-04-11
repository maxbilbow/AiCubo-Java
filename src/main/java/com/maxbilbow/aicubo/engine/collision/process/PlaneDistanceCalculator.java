package com.maxbilbow.aicubo.engine.collision.process;

import com.maxbilbow.aicubo.engine.collision.type.BoundingBox;
import com.maxbilbow.aicubo.engine.math.Vector3;
import com.maxbilbow.aicubo.model.Node;
import com.maxbilbow.aicubo.model.collision.CollisionEvent;

/**
 * Created by Max on 11/04/2016.
 */
public class PlaneDistanceCalculator
{
  public void calculatePlaneDistance(CollisionEvent aCollisionEvent)
  {
    final Node nodeA = aCollisionEvent.getNodeA();
    final Node nodeB = aCollisionEvent.getNodeB();

    BoundingBox boxA = nodeA.collisionBody().boundingBox;
    BoundingBox boxB = nodeB.collisionBody().boundingBox;
    Vector3 posA = nodeA.transform().localPosition();
    Vector3 posB = nodeB.transform().localPosition();


    float dx = boxA.xMax() + posA.x - boxB.xMin() - posB.x; //left
    float dx2 = boxB.xMax() + posB.x - boxA.xMin() - posA.x;//right
    float dy = boxA.yMax() + posA.y - boxB.yMin() - posB.y; //top
    float dy2 = boxB.yMax() + posB.y - boxA.yMin() - posA.y;//bottom
    float dz = boxA.zMax() + posA.z - boxB.zMin() - posB.z; //front
    float dz2 = boxB.zMax() + posB.z - boxA.zMin() - posA.z;//back
    String axis = "x";
    float diff = dx;
    float sign = 1;

    //		System.out.println(" dx: " + dx + ",  dy: " + dy + ",  dz: " + dz);
    //		System.out.println("dx2: " + dx2 + ", dy2: " + dy2 + ", dz2: " + dz2);
    //		System.out.println("dx2: " + dx2 + ", dy2: " + dy2 + ", dz2: " + dz2);

    if (dx2 < diff)
    {
      diff = dx2;
      axis = "x";
      sign = -1;
    }
    if (dy < diff)
    {
      diff = dy;
      axis = "y";
      sign = 1;
    }
    if (dy2 < diff)
    {
      diff = dy2;
      axis = "y";
      sign = -1;
    }
    if (dz < diff)
    {
      diff = dz;
      axis = "z";
      sign = 1;
    }
    if (dz2 < diff)
    {
      diff = dz2;
      axis = "z";
      sign = -1;
    }
    aCollisionEvent.setAxis(axis);
    aCollisionEvent.getData().setDistanceBetweenPlanes(diff * sign);
  }

}
