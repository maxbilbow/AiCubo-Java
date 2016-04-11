package com.maxbilbow.aicubo.engine;

public interface Ticker
{
  long tick();

  void updateTick(long newTick);
}
