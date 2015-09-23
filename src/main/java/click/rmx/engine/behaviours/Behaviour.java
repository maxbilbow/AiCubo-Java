package click.rmx.engine.behaviours;

import click.rmx.engine.ANodeComponent;
import click.rmx.engine.math.Vector3;

public abstract class Behaviour extends ANodeComponent implements IBehaviour {
	public static final String 
	AI_STATE_NULL = "AI_STATE_NULL",
	AI_STATE_POSSESSED = "AI_STATE_POSSESSED";
	//	void setNode(Node node);
	public final static String 
	GET_AI_STATE = "KEY_AI_STATE";

//	@Deprecated
//	@Warning("Behaviours no longer guarenteed to hold nodes."
//			+ "\nUse node.getState instead")
//	public String state() {
//		return this.state(getNode());
//	}

	
	public String state() {
		if (getNode() != null) {
			Object i = getNode().getValue(GET_AI_STATE);
			return i == null ? AI_STATE_NULL : String.valueOf(i);
		}
		return AI_STATE_NULL;
	}

//		@Deprecated
//	@Warning("Behaviours no longer guarenteed to hold nodes."
//			+ "\nUse node.setState instead")
//	public void setState(String state) {
//		this.setState(getNode(), state);
//	}
	public void setState(String state) {//, String state) {
		this.getNode().setValue(GET_AI_STATE, state);		
	}

	@Override
	public abstract boolean hasLateUpdate();

	//	public Node getNode();
	public void setDefaultState() {
		this.setState(AI_STATE_NULL);
	}

	protected long interval() {
		return 800;
	}

	@Override
	protected abstract void onAwake();

	//	public abstract void update(long tick);

	public void lateUpdate() {

	};

	public void turnToFace(Vector3 position) {
		// TODO Auto-generated method stub
	}
	//	public boolean isEnabled();
	
//	public void broadcastMessage(String message){
//		super.broadcastMessage(message);
//	}
//
//
//	public void broadcastMessage(String message, Object args) {
//		super.broadcastMessage(message, args);
//	}
}

