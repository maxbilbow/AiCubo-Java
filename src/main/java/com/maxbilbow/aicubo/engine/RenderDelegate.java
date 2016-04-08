package com.maxbilbow.aicubo.engine;

public interface RenderDelegate
{
  public void updateBeforeSceneLogic(Object... args);

  public void updateBeforeSceneRender(Object... args);
//	public void updateLast(Object args);
}
