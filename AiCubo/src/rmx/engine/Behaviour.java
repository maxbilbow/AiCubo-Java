package rmx.engine;

import rmx.Messageable;

public interface Behaviour extends Messageable {

	void setNode(Node node);
	
	public Node getNode();
	public void update();
	
	public void lateUpdate();

	public boolean isEnabled();
}
