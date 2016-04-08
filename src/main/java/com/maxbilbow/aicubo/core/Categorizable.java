package com.maxbilbow.aicubo.core;


public interface Categorizable
{
  default String getCategoryName()
  {
    return this.getClass().getSimpleName();
  }
}
