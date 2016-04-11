package com.maxbilbow.aicubo.engine.physics;


import com.maxbilbow.aicubo.engine.collision.CollisionDelegate;
import com.maxbilbow.aicubo.engine.collision.CollisionManager;
import com.maxbilbow.aicubo.engine.collision.type.CollisionBody;
import com.maxbilbow.aicubo.engine.math.Vector3;
import com.maxbilbow.aicubo.engine.physics.type.PhysicsWorldConstants;
import com.maxbilbow.aicubo.model.Node;
import com.maxbilbow.aicubo.model.core.RMXObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.stream.Stream;

import static com.maxbilbow.aicubo.config.RMX.rmxGetCurrentFramerate;

@Component
public class PhysicsWorld extends RMXObject
{

  @Resource
  private CollisionManager mCollisionManager;

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

  public void updatePhysics(Node rootNode)
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
    else if (node.getParent().getParent() == null)
    {
      node.transform().localMatrix().m31 = ground;
    }

  }

  private Collection<CollisionBody> staticBodies    = new LinkedList<>();
  private Collection<CollisionBody> dynamicBodies   = new LinkedList<>();
  private Collection<CollisionBody> kinematicBodies = new LinkedList<>();

  void buildCollisionList(Collection<Node> collection)
  {
    this.staticBodies.clear();
    this.dynamicBodies.clear();
    this.kinematicBodies.clear();
    collection.forEach(node -> {
      if (node.collisionBody() != null)
      {
        switch (node.physicsBody().getType())
        {
          case Dynamic:
            this.dynamicBodies.add(node.collisionBody());
            break;
          case Static:
            this.staticBodies.add(node.collisionBody());
            break;
          case Kinematic:
            this.kinematicBodies.add(node.collisionBody());
            break;
          default:
            break;
        }
      }
    });

  }

  public void updateCollisionEvents(Node rootNode)
  {


    this.buildCollisionList(rootNode.getChildren());


    if (!dynamicBodies.isEmpty())
    {
		if (!staticBodies.isEmpty())
		{
			checkForStaticCollisions(dynamicBodies, staticBodies);
		}
      checkForDynamicCollisions(dynamicBodies);
      count = checks = 0;
      //			swapLists();
    }

  }


  public void checkForStaticCollisions(Collection<CollisionBody> dynamic, Collection<CollisionBody> staticBodies)
  {
    Iterator<CollisionBody> si = staticBodies.iterator();
    while (si.hasNext())
    {
      CollisionBody staticBody = si.next();
      Iterator<CollisionBody> di = dynamic.iterator();
      while (di.hasNext())
      {
        CollisionBody dynamicBody = di.next();
        checks++;
        if (mCollisionManager.detectCollision(staticBody, dynamicBody))
        {
          count++;
        }
      }
    }
  }

  int count = 0;
  int checks = 0;

  public void checkForDynamicCollisions(Collection<CollisionBody> dynamic)
  {
    CollisionBody A = ((LinkedList<CollisionBody>) dynamic).removeFirst();
    Iterator<CollisionBody> i = dynamic.iterator();
    while (i.hasNext())
    {
      CollisionBody B = i.next();
      checks++;
      if (mCollisionManager.detectCollision(A, B))
      {
        count++;
        //					if (unchecked.remove(A))
        //						System.err.println(A.uniqueName() + " removed twie");//checked.addLast(A);
		  if (!dynamic.remove(B))
		  {
			  System.err.println(B.uniqueName() + " was not removed from unchecked");//checked.addLast(B);
		  }
        if (!dynamic.isEmpty())
        {
          this.checkForDynamicCollisions(dynamic);
          return;
        }
      }
    }
    if (!dynamic.isEmpty())
    {
      this.checkForDynamicCollisions(dynamic);
    }
  }

  Vector3 collisionDistance = new Vector3();
  public static boolean UseBoundingBox = true;

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
  private CollisionDelegate collisionDelegate;


  public float getFriction()
  {
    return mConstants.getFriction();
  }

  public void setFriction(float aFriction)
  {
    mConstants.setFriction(aFriction);
  }

  public CollisionDelegate getCollisionDelegate()
  {
    return collisionDelegate;
  }

  public void setCollisionDelegate(CollisionDelegate collisionDelegate)
  {
    this.collisionDelegate = collisionDelegate;
  }


}
