package com.maxbilbow.aicubo.model;


import com.maxbilbow.aicubo.engine.geometry.Geometry;
import com.maxbilbow.aicubo.engine.geometry.Shape;
import com.maxbilbow.aicubo.model.Node;

import java.util.Map;
import java.util.Set;

public interface RootNode extends Node
{

  Map<Shape, Set<Geometry>> getGeometries();
}
