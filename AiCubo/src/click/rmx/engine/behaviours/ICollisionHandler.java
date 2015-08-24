package click.rmx.engine.behaviours;

import click.rmx.engine.physics.CollisionEvent;

public interface ICollisionHandler {
	public void onCollision(CollisionEvent event);
}
