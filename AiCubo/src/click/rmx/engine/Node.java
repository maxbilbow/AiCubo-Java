package click.rmx.engine;


import java.util.ArrayList;
import java.util.HashMap;

import click.rmx.RMXObject;
import click.rmx.engine.math.Matrix4;


public class Node extends RMXObject {

	private static Node current;
	
	private Node parent;
	public static void setCurrent(Node node) {
		current = node;
	}
	
//	public static Node getPointOfView() {
//		if (this.pointOfView != null)
//			return this.pointOfView;
//		else
//			this.pointOfView = Node.newCameraNode();
//		return this.pointOfView;
//	}
	public static Node getCurrent() {
		if (current == null)
			current = Node.newCameraNode();
		return current;
	}
	private HashMap<Class<?>,NodeComponent> components = new HashMap<Class<?>,NodeComponent>();
	private ArrayList<Behaviour> behaviours = new ArrayList<Behaviour>();
	
	public void setComponent(Class<?> type, NodeComponent component) {
		this.components.put(type, component);
		component.setNode(this);
	}
	
	public void addBehaviour(Behaviour behaviour) {
		if (behaviour != null) {
			this.behaviours.add(behaviour);
			behaviour.setNode(this);
		}
	}
	
	public NodeComponent getComponent(Class<?> type) {
		return components.getOrDefault(type,null);
	}
	
	public final Transform transform;

	private ArrayList<Node> children = new ArrayList<Node>();
	public ArrayList<Node> getChildren() {
		return this.children;
	}
	
	public void addChild(Node child) {
		if (!this.children.contains(child)) {
			this.children.add(child);
			child.setParent(this);
		}
	}
	
	public boolean removeChildNode(Node node) {
		return this.children.remove(node);
	}
	
	public Node getChildWithName(String name) {
		for (Node child : this.children) {
			if (child.getName() == name)
				return child;
		}
		return null;
	}
	
	public Node(){
		this.transform = new Transform(this);
		
	}
	

	public Node(String name) {
		this.transform = new Transform(this);
		this.setName(name);
	}

	public Camera camera() {
		return (Camera) this.getComponent(Camera.class);
	}

	public void setCamera(Camera camera) {
		this.setComponent(Camera.class, camera);
	}
	
	public static Node newCameraNode() {
		Node cameraNode = new Node("CameraNode");
		cameraNode.setCamera(new Camera());
		return cameraNode;
	}
	
	

	public Geometry geometry() {
		return _geometry;// (Geometry) this.getComponent(Geometry.class);
	}

	private Geometry _geometry;
	public void setGeometry(Geometry geometry) {
		_geometry = geometry;
//		this.setComponent(Geometry.class, geometry);
	}
	
	public PhysicsBody physicsBody(){
		return (PhysicsBody) this.getComponent(PhysicsBody.class);
	}
	
	public void setPhysicsBody(PhysicsBody body) {
		this.setComponent(PhysicsBody.class, body);
	}
	
	public CollisionBody collisionBody(){
		return (CollisionBody) this.getComponent(CollisionBody.class);
	}
	
	public void setCollisionBody(CollisionBody body) {
		this.setComponent(CollisionBody.class, body);
	}
	
	public void updateLogic() {
		for (Behaviour behaviour : this.behaviours) {
			if (behaviour.isEnabled())
				behaviour.update();
		}
		for (Node child : this.children)
			child.updateLogic();
		
	}
	public void updateAfterPhysics() {
		for (Behaviour behaviour : this.behaviours) {
			if (behaviour.isEnabled())
				behaviour.lateUpdate();
		}
	}
	
	public void draw(Matrix4 modelMatrix) {
		if (this.geometry() != null) {
			this.geometry().render(this, modelMatrix);
		}
		for (Node child : this.children) {
			child.draw(modelMatrix);
		}
	}
		
	
	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		if (this.parent != null && parent != this.parent) {
			this.parent.removeChildNode(this);
;		}
		this.parent = parent;
	}
	
	@Override
	public void broadcastMessage(String message) {
		super.broadcastMessage(message);
		for (NodeComponent c : this.components.values()) {
			c.broadcastMessage(message);
		}
		for (Behaviour b : this.behaviours) {
			b.broadcastMessage(message);
		}
		for (Node child : this.children) {
			child.broadcastMessage(message);
		}
	}

	@Override
	public void broadcastMessage(String message, Object args) {
		super.broadcastMessage(message, args);
		for (NodeComponent c : this.components.values()) {
			c.broadcastMessage(message, args);
		}
		for (Behaviour b : this.behaviours) {
			b.broadcastMessage(message, args);
		}
		for (Node child : this.children) {
			child.broadcastMessage(message, args);
		}
	}
	
	public static Node makeCube(float s,boolean body, Behaviour b) {
		Node n = new Node("Cube");
		n.setGeometry(Geometry.cube());
		if (body)
			n.setPhysicsBody(new PhysicsBody());
		n.transform.setScale(s, s, s);
		n.addBehaviour(b);
		n.addToCurrentScene();
		return n;
	}

	private void addToCurrentScene() {
		Scene.getCurrent().rootNode.addChild(this);
	}
}
