package com.maxbilbow.aicubo.exception;

/**
 * Created by Max on 18/04/2016.
 */
public class AiCuboException extends RuntimeException
{

  public AiCuboException(Exception aException)
  {
    super(aException);
  }
}
