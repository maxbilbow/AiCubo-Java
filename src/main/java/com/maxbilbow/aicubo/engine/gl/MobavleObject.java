package com.maxbilbow.aicubo.engine.gl;

import com.maxbilbow.aicubo.engine.math.Vector3;

abstract class MoveableObject
{
  Tree<Mesh> meshTree;
  public Vector3 pos;
  public Vector3 rot;
}
