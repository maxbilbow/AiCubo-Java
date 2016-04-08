package com.maxbilbow.aicubo.view;

import com.maxbilbow.aicubo.control.GameController;
import com.maxbilbow.aicubo.engine.*;
import com.maxbilbow.aicubo.engine.behaviours.Behaviour;
import com.maxbilbow.aicubo.engine.gl.CursorCallback;
import com.maxbilbow.aicubo.engine.gl.KeyCallback;
import com.maxbilbow.aicubo.engine.gl.SharedLibraryLoader;
import com.maxbilbow.aicubo.engine.math.Vector4;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.Instant;
import java.util.LinkedList;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.system.MemoryUtil.NULL;

public abstract class GameView implements GLView
{

  private GLFWWindowSizeCallback windowSizeCallback;

  private GameController mGameController;

  private final Vector4 clearColor = new Vector4();

  private Node _pointOfView;
  private int height = 720, width = 1280;

  // We need to strongly reference callback instances.

  private GLFWErrorCallback errorCallback;

  //	@Resource
  private KeyCallback mKeyCallback;
  // The window handle
  private long        window;

  //	@Resource
  private CursorCallback cursorCallback;

//	protected Scene scene;

  private boolean debugging = false;

  private int framesPerUpdate = 60;

  private GLViewPerFrameLog perFramDebugInfo = () -> "";
  private Logger            mLogger          = LoggerFactory.getLogger(GameView.class);


  public GameView(GameController aGameController)
  {
    mGameController = aGameController;
    init();
  }

  @PostConstruct
  public void init()
  {
    this.initCallbacks();
    this.onAwake();
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

      renderFrame(getScene());

      // swap the color buffers
      glfwSwapBuffers(window());
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

  @Override
  public GLFWErrorCallback errorCallback()
  {
    return errorCallback;
  }

  protected GLViewPerFrameLog getPerFramDebugInfo()
  {
    return perFramDebugInfo;
  }


  public Scene getScene()
  {
    return Scene.getCurrent();
  }


  @Override
  public int height()
  {
    return this.height;
  }

  protected void initCallbacks()
  {
    mKeyCallback = KeyCallback.getInstance();
    mKeyCallback.setGameController(mGameController);
    cursorCallback = CursorCallback.getInstance();
    cursorCallback.setGameController(mGameController);
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
  public void initGLCallbacks(long window)
  {
    glfwSetErrorCallback(errorCallback = errorCallbackPrint(System.err));
    glfwSetKeyCallback(window, mKeyCallback);
    glfwSetCursorPosCallback(window, cursorCallback);
    glfwSetWindowSizeCallback(window, this.windowSizeCallback);
  }

  public boolean isDebugging()
  {
    return debugging;
  }

  @Override
  public KeyCallback keyCallback()
  {
    return mKeyCallback;
  }

  protected abstract void onAwake();

  @Override
  public Node pointOfView()
  {
    if (_pointOfView != null || this.setPointOfView(Nodes.getCurrent()))
    {
      return _pointOfView;
    }
    mLogger.debug("ERROR: Could Not Set _pointOfView", true);
    return null;
  }

  protected abstract void renderFrame(Scene scene);

  public void setDebugging(boolean debugging, int framesPerUpdate)
  {
    this.debugging = debugging;
    this.framesPerUpdate = framesPerUpdate;
  }

  public void setPerFramDebugInfo(GLViewPerFrameLog perFramDebugInfo)
  {
    this.perFramDebugInfo = perFramDebugInfo;
  }


  @Override
  public boolean setPointOfView(Node pointOfView)
  {
    if (pointOfView.camera() == null)
    {
      pointOfView.setCamera(new Camera());
      pointOfView.addBehaviour(new Behaviour()
      {

        @Override
        public void broadcastMessage(String message, Object args)
        {

          if (message == "lookUp")
          {
            //					mLogger.debug(message + ": "+ args , false);
            pointOfView.transform().rotate("pitch", (float) args);
          }
        }

        @Override
        public boolean hasLateUpdate()
        {
          return false;
        }

        @Override
        protected void onAwake()
        {

        }

        @Override
        public void update(Node node)
        {
          // TODO Auto-generated method stub

        }

      });
    }
    else if (_pointOfView == pointOfView)
    {
      return false;
    }
    if (_pointOfView != null)
    {
      _pointOfView.camera().stopListening();
    }

    _pointOfView = pointOfView;
    _pointOfView.camera().startListening();
    return true;
  }

  public void setScene(Scene scene)
  {
    scene.makeCurrent();
  }

  public void setSize(int width, int height)
  {
    this.width = width;
    this.height = height;
  }

  @Override
  public void setWindow(long window)
  {
    this.window = window;
    if (this.window == NULL)
    {
      throw new RuntimeException("Failed to create the GLFW window");
    }
  }

  @Override
  public void setWindowSizeCallback(GLFWWindowSizeCallback windowSizeCallback)
  {
    this.windowSizeCallback = windowSizeCallback;
  }

  @Override
  public int width()
  {
    return this.width;
  }

  @Override
  public long window()
  {
    return window;
  }

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

  @Deprecated
  public static GLView newInstance(GameController aGameController)
  {
    return newInstance(DEFAULT, aGameController);
  }

  @Deprecated
  public static GLView newInstance(int type, GameController aGameController)
  {
    GLView view;
    switch (type)
    {
      case OLD:
//			return new GameView1();
      case SHADER:
      case DEFAULT:
      default:
        return new GLViewImpl(aGameController);
    }
  }

  public GameController getGameController()
  {
    return mGameController;
  }

  public void setGameController(GameController aGameController)
  {
    mGameController = aGameController;
  }

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
      long window;
      this.setWindow(window = this.initWindow());

      this.initGLCallbacks(window);

      mLogger.debug("Init GL");

      GLContext.createFromCurrent();
      this.initGL(window);

      mLogger.debug("Enter Gameloop");
      this.enterGameLoop(window);

      // Release window and window callbacks
      glfwDestroyWindow(window);
      keyCallback().release();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    finally
    {
      // Terminate GLFW and release the GLFWerrorfun
      glfwTerminate();
      errorCallback().release();
    }
  }
}
