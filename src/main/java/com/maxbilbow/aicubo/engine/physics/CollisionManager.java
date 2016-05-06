package com.maxbilbow.aicubo.engine.physics;

import com.maxbilbow.aicubo.engine.collision.CollisionDelegate;
import com.maxbilbow.aicubo.engine.collision.CollisionDetector;
import com.maxbilbow.aicubo.engine.collision.type.CollisionBody;
import com.maxbilbow.aicubo.engine.math.Vector3;
import com.maxbilbow.aicubo.model.Node;
import com.maxbilbow.aicubo.model.RootNode;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by Max on 06/05/2016.
 */
@Component
public class CollisionManager
{
  private CollisionDetector mCollisionDetector = new CollisionDetector();

  private CollisionDelegate collisionDelegate;
  public CollisionDelegate getCollisionDelegate()
  {
    return collisionDelegate;
  }

  public void setCollisionDelegate(CollisionDelegate collisionDelegate)
  {
    this.collisionDelegate = collisionDelegate;
  }

  class CollisionBodies {
    Collection<CollisionBody> staticBodies    = new LinkedList<>();
    Collection<CollisionBody> dynamicBodies   = new LinkedList<>();
    Collection<CollisionBody> kinematicBodies = new LinkedList<>();
  }

  private Map<RootNode,CollisionBodies> mBodies = new HashMap<>();


  void buildCollisionList(final CollisionBodies aBodies, final Collection<Node> aNodes)
  {

    aBodies.staticBodies.clear();
    aBodies.dynamicBodies.clear();
    aBodies.kinematicBodies.clear();
    aNodes.forEach(node -> {
      if (node.collisionBody() != null)
      {
        switch (node.physicsBody().getType())
        {
          case Dynamic:
            aBodies.dynamicBodies.add(node.collisionBody());
            break;
          case Static:
            aBodies.staticBodies.add(node.collisionBody());
            break;
          case Kinematic:
            aBodies.kinematicBodies.add(node.collisionBody());
            break;
          default:
            break;
        }
      }
    });

  }

  public void updateCollisionEvents(final RootNode rootNode)
  {

    if (!mBodies.containsKey(rootNode))
    {
      mBodies.put(rootNode,new CollisionBodies());
    }
    final CollisionBodies bodies;
    this.buildCollisionList(bodies = mBodies.get(rootNode),rootNode.getChildren());


    if (!bodies.dynamicBodies.isEmpty())
    {
      if (!bodies.staticBodies.isEmpty())
      {
        checkForStaticCollisions(bodies.dynamicBodies, bodies.staticBodies);
      }
      checkForDynamicCollisions(bodies.dynamicBodies);
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
        if (mCollisionDetector.detectCollision(staticBody, dynamicBody))
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
      if (mCollisionDetector.detectCollision(A, B))
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
}
