package click.rmx.engine;

import java.lang.ref.ReferenceQueue;
import java.util.List;

import static click.rmx.Tests.*;
import click.rmx.WeakObject;
import click.rmx.engine.behaviours.IBehaviour;
import click.rmx.engine.math.Matrix4;

public class WeakNode extends WeakObject<Node> implements Node {

	private WeakNode() {
		super(Nodes.newGameNode());
	}
	
	private WeakNode(Node node) {
		super(node);
	}

	public WeakNode(Node node, ReferenceQueue<? super Node> q) {
		super(node, q);
	}

	
	@Override
	public void setComponent(Class<?> type, NodeComponent component) {
		// TODO Auto-generated method stub
		todo();
		
	}

	@Override
	public void addBehaviour(IBehaviour behaviour) {
		// TODO Auto-generated method stub
		todo();
	}

	@Override
	public NodeComponent getComponent(Class<?> type) {
		// TODO Auto-generated method stub
		todo();
		return null;
	}

	@Override
	public List<Node> getChildren() {
		// TODO Auto-generated method stub
		todo();
		return null;
	}

	@Override
	public void addChild(Node child) {
		// TODO Auto-generated method stub
		todo();
		
	}

	@Override
	public boolean removeChildNode(Node node) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Node getChildWithName(String name) {
		// TODO Auto-generated method stub
		todo();
		return null;
	}

	@Override
	public Camera camera() {
		// TODO Auto-generated method stub
		todo();
		return null;
	}

	

	@Override
	public void updateLogic(long time) {
		// TODO Auto-generated method stub
		todo();
	}

	@Override
	public void updateAfterPhysics(long time) {
		// TODO Auto-generated method stub
		todo();
	}

	@Override
	public void draw(Matrix4 modelMatrix) {
		// TODO Auto-generated method stub
		todo();
	}

	@Override
	public Node getParent() {
		// TODO Auto-generated method stub
		todo();
		return null;
	}

	@Override
	public void setParent(Node parent) {
		// TODO Auto-generated method stub
		todo();
	}

	@Override
	public boolean sendMessageToBehaviour(Class<?> theBehaviour, String message) {
		// TODO Auto-generated method stub
		todo();
		return false;
	}

	@Override
	public boolean sendMessageToBehaviour(Class<?> theBehaviour, String message, Object args) {
		// TODO Auto-generated method stub
		todo();
		return false;
	}

	@Override
	public void addToCurrentScene() {
		todo();
		// TODO Auto-generated method stub
		
	}

	@Override
	public Transform transform() {
		todo();
		// TODO Auto-generated method stub
		return null;
	}

	public static WeakNode newInstnance() {
		todo();
		return new WeakNode(GameNode.newInstance());
	}
	

}
