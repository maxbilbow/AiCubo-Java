package com.maxbilbow.aicubo.engine.behaviours;


import com.maxbilbow.aicubo.model.collision.CollisionEvent;

public interface ICollisionHandler
{
  void onCollisionStart(CollisionEvent event);

  void onCollisionEnd(CollisionEvent event);
}
