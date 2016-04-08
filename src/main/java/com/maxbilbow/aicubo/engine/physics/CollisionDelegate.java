package com.maxbilbow.aicubo.engine.physics;

import com.maxbilbow.aicubo.engine.Node;

public interface CollisionDelegate
{
  public void doAfterCollision(Node nodeA, Node nodeB, CollisionEvent eventData);

  public void doBeforeCollision(Node nodeA, Node nodeB, CollisionEvent eventData);
}
