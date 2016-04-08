package com.maxbilbow.aicubo.engine.geometry;

//import com.sun.istack.internal.NotNull;


import com.maxbilbow.aicubo.config.Categories;
import com.maxbilbow.aicubo.engine.NodeComponent;
import com.maxbilbow.aicubo.engine.math.Matrix4;
import com.maxbilbow.aicubo.engine.math.Vector3;
import com.maxbilbow.aicubo.engine.math.Vector4;

//@Categorizable(category = Categories.GEOMETRY)
public interface Geometry extends NodeComponent
{

  /**
   * @return a Shape object that must not be changed during the life of the Geometry object
   */
//	@NotNull
  Shape getShape();

//	void setShape(Shape shape);

  default Matrix4 modelMatrix()
  {
    return this.transform().worldMatrix();
  }

  default Vector3 scale()
  {
    return this.transform().scale();
  }

  Vector4 getColor();

  void setColor(Vector4 color);

  default void setColor(int r, int g, int b, int a)
  {
    this.getColor().set(r, g, b, a);
  }

  boolean isVisible();

  void setVisible(boolean visible);


  @Deprecated
  void setShape(Shape shape);

  @Deprecated
  void render();


  default String getCategoryName()
  {
    return Categories.GEOMETRY;
  }


//	{
//		NotificationCenter.getInstance().BroadcastMessage(GEOMETRY_WAS_DESTROYED, null);
//	}

}