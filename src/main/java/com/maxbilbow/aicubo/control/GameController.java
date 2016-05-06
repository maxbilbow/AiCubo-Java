package com.maxbilbow.aicubo.control;


import com.maxbilbow.aicubo.model.core.RMXObject;
import com.maxbilbow.aicubo.view.GameView;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class GameController extends RMXObject implements Runnable
{
  @Resource
  private GameView mView;

  private Logger mLogger = LoggerFactory.getLogger(GameController.class);


  @Override
  public void run()
  {
//        
    System.out.println("Hello LWJGL " + GLFW.glfwGetVersionString() + "!");
    mLogger.debug("Setup GameController");

    this.mView.run();
  }





}

