package com.maxbilbow.aicubo.engine.gl;


import com.maxbilbow.aicubo.control.GameController;
import com.maxbilbow.aicubo.engine.Nodes;
import com.maxbilbow.aicubo.engine.math.Vector3;
import org.lwjgl.glfw.GLFWCursorPosCallback;

//@Component
public class CursorCallback extends GLFWCursorPosCallback
{
//	void center();
//	void MouseButton(int button, int state, int x, int y);
//	void MouseMotion(int x, int y);
//	void mouseFree(int x, int y);

  //  @Resource
  private GameController mGameController;

  private static CursorCallback singleton = new CursorCallback();

  public CursorCallback()
  {
  }

  public static CursorCallback getInstance()
  {
    return singleton;
  }


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
      Nodes.getCurrent().physicsBody().applyTorque((float) dx, "yaw", Vector3.Zero);
      mGameController.getView().pointOfView().broadcastMessage("lookUp", (float) dy);
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

  public void setGameController(GameController aGameController)
  {
    mGameController = aGameController;
  }
}
