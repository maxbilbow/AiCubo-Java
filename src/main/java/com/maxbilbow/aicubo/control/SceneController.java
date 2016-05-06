package com.maxbilbow.aicubo.control;

import com.maxbilbow.aicubo.model.Scene;
import org.springframework.stereotype.Component;

/**
 * Created by Max on 06/05/2016.
 */
@Component
public class SceneController
{

  private Scene mScene = new Scene();

  public Scene getScene()
  {
    return mScene;
  }

  public void setScene(Scene aScene)
  {
    mScene = aScene;
  }
}
