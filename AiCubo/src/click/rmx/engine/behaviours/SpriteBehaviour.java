package click.rmx.engine.behaviours;

import click.rmx.engine.Behaviour;

public class SpriteBehaviour extends Behaviour {

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	public void jump() {
		this.getNode().transform.move("y:10");
	}
	
}