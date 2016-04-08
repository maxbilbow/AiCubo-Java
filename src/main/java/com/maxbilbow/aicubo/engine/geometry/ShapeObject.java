/**
 *
 */
package com.maxbilbow.aicubo.engine.geometry;

/**
 * @author Max
 */
public abstract class ShapeObject implements Shape
{

  protected ShapeObject()
  {
    setInstance(this);
  }

  protected abstract void setInstance(Shape shape);


}
