package com.maxbilbow.aicubo.model;


import com.maxbilbow.aicubo.engine.geometry.Geometry;
import com.maxbilbow.aicubo.engine.geometry.Shape;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RootNode extends Hierarchy<Node>
{

  Map<Shape, Set<Geometry>> getGeometries();

  List<Node> getChildren();

}
