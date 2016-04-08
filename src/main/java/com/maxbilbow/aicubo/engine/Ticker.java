package com.maxbilbow.aicubo.engine;

public interface Ticker
{
  public long tick();

  public void updateTick(long newTick);
}
