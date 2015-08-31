package click.rmx.engine;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.function.Predicate;

import click.rmx.RMXObject;
import click.rmx.engine.behaviours.Behaviour;
import click.rmx.engine.behaviours.CameraBehaviour;
import click.rmx.engine.behaviours.IBehaviour;
import click.rmx.engine.math.Matrix4;
import click.rmx.engine.math.Tools;
import click.rmx.engine.physics.CollisionBody;
import click.rmx.engine.physics.PhysicsBody;


public class Node extends RMXObject implements Ticker{

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
			current = new Node("Player");
		return current;
	}
	private HashMap<Class<?>,NodeComponent> components = new HashMap<Class<?>,NodeComponent>();
	private ArrayList<IBehaviour> behaviours = new ArrayList<IBehaviour>();
	
	public void setComponent(Class<?> type, NodeComponent component) {
		this.components.put(type, component);
		component.setNode(this);
	}
	
	public void addBehaviour(IBehaviour behaviour) {
		if (behaviour != null) {
			for (IBehaviour b : this.behaviours) {
				if (!b.getName().isEmpty() && b.getName() == behaviour.getName()) {
					System.err.println("Behaviour: " + b.getName() + " was already added.");
					return;
				}
			}
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
		cameraNode.addBehaviour(new CameraBehaviour());
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
	
	public CollisionBody collisionBody() {
		PhysicsBody body = this.physicsBody();
		if (body != null)
			return body.getCollisionBody();
		else
			return null;
	}
	
	public void updateLogic(long time) {
		
		for (IBehaviour behaviour : this.behaviours) {
			if (behaviour.isEnabled())
				behaviour.update(time);
		}
		for (Node child : this.children)
			child.updateLogic(time);
		
	}
	public void updateAfterPhysics(long time) {
		for (IBehaviour behaviour : this.behaviours) {
			if (behaviour.isEnabled())
				behaviour.lateUpdate();
		}
		for (Node child: this.children)
			child.updateAfterPhysics(time);
		this.transform.updateLastPosition();
		this.updateTick(time);
		///.set(arg0);
	}
	
	public void draw(Matrix4 modelMatrix) {
		if (this.geometry() != null) {
			this.geometry().render(this);//, modelMatrix);
		}
		LightSource light = (LightSource) this.getComponent(LightSource.class);
		if (light != null)
			light.shine();
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
	
	/**
	 * Sends a message to all behaviours and all children of this node.
	 */
	@Override
	public void broadcastMessage(String message) {
		super.broadcastMessage(message);
//		for (NodeComponent c : this.components.values()) {
//			c.broadcastMessage(message);
//		}
		for (IBehaviour b : this.behaviours) {
			b.broadcastMessage(message);
		}
		for (Node child : this.children) {
			child.broadcastMessage(message);
		}
	}

	/**
	 * Sends a message to all behaviours and all children of this node.
	 */
	@Override
	public void broadcastMessage(String message, Object args) {
		super.broadcastMessage(message, args);
		for (NodeComponent c : this.components.values()) {
			c.broadcastMessage(message, args);
		}
		for (IBehaviour b : this.behaviours) {
			b.broadcastMessage(message, args);
		}
		for (Node child : this.children) {
			child.broadcastMessage(message, args);
		}
	}
	
	public boolean sendMessageToBehaviour(Class<?> theBehaviour, String message) {
		return this.sendMessageToBehaviour(theBehaviour, message, null);
	}
	
	public boolean sendMessageToBehaviour(Class<?> theBehaviour, String message, Object args) {
		for (IBehaviour b : this.behaviours) {
			if (b.getClass().equals(theBehaviour)) {
				if (args != null)
					b.broadcastMessage(message, args);
				else
					b.broadcastMessage(message);
				return true;
			}
		}
		return false;
	}
	
	public static Node makeCube(float s,PhysicsBody body, Behaviour b) {
		Node n = new Node("Cube");
		n.setGeometry(Geometry.cube());
		if (body != null)
			n.setPhysicsBody(body);
		n.transform.setScale(s, s, s);
		n.addBehaviour(b);
//		n.addToCurrentScene();
		return n;
	}

	
	public void addToCurrentScene() {
		Scene.getCurrent().rootNode.addChild(this);
	}

	private long tick = System.currentTimeMillis();
	
	@Override
	public long tick() {
		// TODO Auto-generated method stub
		return this.tick;
	}

	@Override
	public void updateTick(long newTick) {
		this.tick = newTick;
	}

	public static Node randomAiNode() {
		@SuppressWarnings("unchecked")
		ArrayList<Node> nodes =  (ArrayList<Node>) Scene.getCurrent().rootNode.getChildren().clone();
		
		nodes.removeIf(new Predicate<Node>() {

			@Override
			public boolean test(Node t) {
				
				return t.getValue(Behaviour.GET_AI_STATE) == null;
			}
			
		});
		
		return nodes.get((int) Tools.rBounds(0, nodes.size()));
	}
}
