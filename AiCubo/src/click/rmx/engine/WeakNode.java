package click.rmx.engine;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import click.rmx.WeakObject;
import click.rmx.engine.behaviours.IBehaviour;
import click.rmx.engine.math.Matrix4;
import click.rmx.engine.physics.CollisionBody;
import click.rmx.engine.physics.PhysicsBody;
import click.rmx.messages.KeyValueObserver;

public class WeakNode extends WeakObject<Node> implements INode {

	public WeakNode() {
		super(new Node());
	}
	
	public WeakNode(Node node) {
		super(node);
	}

	@Override
	public void setComponent(Class<?> type, NodeComponent component) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addBehaviour(IBehaviour behaviour) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public NodeComponent getComponent(Class<?> type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<INode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addChild(Node child) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean removeChildNode(INode node) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public INode getChildWithName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Camera camera() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCamera(Camera camera) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Geometry geometry() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGeometry(Geometry geometry) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PhysicsBody physicsBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPhysicsBody(PhysicsBody body) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CollisionBody collisionBody() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateLogic(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateAfterPhysics(long time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(Matrix4 modelMatrix) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public INode getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(Node parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean sendMessageToBehaviour(Class<?> theBehaviour, String message) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sendMessageToBehaviour(Class<?> theBehaviour, String message, Object args) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void addToCurrentScene() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Transform transform() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
