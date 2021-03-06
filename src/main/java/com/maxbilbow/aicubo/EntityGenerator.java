package com.maxbilbow.aicubo;

import click.rmx.engine.Node;
import click.rmx.engine.Scene;
import click.rmx.engine.math.Tools;
public abstract class EntityGenerator {
	public abstract Node makeEntity();
	public int 
	xMin, yMin, zMin,
	xMax, yMax, zMax;
		
	public EntityGenerator() {
		xMax = yMax = zMax =  100;
		xMin = yMin = zMin = -100;
	}
	
	
	private Node initPositionOf(Node node) {
		node.transform().setPosition(
				Tools.rBounds(xMin, xMax),
				Tools.rBounds(yMin, yMax),
				Tools.rBounds(zMin, zMax)
				);
		node.transform().rotate("yaw", (float)Tools.rBounds(0, 360));
		return node;
	}
	public void makeShapesAndAddToScene(Scene scene, int quantity) {
		for (int i = 0; i<quantity; ++i) {
			Node entity = this.makeEntity();
			if (entity != null)
				scene.rootNode().addChild(this.initPositionOf(entity));
		}
	}
}