package com.maxbilbow.aicubo.control;


import com.maxbilbow.aicubo.model.Scene;
import com.maxbilbow.aicubo.model.core.RMXObject;
import com.maxbilbow.aicubo.model.Node;
import com.maxbilbow.aicubo.model.Nodes;
import com.maxbilbow.aicubo.engine.RenderDelegate;
import com.maxbilbow.aicubo.engine.gl.IKeyCallback;
import com.maxbilbow.aicubo.engine.gl.KeyStates;
import com.maxbilbow.aicubo.view.GLView;
import com.maxbilbow.aicubo.view.GameView;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;

import static org.lwjgl.glfw.GLFW.*;


public abstract class GameController extends RMXObject implements RenderDelegate, Runnable
{

  //  @Resource
//  @Qualifier("GLView")
  protected GLView mView;

  private static GameController singleton;// = new GameController();
  private Logger mLogger = LoggerFactory.getLogger(GameController.class);

  public GameController()
  {
    this.setView(GameView.newInstance(this));
  }

  @PostConstruct
  public void init()
  {

    if (singleton == null)
    {
      singleton = this;
    }
    else
    {
      throw new IllegalArgumentException("Two GameController Instances cannot exist");
    }
    this.initpov();
    this.setup();

  }

  public GLView getView()
  {
    return this.mView;
  }

  public static boolean isInitialized()
  {
    return singleton != null;
  }

  public static GameController getInstance()
  {
//		if (singleton != null)
    return singleton;
//		else
//			throw new IllegalArgumentException("Abstract class GameController must have been initialized first");

  }

  protected abstract void initpov();

  public abstract void setup();

  public void addKeyCallback(IKeyCallback callback)
  {
    this.mView.keyCallback().callbacks.add(callback);
  }

  public void addKeyListenerForKey(int key, IKeyCallback listener)
  {
    this.mView.keyCallback().addKeyListenerForKey(key, listener);
  }

  @Override
  public void run()
  {
//        
    System.out.println("Hello LWJGL " + GLFW.glfwGetVersionString() + "!");
    mLogger.debug("Setup GameController");

    this.mView.run();
  }

  @Override
  public void updateBeforeSceneLogic(Object... args)
  {
    this.repeatedKeys();
  }


  public final KeyStates keys = new KeyStates();

  private void repeatedKeys()
  {
    Node player = Nodes.getCurrent();

    if (this.keys.getOrDefault(GLFW_KEY_W, false))
    {
      player.broadcastMessage("applyForce", "forward:-1");
    }

    if (this.keys.getOrDefault(GLFW_KEY_S, false))
    {
      player.broadcastMessage("applyForce", "forward:1");
    }

    if (this.keys.getOrDefault(GLFW_KEY_A, false))
    {
      player.broadcastMessage("applyForce", "left:-1");
    }

    if (this.keys.getOrDefault(GLFW_KEY_D, false))
    {
      player.broadcastMessage("applyForce", "left:1");
    }

    if (this.keys.getOrDefault(GLFW_KEY_Q, false))
    {
      player.broadcastMessage("applyForce", "up:-1");
    }

    if (this.keys.getOrDefault(GLFW_KEY_E, false))
    {
      player.broadcastMessage("applyForce", "up:1");
    }


    if (this.keys.getOrDefault(GLFW_KEY_RIGHT, false))
    {
      player.broadcastMessage("applyTorque", "yaw:0.5");
    }

    if (this.keys.getOrDefault(GLFW_KEY_LEFT, false))
    {
      player.broadcastMessage("applyTorque", "yaw:-0.5");
    }

    if (this.keys.getOrDefault(GLFW_KEY_UP, false))
    {
      player.broadcastMessage("lookUp", 0.5f);
    }

    if (this.keys.getOrDefault(GLFW_KEY_DOWN, false))
    {
      player.broadcastMessage("lookUp", -0.5f);
    }

    if (this.keys.getOrDefault(GLFW_KEY_X, false))
    {
      player.broadcastMessage("applyTorque", "roll:0.2");
    }

    if (this.keys.getOrDefault(GLFW_KEY_Z, false))
    {
      player.broadcastMessage("applyTorque", "roll:-0.2");
    }
//		System.out.println("hello");
  }


  public void setView(GLView view)
  {
    this.mView = view;
//		this.view.setDelegate(this);
  }

  //	private Thread thread;
  public void Start()
  {
//			thread = new Thread(this,"Game");
//			thread.start();
    this.run();
  }


  public abstract Scene getScene();
}

