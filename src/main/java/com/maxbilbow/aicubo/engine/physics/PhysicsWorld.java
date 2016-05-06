package com.maxbilbow.aicubo.engine.physics;


import com.maxbilbow.aicubo.engine.math.Vector3;
import com.maxbilbow.aicubo.engine.physics.type.PhysicsWorldConstants;
import com.maxbilbow.aicubo.model.Node;
import com.maxbilbow.aicubo.model.RootNode;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

import static com.maxbilbow.aicubo.config.RMX.rmxGetCurrentFramerate;

@Component
public class PhysicsWorld //extends RMXObject
{

  private CollisionManager mCollisionManager = new CollisionManager();

  private PhysicsWorldConstants mConstants = PhysicsWorldConstants.defaultConstants();



  public Vector3 getGravity()
  {
	  return mConstants.getGravity();
  }

  public void setGravity(Vector3 gravity)
  {
    this.mConstants.getGravity().set(gravity);
  }

  public void setGravity(float x, float y, float z)
  {
    mConstants.setGravity(x,y,z);
  }

  public void updatePhysics(RootNode rootNode)
  {
    Stream<Node> children = rootNode.getChildren().stream();

    children.forEach(node -> {
      if (node.physicsBody() != null)
      {
        this.applyGravityToNode(node);
        node.physicsBody().updatePhysics(this);
      }
    });
//		children.close();

  }


  private void applyGravityToNode(Node node)
  {
	  if (this.getGravity().isZero() || !node.physicsBody().isEffectedByGravity())
	  {
		  return;
	  }
    float ground = node.transform().getHeight() / 2;//.scale().y / 2;
    float mass = node.transform().mass();
    float framerate = rmxGetCurrentFramerate();
    float height = node.transform().worldMatrix().m31;
    if (height > ground)
    {
      //			System.out.println(node.getName() + " >> BEFORE: " + m.position());
      node.physicsBody().applyForce(framerate * mass, this.getGravity(), Vector3.Zero);
      //			node.forces.x += g.x * framerate * mass;
      //			this.forces.y += g.y * framerate * mass;
      //			this.forces.z += g.z * framerate * mass;
    }
    else if (node.getParent() == null)
    {
      node.transform().localMatrix().m31 = ground;
    }

  }



//  private boolean checkForCollision(CollisionBody A, CollisionBody B)
//  {
//	  if (A == B)
//	  {
//		  return false;
//	  }
//	  if (A.getCollisionGroup() != B.getCollisionGroup())
//	  {
//		  if (A.getCollisionGroup() != EXCLUSIVE_COLLISION_GROUP &&
//			  B.getCollisionGroup() != EXCLUSIVE_COLLISION_GROUP)
//		  {
//			  return false;
//		  }
//	  }
//
//    switch (A.getCollisionGroup())
//    {
//      case NO_COLLISIONS:
//      case EXCLUSIVE_COLLISION_GROUP:
//        return false;
//    }
//
//    boolean isHit = A.intersects(B);
//    if (isHit)
//    {
//      CollisionEvent e = new CollisionEvent(A.getNode(), B.getNode(), securityKey);
//		if (collisionDelegate != null)
//		{
//			collisionDelegate.doBeforeCollision(A.getNode(), B.getNode(), e);
//		}
//      e.processCollision(securityKey);
//		if (collisionDelegate != null)
//		{
//			collisionDelegate.doAfterCollision(A.getNode(), B.getNode(), e);
//		}
//
//    }
//    return isHit;
//
//  }

  private static final int securityKey = (int) (Math.random() * 100);



  public float getFriction()
  {
    return mConstants.getFriction();
  }

  public void setFriction(float aFriction)
  {
    mConstants.setFriction(aFriction);
  }



}
