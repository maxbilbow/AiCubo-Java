package click.rmx.engine.behaviours;

import click.rmx.engine.NodeComponent;

public abstract class Behaviour extends NodeComponent implements IBehaviour {

//	void setNode(Node node);
	
//	public Node getNode();
	public abstract void update();
	
	public void lateUpdate() {
		
	};

//	public boolean isEnabled();
}

