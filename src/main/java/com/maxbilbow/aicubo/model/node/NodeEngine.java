package com.maxbilbow.aicubo.model;


import com.maxbilbow.aicubo.engine.geometry.Geometry;
import com.maxbilbow.aicubo.model.core.RMXObject;
import com.maxbilbow.aicubo.engine.behaviours.IBehaviour;
import com.maxbilbow.aicubo.engine.math.Matrix4;

import java.util.*;
import java.util.stream.Stream;


public class NodeEngine extends RMXObject implements Node
{


  private       Hierarchy<Node>         parent;


  private final HashMap<Class<?>, NodeComponent> components = new HashMap<>();
  private final Set<IBehaviour>                  behaviours = new HashSet<>();
  private Scene mScene;

  /* (non-Javadoc)
   * @see Node#setComponent(java.lang.Class, NodeComponent)
   */
  @Override
  public void setComponent(Class<?> type, NodeComponent component)
  {
	  this.components.put(type, component);
    component.setNode(this);
  }

  /* (non-Javadoc)
   * @see Node#addBehaviour(IBehaviour)
   */
  @Override
  public void addBehaviour(IBehaviour behaviour)
  {
    if (behaviour != null)
    {
      for (IBehaviour b : this.behaviours)
      {
        if (!b.getName().isEmpty() && b.getName() == behaviour.getName())
        {
          System.err.println("Behaviour: " + b.getName() + " was already added.");
          return;
        }
      }
      this.behaviours.add(behaviour);
//			behaviour.broadcastMessage("setNode",this);
      behaviour.setNode(this);
    }
  }

  /* (non-Javadoc)
   * @see Node#getComponent(java.lang.Class)
   */
  @Override
  public NodeComponent getComponent(Class<?> type)
  {
    return components.getOrDefault(type, null);
  }

  private final Transform transform;

  private ArrayList<Node> mChildren = new ArrayList<Node>();

  /* (non-Javadoc)
   * @see Node#getChildren()
   */
  @Override
  public List<Node> getChildren()
  {
    return this.mChildren;
  }

  /* (non-Javadoc)
   * @see Node#addChild(Node)
   */
  @Override
  public void addChild(Node child)
  {
    if (!this.mChildren.contains(child))
    {
      this.mChildren.add(child);
      child.setParent(this);
      if (mScene != null && child.getScene() != mScene)
      {
        child.setScene(mScene);
      }
    }
  }

  /* (non-Javadoc)
   * @see Node#removeChildNode(Node)
   */
  @Override
  public boolean removeChild(Node node)
  {
    return this.mChildren.remove(node);
  }

  /* (non-Javadoc)
   * @see Node#getChildWithName(java.lang.String)
   */
  @Override
  public Node getChildWithName(String name)
  {
    for (Node child : this.mChildren)
    {
		if (child.getName() == name)
		{
			return child;
		}
    }
    return null;
  }

  public NodeEngine()
  {
    transform = new Transform(this);
  }



  /* (non-Javadoc)
   * @see Node#updateLogic(long)
   */
  @Override
  public void updateLogic()
  {

    Stream<IBehaviour> behaviours = this.behaviours.stream();
    behaviours.forEach(behaviour -> {
		if (behaviour.isEnabled())
		{
			behaviour.update(this);
		}
    });
//		behaviours.close();		

    Stream<Node> children = this.mChildren.stream();
    children.forEach(child -> {
      child.updateLogic();
    });


  }

  private long _timeStamp = -1;

  /* (non-Javadoc)
   * @see Node#updateAfterPhysics(long)
   */
  @Override
  public void updateAfterPhysics()
  {
    this.behaviours.stream().forEach(behaviour -> {
		if (behaviour.hasLateUpdate())
		{
			behaviour.broadcastMessage("lateUpdate");
		}
    });

    this.mChildren.forEach(Node::updateAfterPhysics);
    this.transform.updateLastPosition();
//		this.updateTick(time);
    ///.set(arg0);
  }

  /* (non-Javadoc)
   * @see Node#draw(Matrix4)
   */
  @Override
  public void draw(Matrix4 viewMatrix)
  {
	  if (this.geometry() != null)
	  {
		  this.geometry().render();//, modelMatrix);
	  }
	  for (Node child : this.mChildren)
	  {
		  child.draw(viewMatrix);
	  }
  }


  /* (non-Javadoc)
   * @see Node#getParent()
   */
  @Override
  public Node getParent()
  {
    if (parent instanceof Node)
    {
      return (Node) parent;
    }
    return null;
  }

  /* (non-Javadoc)
   * @see Node#setParent(Node)
   */
  @Override
  public void setParent(Hierarchy parent)
  {
    if (this.parent != null && parent != this.parent)
    {
      this.parent.removeChild(this);
      ;
    }
    this.parent = parent;
  }

  /* (non-Javadoc)
   * @see Node#broadcastMessage(java.lang.String)
   */
  @Override
  public void broadcastMessage(String message)
  {
    super.broadcastMessage(message);
//		for (NodeComponent c : this.components.values()) {
//			c.broadcastMessage(message);
//		}
    for (IBehaviour b : this.behaviours)
    {
      b.broadcastMessage(message);
    }
    for (Node child : this.mChildren)
    {
      child.broadcastMessage(message);
    }
  }

  /* (non-Javadoc)
   * @see Node#broadcastMessage(java.lang.String, java.lang.Object)
   */
  @Override
  public void broadcastMessage(String message, Object args)
  {
    super.broadcastMessage(message, args);
    for (NodeComponent c : this.components.values())
    {
      c.broadcastMessage(message, args);
    }
    for (IBehaviour b : this.behaviours)
    {
      b.broadcastMessage(message, args);
    }
    for (Node child : this.mChildren)
    {
      child.broadcastMessage(message, args);
    }
  }

  /* (non-Javadoc)
   * @see Node#sendMessageToBehaviour(java.lang.Class, java.lang.String)
   */
  @Override
  public boolean sendMessageToBehaviour(Class<?> theBehaviour, String message)
  {
    return this.sendMessageToBehaviour(theBehaviour, message, null);
  }

  /* (non-Javadoc)
   * @see Node#sendMessageToBehaviour(java.lang.Class, java.lang.String, java.lang.Object)
   */
  @Override
  public boolean sendMessageToBehaviour(Class<?> theBehaviour, String message, Object args)
  {
    for (IBehaviour b : this.behaviours)
    {
      if (b.getClass().equals(theBehaviour))
      {
		  if (args != null)
		  {
			  b.broadcastMessage(message, args);
		  }
		  else
		  {
			  b.broadcastMessage(message);
		  }
        return true;
      }
    }
    return false;
  }





  @Override
  public Transform transform()
  {
    return this.transform;
  }

  @Override
  public Scene getScene()
  {
    return mScene;
  }


//  public static NodeEngine newInstance()
//  {
//    return new NodeEngine();
//  }


  public static RootNode newRootNode()
  {
    return RootNodeImpl.newInstance();
  }


  @Override
  public void addGeometryToList(Set<Geometry> geometries)
  {
    if (this.geometry() != null && this.geometry().isVisible())
    {
      geometries.add(this.geometry());
    }
    this.getChildren().stream().forEach(child -> child.addGeometryToList(geometries));
  }


  @Override
  public void setScene(Scene aScene)
  {
    mScene = aScene;
    mChildren.forEach(aNode -> aNode.setScene(aScene));
  }
}



