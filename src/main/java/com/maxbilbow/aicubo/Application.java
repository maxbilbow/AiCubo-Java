package com.maxbilbow.aicubo;

import com.maxbilbow.aicubo.hud.InfoWindow;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by Max on 07/04/2016.
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Application
{

  public static void main(String[] args)
  {
    InfoWindow.open();
    SpringApplication.run(Application.class,args);

  }
}
