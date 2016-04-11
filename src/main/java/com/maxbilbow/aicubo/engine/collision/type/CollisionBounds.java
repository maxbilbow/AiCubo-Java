package com.maxbilbow.aicubo.engine.collision.type;

import com.maxbilbow.aicubo.model.Node;
import com.maxbilbow.aicubo.model.Transform;

public abstract class CollisionBounds
{

  public final Transform transform;

  public CollisionBounds(Node node)
  {
    this.transform = node.transform();
  }

  /**
   * Checks if this AABB intersects another AABB.
   *
   * @param other The other AABB
   * @return true if a collision was detected.
   */
  public abstract boolean intersects(CollisionBounds other);

}