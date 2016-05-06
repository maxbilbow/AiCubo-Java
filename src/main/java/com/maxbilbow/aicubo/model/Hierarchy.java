package com.maxbilbow.aicubo.model;

/**
 * Created by Max on 06/05/2016.
 */
public interface Hierarchy<T>
{
  void addChild(T aChild);

  boolean removeChild(T aChild);
}
