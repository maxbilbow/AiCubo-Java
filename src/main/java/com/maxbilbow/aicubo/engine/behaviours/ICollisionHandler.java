package com.maxbilbow.aicubo.engine.behaviours;

import com.maxbilbow.aicubo.engine.physics.CollisionEvent;

public interface ICollisionHandler
{
  public void onCollisionStart(CollisionEvent event);

  public void onCollisionEnd(CollisionEvent event);
}
