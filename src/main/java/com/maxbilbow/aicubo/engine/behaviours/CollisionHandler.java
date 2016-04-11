package com.maxbilbow.aicubo.engine.behaviours;


import com.maxbilbow.aicubo.model.collision.CollisionEvent;

public abstract class CollisionHandler extends Behaviour implements ICollisionHandler
{

  public abstract void onCollision(final CollisionEvent e);

  public void update()
  {
  }

  @Override
  public void broadcastMessage(String message, Object args)
  {
    if (message == "onCollision")
    {
      this.onCollision((CollisionEvent) args);
    }
    else
    {
      super.broadcastMessage(message, args);
    }
  }
}
