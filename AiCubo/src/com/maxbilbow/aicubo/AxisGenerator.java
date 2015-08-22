package com.maxbilbow.aicubo;

import click.rmx.engine.Node;
import click.rmx.engine.PhysicsBody;

public class AxisGenerator extends EntityGenerator {

	public float size = 1000.0f; 
	
	public AxisGenerator(float size) {
		this.size = size;
		xMin = xMax = yMin = yMax = zMin = zMax = 0;
	}
	
	@Override
	public Node makeEntity() {
		Node X = Node.makeCube(1, PhysicsBody.newStaticBody(), null);
		Node Y = Node.makeCube(1, PhysicsBody.newStaticBody(), null);
		Node Z = Node.makeCube(1, PhysicsBody.newStaticBody(), null);
		X.transform.setScale(size, 1.0f, 1.0f);
		Y.transform.setScale(1.0f, size, 1.0f);
		Z.transform.setScale(1.0f, 1.0f, size);
		
		Node axis = new Node();
		axis.addChild(X);
		axis.addChild(Y);
		axis.addChild(Z);
		return axis;
	}

}