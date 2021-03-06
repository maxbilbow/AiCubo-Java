package click.rmx.engine;

import java.util.List;
import java.util.Set;

import click.rmx.IRMXObject;
import click.rmx.engine.behaviours.IBehaviour;
import click.rmx.engine.geometry.Geometry;
import click.rmx.engine.geometry.Shape;
import click.rmx.engine.math.Matrix4;
import click.rmx.engine.physics.CollisionBody;
import click.rmx.engine.physics.PhysicsBody;

public interface Node extends IRMXObject {

	void setComponent(Class<?> type, NodeComponent component);

	void addBehaviour(IBehaviour behaviour);

	NodeComponent getComponent(Class<?> type);

	List<Node> getChildren();

	void addChild(Node child);

	boolean removeChildNode(Node node);

	Node getChildWithName(String name);

	default Camera camera() {
		return (Camera) this.getComponent(Camera.class);
	}

	default void setCamera(Camera camera) {
		this.setComponent(Camera.class, camera);
	}

	
	default Geometry geometry() {
		return (Geometry) this.getComponent(Geometry.class);
	}

	default void setGeometry(Shape shape) {
		this.setComponent(Geometry.class, shape.newGeometry());
	}


	default PhysicsBody physicsBody(){
		return (PhysicsBody) this.getComponent(PhysicsBody.class);
	}
	
	default void setPhysicsBody(PhysicsBody body) {
		this.setComponent(PhysicsBody.class, body);
	}
	
	default CollisionBody collisionBody() {
		PhysicsBody body = this.physicsBody();
		if (body != null)
			return body.getCollisionBody();
		else
			return null;
	}
	

	default LightSource getLight() {
		return (LightSource) this.getComponent(LightSource.class);
	}
	
	default void setLightSource(LightSource light) {
		this.setComponent(LightSource.class, light);
	}
	
	void updateLogic(long time);

	void updateAfterPhysics(long time);

	void draw(Matrix4 viewMatrix);

	Node getParent();

	void setParent(Node parent);

	default void shine() {
		LightSource light = this.getLight();
		if (light != null)
			light.shine();
	}
	
	/**
	 * Sends a message to all behaviours and all children of this node.
	 */
	@Override
	void broadcastMessage(String message);

	/**
	 * Sends a message to all behaviours and all children of this node.
	 */
	@Override
	void broadcastMessage(String message, Object args);

	boolean sendMessageToBehaviour(Class<?> theBehaviour, String message);

	boolean sendMessageToBehaviour(Class<?> theBehaviour, String message, Object args);

	void addToCurrentScene();

	Transform transform();

	default void addGeometryToList(Set<Geometry> geometries) {
		if (this.geometry() != null && this.geometry().isVisible())
			geometries.add(this.geometry());
		this.getChildren().stream().forEach(child -> child.addGeometryToList(geometries));
	}
}