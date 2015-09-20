package click.rmx.engine;



import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import click.rmx.engine.geometry.Geometry;
import click.rmx.engine.geometry.Shape;

public interface RootNode extends Node {
	Map<Shape, Set<Geometry>> getGeometries();
}
