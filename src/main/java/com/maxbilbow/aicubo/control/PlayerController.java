package com.maxbilbow.aicubo.control;

import com.maxbilbow.aicubo.engine.RenderDelegate;
import com.maxbilbow.aicubo.engine.gl.KeyCallback;
import com.maxbilbow.aicubo.model.Node;
import com.maxbilbow.aicubo.model.Nodes;
import com.maxbilbow.aicubo.view.SceneRenderer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Created by Max on 06/05/2016.
 */
@Component
public class PlayerController implements RenderDelegate
{

  @Resource
  private Nodes mNodes;

  @Resource
  private SceneRenderer mRenderer;

  @Resource
  private KeyCallback mKeyCallback;
  private Node mPlayer;

  private static PlayerController INSTANCE;
  @PostConstruct
  private void init()
  {
    INSTANCE = this;
    mRenderer.addDelegate(this);
  }

  @Override
  public void updateBeforeSceneLogic(Object... args)
  {
    final Node player = getPlayer();

    if (mKeyCallback.isDown(GLFW_KEY_W))
    {
      player.broadcastMessage("applyForce", "forward:-1");
    }

    if (mKeyCallback.isDown(GLFW_KEY_S))
    {
      player.broadcastMessage("applyForce", "forward:1");
    }

    if (mKeyCallback.isDown(GLFW_KEY_A))
    {
      player.broadcastMessage("applyForce", "left:-1");
    }

    if (mKeyCallback.isDown(GLFW_KEY_D))
    {
      player.broadcastMessage("applyForce", "left:1");
    }

    if (mKeyCallback.isDown(GLFW_KEY_Q))
    {
      player.broadcastMessage("applyForce", "up:-1");
    }

    if (mKeyCallback.isDown(GLFW_KEY_E))
    {
      player.broadcastMessage("applyForce", "up:1");
    }


    if (mKeyCallback.isDown(GLFW_KEY_RIGHT))
    {
      player.broadcastMessage("applyTorque", "yaw:0.5");
    }

    if (mKeyCallback.isDown(GLFW_KEY_LEFT))
    {
      player.broadcastMessage("applyTorque", "yaw:-0.5");
    }

    if (mKeyCallback.isDown(GLFW_KEY_UP))
    {
      player.broadcastMessage("lookUp", 0.5f);
    }

    if (mKeyCallback.isDown(GLFW_KEY_DOWN))
    {
      player.broadcastMessage("lookUp", -0.5f);
    }

    if (mKeyCallback.isDown(GLFW_KEY_X))
    {
      player.broadcastMessage("applyTorque", "roll:0.2");
    }

    if (mKeyCallback.isDown(GLFW_KEY_Z))
    {
      player.broadcastMessage("applyTorque", "roll:-0.2");
    }
  }

  @Override
  public void updateBeforeSceneRender(Object... args)
  {

  }

  public void setPlayer(Node aPlayer)
  {
    mPlayer = aPlayer;
  }

  public Node getPlayer()
  {
    return mPlayer;
  }

  public static Node current()
  {
    if (INSTANCE != null)
    {
      return INSTANCE.getPlayer();
    }
    return null;
  }
}
