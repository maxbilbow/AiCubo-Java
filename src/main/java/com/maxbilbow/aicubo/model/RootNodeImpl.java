package com.maxbilbow.aicubo.model;


import com.maxbilbow.aicubo.config.Categories;
import com.maxbilbow.aicubo.engine.geometry.Geometry;
import com.maxbilbow.aicubo.engine.geometry.GeometryImpl;
import com.maxbilbow.aicubo.engine.geometry.Shape;
import com.maxbilbow.aicubo.model.core.RMXObject;

import java.util.*;

import static com.maxbilbow.aicubo.config.RMX.rmxObjectCount;


public class RootNodeImpl extends RMXObject implements RootNode
{


  private Set<Geometry> geometries = new HashSet<>();


  private Map<Shape, Set<Geometry>> shapeMap;// = new HashMap<>();

  int count = -1;
  private ArrayList<Node> mChildren = new ArrayList<>();

  @Override
  public void onEventDidEnd(String theEvent, Object args)
  {
    if (theEvent == GeometryImpl.GEOMETRY_WAS_DESTROYED)
    {
      this.geometries = null;
      this.shapeMap = null;
      this.count = -1;
    }

    super.onEventDidEnd(theEvent, args);
  }

  @Override
  public Map<Shape, Set<Geometry>> getGeometries()
  {
    int count = rmxObjectCount(Categories.GEOMETRY);

    if (shapeMap == null || this.count > count)
    {
      geometries = new HashSet<>();
      shapeMap = new HashMap<>();
    }

    if (this.count != count)
    {
      mChildren.stream().forEach(child -> child.addGeometryToList(geometries));
      this.count = count;
    }

    geometries.forEach(geo -> {
      Set<Geometry> set = shapeMap.get(geo.getShape());
      if (set == null)
      {
        set = new HashSet<>();
        shapeMap.put(geo.getShape(), set);
      }
      set.add(geo);
    });
    return shapeMap;
  }

  private RootNodeImpl()
  {
    super();
    this.setName("mRootNode");
  }


  public static RootNodeImpl newInstance()
  {
    return new RootNodeImpl();
  }


  /* (non-Javadoc)
   * @see Node#addChild(Node)
   */
  @Override
  public void addChild(Node child)
  {
    if (!this.mChildren.contains(child))
    {
      this.mChildren.add(child);
      child.setParent(this);
    }
  }

  @Override
  public List<Node> getChildren()
  {
    return mChildren;
  }

  @Override
  public void updateLogic()
  {
    mChildren.stream().forEach(Node::updateLogic);
  }

  @Override
  public void updateAfterPhysics()
  {
    mChildren.stream().forEach(Node::updateAfterPhysics);
  }

  @Override
  public boolean removeChild(Node aChild)
  {
    return mChildren.remove(aChild);
  }
}
