package com.maxbilbow.aicubo.engine.gl;


public interface IKeyCallback
{

  public void invoke(long window, int key, int scancode, int action, int mods);
}