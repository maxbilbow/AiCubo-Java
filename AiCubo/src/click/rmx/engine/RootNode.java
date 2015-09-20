package click.rmx.engine;



import java.util.Map;
import java.util.Set;

import com.sun.istack.internal.NotNull;

import click.rmx.engine.geometry.Geometry;
import click.rmx.engine.geometry.Shape;

public interface RootNode extends Node {
	@NotNull
	Map<Shape, Set<Geometry>> getGeometries();
}
