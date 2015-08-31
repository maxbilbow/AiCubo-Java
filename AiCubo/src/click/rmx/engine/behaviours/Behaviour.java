package click.rmx.engine.behaviours;

import click.rmx.engine.Node;
import click.rmx.engine.NodeComponent;
import click.rmx.engine.Transform;
import click.rmx.engine.math.Vector3;

public abstract class Behaviour extends NodeComponent implements IBehaviour {
	public static final String 
	AI_STATE_NULL = "AI_STATE_NULL",
	AI_STATE_POSSESSED = "AI_STATE_POSSESSED";
//	void setNode(Node node);
	public final static String 
	GET_AI_STATE = "KEY_AI_STATE";
	public String state() {
		Object i = this.getNode().getValue(GET_AI_STATE);
		return i == null ? AI_STATE_NULL : String.valueOf(i);
	}
	public void setState(String state) {
		this.setValue(GET_AI_STATE, state);		
	}


	
//	public Node getNode();
	public void setDefaultState() {
		this.setState(AI_STATE_NULL);
	}
	
	protected long interval() {
		return 800;
	}
	
	protected abstract void onAwake();
	
	/**
	 * Overriden so that multiple behaviours can share the same variables
	 */
	public Object getValue(String forKey) {
		return this.getNode().getValue(forKey);
	}
	
	/**
	 * Overriden so that multiple behaviours can share the same variables
	 */
	public Object setValue(String forKey, Object value) {
		return this.getNode().setValue(forKey, value);
	}
	
//	public abstract void update(long tick);
	
	public void lateUpdate() {
		
	};
	
	public void setNode(Node node) {
		super.setNode(node);
		this.onAwake();
	}

	public void turnToFace(Vector3 position) {
		// TODO Auto-generated method stub
	}
//	public boolean isEnabled();
}

