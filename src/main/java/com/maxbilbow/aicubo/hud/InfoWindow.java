package com.maxbilbow.aicubo.hud;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author José
 */
public class InfoWindow extends Application
{

  @Override
  public void start(Stage stage)
  {

    try
    {
      FXMLLoader loader =
              new FXMLLoader(
                      getClass().getResource("ihm.fxml"));

      Parent root = loader.load();
      stage.setScene(new Scene(root));
      stage.show();
    }
    catch (IOException ioe)
    {
      System.err.println(ioe.getMessage());
    }
  }


  public static void open()
  {
//		new Thread(InfoWindow::launch).start();
  }
}
