package click.rmx.engine;

public interface CollisionDelegate {
	public void handleCollision(Node nodeA, Node nodeB, CollisionEvent eventData);
}