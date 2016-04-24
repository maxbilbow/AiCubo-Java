package com.maxbilbow.aicubo;

import org.slf4j.LoggerFactory;
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
    try
    {
      SpringApplication.run(Application.class, args);
    }
    catch (Exception e)
    {
      LoggerFactory.getLogger(Application.class).error("Big Fat Error!",e);
    }
//    InfoWindow.open();

  }
}
