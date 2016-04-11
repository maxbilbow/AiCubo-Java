package com.maxbilbow.aicubo.engine.collision;

import com.maxbilbow.aicubo.model.Node;
import com.maxbilbow.aicubo.model.collision.CollisionEvent;

public interface CollisionDelegate
{
  void doAfterCollision(Node nodeA, Node nodeB, CollisionEvent eventData);

  void doBeforeCollision(Node nodeA, Node nodeB, CollisionEvent eventData);
}
