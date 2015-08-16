package rmx.engine;


import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import rmx.Bugger;
import rmx.RMXObject;

import java.util.HashMap;

@StructClass
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
		this.behaviours.add(behaviour);
		behaviour.setNode(this);
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
		this.children.add(child);
		child.setParent(this);
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
		this.setComponent(Transform.class, this.transform);
	}
	

	public Camera camera() {
		return (Camera) this.getComponent(Camera.class);
	}

	public void setCamera(Camera camera) {
		this.setComponent(Camera.class, camera);
	}
	
	public static Node newCameraNode() {
		Node cameraNode = new Node();
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
	
	public void updateLogic() {
		for (Behaviour behaviour : this.behaviours) {
			if (behaviour.isEnabled())
				behaviour.update();
		}
		for (Node child : this.children)
			child.updateLogic();
		for (Behaviour behaviour : this.behaviours) {
			if (behaviour.isEnabled())
				behaviour.lateUpdate();
		}
	}
	
	public void draw() {
		if (this.geometry() != null) {
			this.geometry().render(this);
		}
		for (Node child : this.children) {
			child.draw();
		}
	}
	
	
	
	public static void test(String[] args) {
		RMXObject o = new RMXObject();
		o.setName("Parent");
		Node o2 = Node.newCameraNode();
		
		try {
			o.sendMessage("getCamera",null);
		
			o2.sendMessage("getCamera",null);
			o.sendMessage("getCamera","Balls");
			o2.sendMessage("getCamera","Balls");
		}catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		if (o2.geometry() == null)
			Bugger.log("That's OK then");
		if (o2.camera() != null)
			Bugger.log("That's OK then");
		
//		o2.addBehaviour(new ABehaviour());
		
		o2.updateLogic();
	}
	
	
	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		if (this.parent != null) {
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
	}

	@Override
	public void broadcastMessage(String message, Object args) {
		super.broadcastMessage(message, args);
		for (NodeComponent c : this.components.values()) {
			c.broadcastMessage(message, args);
		}
		for (Behaviour b : this.behaviours) {
			b.broadcastMessage(message,args);
		}
		
	}
}
