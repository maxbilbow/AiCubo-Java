package click.rmx.engine.behaviours;

import click.rmx.engine.physics.CollisionEvent;

public abstract class CollisionHandler extends Behaviour implements ICollisionHandler {

	public abstract void onCollision(final CollisionEvent e);
	public void update() {};
	
	@Override
	public void broadcastMessage(String message, Object args) {
		if (message == "onCollision")
			this.onCollision((CollisionEvent) args);
		else
			super.broadcastMessage(message, args);
	}
}
