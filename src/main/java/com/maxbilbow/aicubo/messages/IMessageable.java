package com.maxbilbow.aicubo.messages;

import java.lang.reflect.InvocationTargetException;

public interface IMessageable
{
  /**
   * @param message Name of selector or any other message
   * @param args    any object.
   * @throws SecurityException
   * @throws NoSuchMethodException
   * @throws InvocationTargetException
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   * @author Max Bilbow, 15-08-04 16:08:55
   * <p>
   * Receives a message
   * Has to be overridden for to add specific method handing
   * as it is currently not automatic to call a method this way
   * @since 0.1
   */
  public void sendMessage(String message, Object args)
          throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
          InvocationTargetException;


  /**
   * @param message Name of selector or any other message
   * @param args    any object.
   * @author Max Bilbow, 15-08-04 16:08:55
   * <p>
   * Sends message to any children who are listenting
   * @since 0.1
   */
  public void broadcastMessage(String message);

  /**
   * @param message Name of selector or any other message
   * @param args    any object.
   * @author Max Bilbow, 15-08-04 16:08:55
   * <p>
   * Sends message to any children who are listenting
   * @since 0.1
   */
  public void broadcastMessage(String message, Object args);


  /**
   * Enter the name of a method (not including arguments). e.g."onEventDidEnd(String,Object)" becomes "onEventDidEnd"
   *
   * @param method name: "onEventDidEnd"
   * @return
   */
  boolean implementsMethod(String method, Class<?>... args);


}
