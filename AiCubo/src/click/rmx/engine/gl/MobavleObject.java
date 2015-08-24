package click.rmx.engine.gl;

import click.rmx.engine.math.Vector3;

abstract class MoveableObject
{
  Tree<Mesh> meshTree;
  public Vector3 pos;
  public Vector3 rot;
}
