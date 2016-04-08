package com.maxbilbow.aicubo.engine;

import com.maxbilbow.aicubo.core.IRMXObject;
import com.maxbilbow.aicubo.engine.behaviours.IBehaviour;
import com.maxbilbow.aicubo.engine.geometry.Geometry;
import com.maxbilbow.aicubo.engine.geometry.Shape;
import com.maxbilbow.aicubo.engine.math.Matrix4;
import com.maxbilbow.aicubo.engine.physics.CollisionBody;
import com.maxbilbow.aicubo.engine.physics.PhysicsBody;

import java.util.List;
import java.util.Set;

public interface Node extends IRMXObject
{

  void setComponent(Class<?> type, NodeComponent component);

  void addBehaviour(IBehaviour behaviour);

  NodeComponent getComponent(Class<?> type);

  List<Node> getChildren();

  void addChild(Node child);

  boolean removeChildNode(Node node);

  Node getChildWithName(String name);

  default Camera camera()
  {
    return (Camera) this.getComponent(Camera.class);
  }

  default void setCamera(Camera camera)
  {
    this.setComponent(Camera.class, camera);
  }


  default Geometry geometry()
  {
    return (Geometry) this.getComponent(Geometry.class);
  }

  default void setGeometry(Shape shape)
  {
    this.setComponent(Geometry.class, shape.newGeometry());
  }


  default PhysicsBody physicsBody()
  {
    return (PhysicsBody) this.getComponent(PhysicsBody.class);
  }

  default void setPhysicsBody(PhysicsBody body)
  {
    this.setComponent(PhysicsBody.class, body);
  }

  default CollisionBody collisionBody()
  {
    PhysicsBody body = this.physicsBody();
    if (body != null)
    {
      return body.getCollisionBody();
    }
    else
    {
      return null;
    }
  }


  default LightSource getLight()
  {
    return (LightSource) this.getComponent(LightSource.class);
  }

  default void setLightSource(LightSource light)
  {
    this.setComponent(LightSource.class, light);
  }

  void updateLogic(long time);

  void updateAfterPhysics(long time);

  void draw(Matrix4 viewMatrix);

  Node getParent();

  void setParent(Node parent);

  default void shine()
  {
    LightSource light = this.getLight();
    if (light != null)
    {
      light.shine();
    }
  }

  /**
   * Sends a message to all behaviours and all children of this node.
   */
  @Override
  void broadcastMessage(String message);

  /**
   * Sends a message to all behaviours and all children of this node.
   */
  @Override
  void broadcastMessage(String message, Object args);

  boolean sendMessageToBehaviour(Class<?> theBehaviour, String message);

  boolean sendMessageToBehaviour(Class<?> theBehaviour, String message, Object args);

  void addToCurrentScene();

  Transform transform();

  default void addGeometryToList(Set<Geometry> geometries)
  {
    if (this.geometry() != null && this.geometry().isVisible())
    {
      geometries.add(this.geometry());
    }
    this.getChildren().stream().forEach(child -> child.addGeometryToList(geometries));
  }
}