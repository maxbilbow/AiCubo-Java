package click.rmx.engine;

import java.util.ArrayList;
import java.util.List;

import click.rmx.IRMXObject;
import click.rmx.engine.behaviours.IBehaviour;
import click.rmx.engine.math.Matrix4;
import click.rmx.engine.physics.CollisionBody;
import click.rmx.engine.physics.PhysicsBody;

public interface INode extends IRMXObject {

	void setComponent(Class<?> type, NodeComponent component);

	void addBehaviour(IBehaviour behaviour);

	NodeComponent getComponent(Class<?> type);

	List<INode> getChildren();

	void addChild(Node child);

	boolean removeChildNode(INode node);

	INode getChildWithName(String name);

	Camera camera();

	void setCamera(Camera camera);

	Geometry geometry();

	void setGeometry(Geometry geometry);

	PhysicsBody physicsBody();

	void setPhysicsBody(PhysicsBody body);

	CollisionBody collisionBody();

	void updateLogic(long time);

	void updateAfterPhysics(long time);

	void draw(Matrix4 modelMatrix);

	INode getParent();

	void setParent(Node parent);

	/**
	 * Sends a message to all behaviours and all children of this node.
	 */
	void broadcastMessage(String message);

	/**
	 * Sends a message to all behaviours and all children of this node.
	 */
	void broadcastMessage(String message, Object args);

	boolean sendMessageToBehaviour(Class<?> theBehaviour, String message);

	boolean sendMessageToBehaviour(Class<?> theBehaviour, String message, Object args);

	void addToCurrentScene();

	Transform transform();

}