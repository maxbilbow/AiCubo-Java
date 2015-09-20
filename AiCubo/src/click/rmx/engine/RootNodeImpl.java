package click.rmx.engine;

import static click.rmx.RMX.rmxTodo;
import static click.rmx.RMX.rmxObjectCount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import click.rmx.Categories;
import click.rmx.RMX;
import click.rmx.Tests;
import click.rmx.engine.geometry.Geometry;
import click.rmx.engine.geometry.Shape;

public class RootNodeImpl extends GameNode implements RootNode {

	private Set<Geometry> geometries = new HashSet<>();
	private Map<Shape, Set<Geometry>> shapeMap = new HashMap<>();
	int count = -1;
	
	@Override
	public Map<Shape, Set<Geometry>> getGeometries() {
		int count = rmxObjectCount(Categories.GEOMETRY);
		if (this.count != count) {
			this.addGeometryToList(geometries);
			this.count = count;
//			Tests.note(Categories.GEOMETRY + ": " + count + " found");
		}
		geometries.forEach(geo -> {
			Set<Geometry> set = shapeMap.get(geo.getShape());
			if (set == null) {
				set = new HashSet<>();
				shapeMap.put(geo.getShape(), set);
			}
			set.add(geo);
		});
		return shapeMap;
	}



	private HashMap<Shape, Stream<Geometry>> children = null;
	private RootNodeImpl(){
		super();
		this.setName("rootNode");
	}



	public static RootNodeImpl newInstance() {
		return new RootNodeImpl();
	}



}
