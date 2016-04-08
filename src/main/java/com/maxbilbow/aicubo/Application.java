package com.maxbilbow.aicubo;

import com.maxbilbow.aicubo.hud.InfoWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by Max on 07/04/2016.
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class Application
{

  public static void main(String[] args)
  {

//    if (click.rmx.util.OSValidator.isMac())
//    {
//      System.setProperty("startOnFirstThread","true");
//    }
    SpringApplication.run(Application.class, args);

    InfoWindow.open();

  }
}
