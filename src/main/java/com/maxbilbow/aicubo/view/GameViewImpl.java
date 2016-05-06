package com.maxbilbow.aicubo.view;

import com.maxbilbow.aicubo.engine.GLViewPerFrameLog;
import com.maxbilbow.aicubo.engine.gl.CursorCallback;
import com.maxbilbow.aicubo.engine.gl.KeyCallback;
import com.maxbilbow.aicubo.engine.gl.SharedLibraryLoader;
import com.maxbilbow.aicubo.engine.math.Vector4;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

@Component
public class GameViewImpl implements GameView
{

  @Resource
  private GLInitializer mGLInitializer;

  private GLFWWindowSizeCallback windowSizeCallback;

  @Resource
  private SceneRenderer mSceneRenderer;

  private final Vector4 clearColor = new Vector4();

  private int height = 720, width = 1280;

  // We need to strongly reference callback instances.

//  @Resource
  private GLFWErrorCallback mErrorCallback;

  @Resource
  private KeyCallback mKeyCallback;
  // The window handle
  private long        mWindow;

  @Resource
  private CursorCallback mCursorCallback;

//	protected Scene scene;

  private boolean debugging = false;

  private int framesPerUpdate = 60;

  private GLViewPerFrameLog perFramDebugInfo = () -> "";
  private Logger            mLogger          = LoggerFactory.getLogger(getClass());



  @PostConstruct
  public void onAwake()
  {
    this.setDebugging(true, 60);
    this.setClearColor(0.3f, 0.3f, 0.8f, 1.0f);

    this.windowSizeCallback = new GLFWWindowSizeCallback()
    {

      @Override
      public void invoke(long arg0, int width, int height)
      {
        setSize(width, height);
      }

    };
  }

  @Override
  public void enterGameLoop(long window)
  {


    LinkedList<Long> frameTime = null;
    int counter = 0;
    if (debugging)
    {
      frameTime = new LinkedList<>();
      for (int i = 0; i < framesPerUpdate; ++i)
      {
        frameTime.add((long) 0);
      }
    }
    Instant start = null, end = null;

    while (glfwWindowShouldClose(window) == GL_FALSE)
    {

      if (debugging)
      {
        start = Instant.now();
      }

      clearColor();

      mSceneRenderer.renderFrame(width,height);

      // swap the color buffers
      glfwSwapBuffers(mWindow);
      // Poll for window events. The key callback above will only be
      // invoked during this call.
      glfwPollEvents();

      if (debugging)
      {
        end = Instant.now();
        Duration duration = Duration.between(start, end);
        frameTime.addLast(duration.toMillis());
        frameTime.removeFirst();

        Long total = (long) 0;
        if (++counter == framesPerUpdate)
        {
          for (Long t : frameTime)
          {
            total += t;
          }
          String s = perFramDebugInfo.getLog();
          mLogger.debug(s + "\n Average time per loop in milliseconds: " + total / framesPerUpdate, false);
          counter = 0;
        }
      }
    }
  }


  protected void clearColor()
  {
    glClearColor(
            clearColor.x,
            clearColor.y,
            clearColor.z,
            clearColor.w
    );
  }

//  @Override
//  public GLFWErrorCallback errorCallback()
//  {
//    return errorCallback;
//  }

  protected GLViewPerFrameLog getPerFramDebugInfo()
  {
    return perFramDebugInfo;
  }



  @Override
  public int height()
  {
    return this.height;
  }




  private void initGLCallbacks(long window)
  {
    glfwSetErrorCallback(mErrorCallback = errorCallbackPrint(System.err));
    glfwSetKeyCallback(window, mKeyCallback);
    glfwSetCursorPosCallback(window, mCursorCallback);
    glfwSetWindowSizeCallback(window, this.windowSizeCallback);
  }

  public boolean isDebugging()
  {
    return debugging;
  }

//  @Override
//  private KeyCallback keyCallback()
//  {
//    return mKeyCallback;
//  }




  public void setDebugging(boolean debugging, int framesPerUpdate)
  {
    this.debugging = debugging;
    this.framesPerUpdate = framesPerUpdate;
  }

  public void setPerFrameDebugInfo(GLViewPerFrameLog perFramDebugInfo)
  {
    this.perFramDebugInfo = perFramDebugInfo;
  }



  public void setSize(int width, int height)
  {
    this.width = width;
    this.height = height;
  }

//  @Override
  protected void setWindow(long window)
  {
    this.mWindow = window;
    if (this.mWindow == NULL)
    {
      throw new RuntimeException("Failed to create the GLFW window");
    }
  }

//  @Override
  protected void setWindowSizeCallback(GLFWWindowSizeCallback windowSizeCallback)
  {
    this.windowSizeCallback = windowSizeCallback;
  }

  @Override
  public int width()
  {
    return this.width;
  }

//  @Override
//  public long window()
//  {
//    return window;
//  }

  public Vector4 getClearColor()
  {
    return clearColor;
  }

  public void setClearColor(float r, float g, float b, float a)
  {
    this.clearColor.x = r;
    this.clearColor.y = g;
    this.clearColor.z = b;
    this.clearColor.w = a;
  }

  public static final int DEFAULT = 0, OLD = 1, SHADER = 2;

//  @Deprecated
//  public static GLView newInstance(GameController aGameController)
//  {
//    return newInstance(DEFAULT, aGameController);
//  }

//  @Deprecated
//  public static GLView newInstance(int type, GameController aGameController)
//  {
//    GLView view;
//    switch (type)
//    {
//      case OLD:
////			return new GameView1();
//      case SHADER:
//      case DEFAULT:
//      default:
//        return new GLViewImpl(aGameController);
//    }
//  }
//
//  public void setGameController(GameController aGameController)
//  {
//    mGameController = aGameController;
//  }

  public void run()
  {

    try
    {

      mLogger.debug("Load Shared Libraries");
      SharedLibraryLoader.load();

      // Initialize GLFW. Most GLFW functions will not work before doing this.
      if (glfwInit() != GL11.GL_TRUE)
      {
        throw new IllegalStateException("Unable to initialize GLFW");
      }
      this.setWindow(this.initWindow());

      this.initGLCallbacks(mWindow);

      mLogger.debug("Init GL");

      GLContext.createFromCurrent();
      mGLInitializer.initGL(mWindow);

      mLogger.debug("Enter Gameloop");
      this.enterGameLoop(mWindow);

      // Release window and window callbacks
      glfwDestroyWindow(mWindow);
      mKeyCallback.release();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      // Terminate GLFW and release the GLFWerrorfun
      glfwTerminate();
      mErrorCallback.release();
    }
  }

  private long initWindow()
  {
    long window;
    this.setWindow(window = glfwCreateWindow(width(), height(), "Hello World!", NULL, NULL));
    // Configure our window
    glfwDefaultWindowHints(); // optional, the current window hints are already the default
    glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // the window will stay hidden after creation
    glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // the window will be resizable


    ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
    // Center our window
    glfwSetWindowPos(
            mWindow,
            (GLFWvidmode.width(vidmode) - width()) / 2,
            (GLFWvidmode.height(vidmode) - height()) / 2
    );

    // Make the OpenGL context current
    glfwMakeContextCurrent(window);
    // Enable v-sync
    glfwSwapInterval(1);

    // Make the window visible
    glfwShowWindow(window);
    return mWindow;
  }
}
