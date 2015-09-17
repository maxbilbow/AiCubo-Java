package click.rmx.engine;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import click.rmx.RMXObject;
import click.rmx.engine.behaviours.Behaviour;
import click.rmx.engine.behaviours.CameraBehaviour;
import click.rmx.engine.behaviours.IBehaviour;
import click.rmx.engine.math.Matrix4;
import click.rmx.engine.math.Tools;
import click.rmx.engine.physics.CollisionBody;
import click.rmx.engine.physics.PhysicsBody;


public class Node extends RMXObject implements INode {

	private static INode current;
	
	private Node parent;
	public static void setCurrent(INode n) {
		current = n;
	}
	
//	public static Node getPointOfView() {
//		if (this.pointOfView != null)
//			return this.pointOfView;
//		else
//			this.pointOfView = Node.newCameraNode();
//		return this.pointOfView;
//	}
	public static INode getCurrent() {
		if (current == null)
			current = new Node("Player");
		return current;
	}
	private HashMap<Class<?>,NodeComponent> components = new HashMap<Class<?>,NodeComponent>();
	private ArrayList<IBehaviour> behaviours = new ArrayList<IBehaviour>();
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#setComponent(java.lang.Class, click.rmx.engine.NodeComponent)
	 */
	@Override
	public void setComponent(Class<?> type, NodeComponent component) {
		this.components.put(type, component);
		component.setNode(this);
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#addBehaviour(click.rmx.engine.behaviours.IBehaviour)
	 */
	@Override
	public void addBehaviour(IBehaviour behaviour) {
		if (behaviour != null) {
			for (IBehaviour b : this.behaviours) {
				if (!b.getName().isEmpty() && b.getName() == behaviour.getName()) {
					System.err.println("Behaviour: " + b.getName() + " was already added.");
					return;
				}
			}
			this.behaviours.add(behaviour);
			behaviour.broadcastMessage("setNode",this);//.setNode(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#getComponent(java.lang.Class)
	 */
	@Override
	public NodeComponent getComponent(Class<?> type) {
		return components.getOrDefault(type,null);
	}
	
	private final Transform transform;

	private ArrayList<INode> children = new ArrayList<INode>();
	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#getChildren()
	 */
	@Override
	public List<INode> getChildren() {
		return this.children;
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#addChild(click.rmx.engine.Node)
	 */
	@Override
	public void addChild(Node child) {
		if (!this.children.contains(child)) {
			this.children.add(child);
			child.setParent(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#removeChildNode(click.rmx.engine.INode)
	 */
	@Override
	public boolean removeChildNode(INode node) {
		return this.children.remove(node);
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#getChildWithName(java.lang.String)
	 */
	@Override
	public INode getChildWithName(String name) {
		for (INode child : this.children) {
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

	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#camera()
	 */
	@Override
	public Camera camera() {
		return (Camera) this.getComponent(Camera.class);
	}

	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#setCamera(click.rmx.engine.Camera)
	 */
	@Override
	public void setCamera(Camera camera) {
		this.setComponent(Camera.class, camera);
	}
	
	public static Node newCameraNode() {
		Node cameraNode = new Node("CameraNode");
		cameraNode.setCamera(new Camera());
		cameraNode.addBehaviour(new CameraBehaviour());
		return cameraNode;
	}
	
	

	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#geometry()
	 */
	@Override
	public Geometry geometry() {
		return _geometry;// (Geometry) this.getComponent(Geometry.class);
	}

	private Geometry _geometry;
	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#setGeometry(click.rmx.engine.Geometry)
	 */
	@Override
	public void setGeometry(Geometry geometry) {
		_geometry = geometry;
//		this.setComponent(Geometry.class, geometry);
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#physicsBody()
	 */
	@Override
	public PhysicsBody physicsBody(){
		return (PhysicsBody) this.getComponent(PhysicsBody.class);
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#setPhysicsBody(click.rmx.engine.physics.PhysicsBody)
	 */
	@Override
	public void setPhysicsBody(PhysicsBody body) {
		this.setComponent(PhysicsBody.class, body);
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#collisionBody()
	 */
	@Override
	public CollisionBody collisionBody() {
		PhysicsBody body = this.physicsBody();
		if (body != null)
			return body.getCollisionBody();
		else
			return null;
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#updateLogic(long)
	 */
	@Override
	public void updateLogic(long time) {
		
		this.behaviours.stream().forEach(behaviour -> {
			if (behaviour.isEnabled())
				behaviour.update(this);
		});
		
		this.children.stream().forEach(child -> {
			child.updateLogic(time);
		});
		
		
	}
	private long _timeStamp = -1;
	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#updateAfterPhysics(long)
	 */
	@Override
	public void updateAfterPhysics(long time) {
		this.behaviours.stream().forEach(behaviour -> {
			if (behaviour.hasLateUpdate())
				behaviour.broadcastMessage("lateUpdate");
		});
		
		for (INode child: this.children)
			child.updateAfterPhysics(time);
		this.transform.updateLastPosition();
//		this.updateTick(time);
		///.set(arg0);
		_timeStamp = time;
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#draw(click.rmx.engine.math.Matrix4)
	 */
	@Override
	public void draw(Matrix4 modelMatrix) {
		if (this.geometry() != null) {
			this.geometry().render(this);//, modelMatrix);
		}
		LightSource light = (LightSource) this.getComponent(LightSource.class);
		if (light != null)
			light.shine();
		for (INode child : this.children) {
			child.draw(modelMatrix);
		}
	}
		
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#getParent()
	 */
	@Override
	public Node getParent() {
		return parent;
	}

	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#setParent(click.rmx.engine.Node)
	 */
	@Override
	public void setParent(Node parent) {
		if (this.parent != null && parent != this.parent) {
			this.parent.removeChildNode(this);
;		}
		this.parent = parent;
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#broadcastMessage(java.lang.String)
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
		for (INode child : this.children) {
			child.broadcastMessage(message);
		}
	}

	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#broadcastMessage(java.lang.String, java.lang.Object)
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
		for (INode child : this.children) {
			child.broadcastMessage(message, args);
		}
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#sendMessageToBehaviour(java.lang.Class, java.lang.String)
	 */
	@Override
	public boolean sendMessageToBehaviour(Class<?> theBehaviour, String message) {
		return this.sendMessageToBehaviour(theBehaviour, message, null);
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#sendMessageToBehaviour(java.lang.Class, java.lang.String, java.lang.Object)
	 */
	@Override
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
	
	public static Node makeCube(float s,PhysicsBody body, IBehaviour b) {
		Node n = new Node("Cube");
		n.setGeometry(Geometry.cube());
		if (body != null)
			n.setPhysicsBody(body);
		n.transform.setScale(s, s, s);
		n.addBehaviour(b);
//		n.addToCurrentScene();
		return n;
	}

	
	/* (non-Javadoc)
	 * @see click.rmx.engine.INode#addToCurrentScene()
	 */
	@Override
	public void addToCurrentScene() {
		Scene.getCurrent().rootNode.addChild(this);
	}

//	private long tick = System.currentTimeMillis();
	
//	@Override
//	public long tick() {
//		// TODO Auto-generated method stub
//		return this.tick;
//	}

//	@Override
//	public void updateTick(long newTick) {
//		this.tick = newTick;
//	}

	public static INode randomAiNode() {
		Stream stream;
		{
			@SuppressWarnings("unchecked")
			Collection<INode> nodes =  (Collection<INode>) Scene.getCurrent().rootNode.getChildren();//.clone();

			stream = nodes.stream().filter(n -> {
				return n.getValue(Behaviour.GET_AI_STATE) == null;
			});
		}
//		nodes.stream()
//		nodes.removeIf(new Predicate<Node>() {
//
//			@Override
//			public boolean test(Node t) {
//				
//				return t.getValue(Behaviour.GET_AI_STATE) == null;
//			}
//			
//		});
		INode[] nodes = (INode[]) stream.toArray();
		return nodes[(int) Tools.rBounds(0, nodes.length)];
	}

	@Override
	public Transform transform() {
		return this.transform;
	}
}
