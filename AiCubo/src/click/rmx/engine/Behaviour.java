package click.rmx.engine;



public abstract class Behaviour extends NodeComponent {

//	void setNode(Node node);
	
//	public Node getNode();
	public abstract void update();
	
	public void lateUpdate() {
		
	};

//	public boolean isEnabled();
}

