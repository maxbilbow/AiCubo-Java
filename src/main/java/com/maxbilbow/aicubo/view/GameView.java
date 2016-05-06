package com.maxbilbow.aicubo.view;

public interface GameView
{
//  void setWindowSizeCallback(GLFWWindowSizeCallback cbfun);

//  void initGL(long window);

  void enterGameLoop(long window);

//  long window();

//  GLFWErrorCallback errorCallback();

//  KeyCallback keyCallback();

  int height();

  int width();

  //	void setSize(int width, int height);
//  boolean set(Node cam);

//  Node pointOfView();

//  void setWindow(long window);
//
//  void initGLCallbacks(long window);

  void run();
}
