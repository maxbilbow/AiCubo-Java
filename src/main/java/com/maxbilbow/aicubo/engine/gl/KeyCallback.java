package com.maxbilbow.aicubo.engine.gl;

import com.maxbilbow.aicubo.model.Nodes;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;

//import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;


@Component
public class KeyCallback extends GLFWKeyCallback
{
  public static char forward = 'w', back = 's', left = 'a', right = 'd', up = 'e', down = 'q', stop = 'c', jump = ' ';
//	public static boolean update = true;
  //, keySpecialStates[] = new boolean[246];


  @Resource
  private CursorCallback mCursor;

  public final KeyStates mKeys = new KeyStates();

  private ArrayList<IKeyCallback> mCallbacks = new ArrayList<>();

  @Override
  public void invoke(long window, int key, int scancode, int action, int mods)
  {
    if (action == GLFW_PRESS)
    {
      mKeys.put(key, true);
//			System.out.println("Key Down: " + (char) key + " "+ scancode + " "+ action + " "+ mods);
    }
    else if (action == GLFW_RELEASE)
    {
      mKeys.put(key, false);
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

          if (mCursor.isCursorLocked())
          {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            mCursor.lockCursor(false);
          }
          else
          {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            mCursor.lockCursor(true);
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

    for (IKeyCallback callback : this.mCallbacks)
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

  public void addCallback(IKeyCallback aKeyCallback)
  {
    mCallbacks.add(aKeyCallback);
  }

  public boolean isDown(int aKeyCode)
  {
    return mKeys.getOrDefault(aKeyCode,false);
  }
}
