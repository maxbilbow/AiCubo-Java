package click.rmx.engine.behaviours;

import click.rmx.engine.Node;

public class CameraBehaviour extends Behaviour {

	@Override
	public void update(Node node) {
		// TODO Auto-generated method stub

	}

	
	public void lookUp(Float r) {
		this.getNode().transform().rotate("pitch", r);
	}
	
	public void lookLeft(Float r) {
		this.getNode().transform().rotate("yaw", r);
	}


	@Override
	protected void onAwake() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean hasLateUpdate() {
		return false;
	}
}
