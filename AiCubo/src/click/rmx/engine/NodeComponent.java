package click.rmx.engine;

import click.rmx.RMXObject;

public abstract class NodeComponent extends RMXObject {
	private boolean enabled = true;
	
//	public abstract void update();

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	private Node node;
	public void setNode(Node node) {
		this.node = node;
	}

	public Node getNode() {
		return this.node;
	}
	
	public Transform transform() {
		return this.node.transform;
	}
	
	
	

}
