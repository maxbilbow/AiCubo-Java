package com.maxbilbow.aicubo.engine;


import com.maxbilbow.aicubo.core.RMXObject;
import com.maxbilbow.aicubo.engine.math.Matrix4;
import com.maxbilbow.aicubo.engine.physics.PhysicsWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;


public class Scene extends RMXObject
{

  private final Logger       mLogger      = LoggerFactory.getLogger(Scene.class);
  private       PhysicsWorld physicsWorld = new PhysicsWorld();
  public final   RootNode rootNode;
  private static Scene    _current;// = new node(null,null,null);

  public Scene()
  {
    mLogger.debug("Scene initializing...");
    this.rootNode = Nodes.newRootNode();
    if (_current == null)
    {
      _current = this;
    }
  }

  public RootNode rootNode()
  {
    return this.rootNode;
  }

  //	private static Scene current;
  public static Scene getCurrent()
  {
//		mLogger.debug("Get current scene...");
    if (_current != null)
    {
      return _current;
    }
    else
    {
      _current = new Scene();
    }
    return _current;
  }

//	public static Scene setCurrent(Scene scene) {
//		Scene old = _current;
//		_current = scene;
//		return old;
//	}

  public void makeCurrent()
  {
    _current = this;
  }


  protected static class node
  {
    Scene scene;
    node  next;
    node  prev;

    node(node prev, Scene scene, node next)
    {
      this.scene = scene;
      this.next = next;
      this.prev = prev;
    }

  }

  private RenderDelegate renderDelegate;


  public void renderScene(Matrix4 viewMatrix)
  {

    Stream<Node> stream = this.rootNode.getChildren().stream();

    stream.forEach(n -> {
      n.shine();
      if (viewMatrix != null)
      {
        n.draw(viewMatrix);
      }
    });


  }

  private long _tick = 0;

  public long tick()
  {
    return _tick;
  }

  public void updateSceneLogic()
  {
    long time = this._tick = System.currentTimeMillis();
    if (this.renderDelegate != null)
    {
      this.renderDelegate.updateBeforeSceneLogic();
    }
//		 Thread logicThread = new Thread(() -> {
    this.rootNode.updateLogic(time);
    this.physicsWorld.updatePhysics(this.rootNode);
    this.physicsWorld.updateCollisionEvents(this.rootNode);

    this.rootNode.updateAfterPhysics(time);
    if (this.renderDelegate != null)
    {
      this.renderDelegate.updateBeforeSceneRender();
    }

  }

  public RenderDelegate getRenderDelegate()
  {
    return renderDelegate;
  }

  public void setRenderDelegate(RenderDelegate renderDelegate)
  {
//		mLogger.debug("Setting render delegate: " + renderDelegate);
    this.renderDelegate = renderDelegate;
  }


  public PhysicsWorld getPhysicsWorld()
  {
    return this.physicsWorld;
  }
}
