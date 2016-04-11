package com.maxbilbow.aicubo.view;

import com.maxbilbow.aicubo.model.Node;
import com.maxbilbow.aicubo.engine.gl.KeyCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWvidmode;

import java.nio.ByteBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

public interface GLView
{
  void setWindowSizeCallback(GLFWWindowSizeCallback cbfun);

  void initGL(long window);

  void enterGameLoop(long window);

  long window();

  GLFWErrorCallback errorCallback();

  KeyCallback keyCallback();

  int height();

  int width();

  //	void setSize(int width, int height);
  boolean setPointOfView(Node cam);

  Node pointOfView();


  default long initWindow()
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
            this.window(),
            (GLFWvidmode.width(vidmode) - width()) / 2,
            (GLFWvidmode.height(vidmode) - height()) / 2
    );

    // Make the OpenGL context current
    glfwMakeContextCurrent(window);
    // Enable v-sync
    glfwSwapInterval(1);

    // Make the window visible
    glfwShowWindow(window);
    return this.window();
  }

  void setWindow(long window);

  void initGLCallbacks(long window);

  void run();
}
