package com.maxbilbow.aicubo.view;

import com.maxbilbow.aicubo.engine.RenderDelegate;

/**
 * Created by Max on 06/05/2016.
 */
public interface SceneRenderer
{

  void renderFrame(int aWidth, int aHeight);

  void addDelegate(RenderDelegate aRenderDelegate);

  boolean removeDelegate(RenderDelegate aRenderDelegate);
}
