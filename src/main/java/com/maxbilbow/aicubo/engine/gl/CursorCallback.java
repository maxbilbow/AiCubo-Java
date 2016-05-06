package com.maxbilbow.aicubo.engine.gl;


import com.maxbilbow.aicubo.control.PlayerController;
import com.maxbilbow.aicubo.engine.math.Vector3;
import com.maxbilbow.aicubo.view.PointOfView;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class CursorCallback extends GLFWCursorPosCallback
{
//	void center();
//	void MouseButton(int button, int state, int x, int y);
//	void MouseMotion(int x, int y);
//	void mouseFree(int x, int y);

  @Resource
  private PointOfView      mPointOfView;

  @Resource
  private PlayerController mPlayerController;

//  private static CursorCallback singleton = new CursorCallback();

  public CursorCallback()
  {
  }

//  public static CursorCallback getInstance()
//  {
//    return singleton;
//  }


  private double xpos, ypos;
  private boolean restart     = true;
  private boolean cusorLocked = false;

  @Override
  public void invoke(long window, double xpos, double ypos)
  {
    if (!cusorLocked)
    {
      return;
    }
    if (restart)
    {
      this.xpos = xpos;
      this.ypos = ypos;
      restart = false;
      return;
    }
    else
    {
      double dx = xpos - this.xpos;
      double dy = ypos - this.ypos;
      dx *= 0.05 * 0.2;
      dy *= 0.01 * 0.2;
      this.xpos = xpos;
      this.ypos = ypos;
      mPlayerController.getPlayer().physicsBody().applyTorque((float) dx, "yaw", Vector3.Zero);
      mPointOfView.get().broadcastMessage("lookUp", (float) dy);
//			mLogger.debugAndPrint(dx + ", " + dy, false);
    }


  }

  public boolean isCursorLocked()
  {
    return cusorLocked;
  }

  public void lockCursor(boolean lock)
  {
    this.cusorLocked = lock;
    this.restart = true;
  }

}
