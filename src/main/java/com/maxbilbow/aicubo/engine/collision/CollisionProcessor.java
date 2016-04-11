package com.maxbilbow.aicubo.engine.collision;

import com.maxbilbow.aicubo.engine.collision.process.BodySeparator;
import com.maxbilbow.aicubo.engine.collision.process.MomentumCalculator;
import com.maxbilbow.aicubo.engine.collision.process.PlaneDistanceCalculator;
import com.maxbilbow.aicubo.engine.collision.type.CollisionResult;
import com.maxbilbow.aicubo.engine.math.Vector3;
import com.maxbilbow.aicubo.model.Node;
import com.maxbilbow.aicubo.model.collision.CollisionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

import static com.maxbilbow.aicubo.engine.collision.type.CollisionResult.Cancelled;

/**
 * Created by Max on 11/04/2016.
 */
public class CollisionProcessor
{

  private Logger mLogger = LoggerFactory.getLogger(CollisionProcessor.class);

  private MomentumCalculator mMomentumCalculator = new MomentumCalculator();

  private PlaneDistanceCalculator mPlaneDistanceCalculator = new PlaneDistanceCalculator();

  private boolean mActive;

  private BodySeparator mBodySeparator = new BodySeparator();

  public void processQueue(BlockingQueue<CollisionEvent> aCollisionEvents)
  {
    if (isActive())
    {
      return;
    }
    mActive = true;

    new Thread(() -> {
      while (aCollisionEvents.size() > 0)
      {
        try
        {
          final CollisionEvent event = aCollisionEvents.take();
          switch (processCollision(event))
          {
            case Ended:
            case Cancelled:
              event.onCollisionEnd();
              break; //Remove completed collisions
            default:
              aCollisionEvents.offer(event);
              break;
          }
        }
        catch (InterruptedException aE)
        {
          mLogger.error("Error taking/putting collision event", aE);
        }
      }
      mActive = false;
    }).start();

  }

  private CollisionResult processCollision(CollisionEvent aCollisionEvent)
  {
    if (aCollisionEvent.isPrevented())
    {
      return Cancelled;
    }

    final Node nodeA = aCollisionEvent.getNodeA();
    final Node nodeB = aCollisionEvent.getNodeB();


    final Vector3 vectorFromAToB = nodeA.transform().position().getVectorTo(nodeB.transform().position());
    aCollisionEvent.getData().setVectorFromAToB(vectorFromAToB);
    mPlaneDistanceCalculator.calculatePlaneDistance(aCollisionEvent);
    mBodySeparator.separateBodies(aCollisionEvent);
    mMomentumCalculator.processMomentum(aCollisionEvent);
    return CollisionResult.Ended;
  }

  public boolean isActive()
  {
    return mActive;
  }

}
