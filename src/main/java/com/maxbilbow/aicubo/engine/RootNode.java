package com.maxbilbow.aicubo.engine;


import com.maxbilbow.aicubo.engine.geometry.Geometry;
import com.maxbilbow.aicubo.engine.geometry.Shape;

import java.util.Map;
import java.util.Set;

public interface RootNode extends Node
{

  Map<Shape, Set<Geometry>> getGeometries();
}
