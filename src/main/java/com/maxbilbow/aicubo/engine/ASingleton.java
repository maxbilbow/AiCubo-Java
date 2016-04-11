/**
 *
 */
package com.maxbilbow.aicubo.engine;


import com.maxbilbow.aicubo.model.core.RMXObject;

/**
 * @author Max
 */
public class ASingleton/*<T extends RMXObject>*/ extends RMXObject
{

  protected ASingleton()
  {
  }

  public static ASingleton getInstance()
  {
    if (singleton == null)
    {
      synchronized (ASingleton.class)
      {
        if (singleton == null)
        {
          singleton = new ASingleton();
        }
      }
    }
    return singleton;
  }

  private static ASingleton singleton;


  public static void main(String[] args)
  {
    // TODO Auto-generated method stub

  }


}
