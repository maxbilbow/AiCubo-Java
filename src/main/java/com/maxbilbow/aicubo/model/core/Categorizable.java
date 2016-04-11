package com.maxbilbow.aicubo.model.core;


public interface Categorizable
{
  default String getCategoryName()
  {
    return this.getClass().getSimpleName();
  }
}
