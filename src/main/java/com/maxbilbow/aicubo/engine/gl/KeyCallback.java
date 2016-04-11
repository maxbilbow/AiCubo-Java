package com.maxbilbow.aicubo.engine.gl;

import com.maxbilbow.aicubo.control.GameController;
import com.maxbilbow.aicubo.model.Nodes;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;

//import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;


//@Component
public class KeyCallback extends GLFWKeyCallback
{
  public static char forward = 'w', back = 's', left = 'a', right = 'd', up = 'e', down = 'q', stop = 'c', jump = ' ';
//	public static boolean update = true;
  //, keySpecialStates[] = new boolean[246];

  @Deprecated
  private static KeyCallback singleton;// = new KeyCallback();


  //	@Resource
  private GameController mGameController;

  public KeyCallback()
  {
    if (singleton == null)
    {
      singleton = this;
    }
//		mGameController = aGameController;
  }

  //    boolean mouseLocked = false;
  public static KeyCallback getInstance()
  {
    return singleton != null ? singleton : (singleton = new KeyCallback());
  }

  public ArrayList<IKeyCallback> callbacks = new ArrayList<>();

  @Override
  public void invoke(long window, int key, int scancode, int action, int mods)
  {
    if (action == GLFW_PRESS)
    {
      mGameController.keys.put(key, true);
//			System.out.println("Key Down: " + (char) key + " "+ scancode + " "+ action + " "+ mods);
    }
    else if (action == GLFW_RELEASE)
    {
      mGameController.keys.put(key, false);
//			System.out.println("  Key Up: " + (char) key + " "+ scancode + " "+ action + " "+ mods);
    }

    if (action == GLFW_RELEASE)
    {
      switch (key)
      {
        case GLFW_KEY_ESCAPE:
          glfwSetWindowShouldClose(window, GL_TRUE);
          break;
        case GLFW_KEY_W:
//			 Node.getCurrent().transform.moveForward(1);
          break;
        case GLFW_KEY_M:
          CursorCallback cursor = CursorCallback.getInstance();
          if (cursor.isCursorLocked())
          {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            cursor.lockCursor(false);
          }
          else
          {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            cursor.lockCursor(true);
          }
          break;
        case GLFW_KEY_SPACE:
          Nodes.getCurrent().broadcastMessage("jump");
          break;
      }
    }
    else if (action == GLFW_PRESS)
    {
      switch (key)
      {
        case GLFW_KEY_SPACE:
          Nodes.getCurrent().broadcastMessage("crouch");
      }
    }

    for (IKeyCallback callback : this.callbacks)
    {
      callback.invoke(window, key, scancode, action, mods);
    }
    ArrayList<IKeyCallback> listeners = this.keyListeners.getOrDefault(key, null);
    if (listeners != null)
    {
      for (IKeyCallback callback : listeners)
      {
        callback.invoke(window, key, scancode, action, mods);
      }
    }
  }


  HashMap<Integer, ArrayList<IKeyCallback>> keyListeners = new HashMap<>();

  public void addKeyListenerForKey(int key, IKeyCallback listener)
  {
    if (!keyListeners.containsKey(key))
    {
      keyListeners.put(key, new ArrayList<>());
    }
    keyListeners.get(key).add(listener);
  }

  public void setGameController(GameController aGameController)
  {
    mGameController = aGameController;
  }
}
