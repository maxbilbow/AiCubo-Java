package click.rmx.engine;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import click.rmx.RMXObject;
import click.rmx.engine.behaviours.IBehaviour;
import click.rmx.engine.math.Matrix4;


public class GameNode extends RMXObject implements Node {

	
	private Node parent;

	
	private final HashMap<Class<?>,NodeComponent> components = new HashMap<>();
	private final Set<IBehaviour> behaviours = new HashSet<>();
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.Node#setComponent(java.lang.Class, click.rmx.engine.NodeComponent)
	 */
	@Override
	public void setComponent(Class<?> type, NodeComponent component) {
		this.components.put(type, component);
		component.setNode(this);
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.Node#addBehaviour(click.rmx.engine.behaviours.IBehaviour)
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
//			behaviour.broadcastMessage("setNode",this);
			behaviour.setNode(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.Node#getComponent(java.lang.Class)
	 */
	@Override
	public NodeComponent getComponent(Class<?> type) {
		return components.getOrDefault(type,null);
	}
	
	private final Transform transform;

	private ArrayList<Node> children = new ArrayList<Node>();
	/* (non-Javadoc)
	 * @see click.rmx.engine.Node#getChildren()
	 */
	@Override
	public List<Node> getChildren() {
		return this.children;
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.Node#addChild(click.rmx.engine.Node)
	 */
	@Override
	public void addChild(Node child) {
		if (!this.children.contains(child)) {
			this.children.add(child);
			child.setParent(this);
		}
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.Node#removeChildNode(click.rmx.engine.Node)
	 */
	@Override
	public boolean removeChildNode(Node node) {
		return this.children.remove(node);
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.Node#getChildWithName(java.lang.String)
	 */
	@Override
	public Node getChildWithName(String name) {
		for (Node child : this.children) {
			if (child.getName() == name)
				return child;
		}
		return null;
	}
	
	protected GameNode(){
		this.transform = new Transform(this);
		
	}

	
	/* (non-Javadoc)
	 * @see click.rmx.engine.Node#updateLogic(long)
	 */
	@Override
	public void updateLogic(long time) {
		
		Stream<IBehaviour> behaviours = this.behaviours.stream();
		behaviours.forEach(behaviour -> {
			if (behaviour.isEnabled())
				behaviour.update(this);
		});
//		behaviours.close();		
		
		Stream<Node> children = this.children.stream();
		children.forEach(child -> {
			child.updateLogic(time);
		});
		
	
	}

	private long _timeStamp = -1;
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.Node#updateAfterPhysics(long)
	 */
	@Override
	public void updateAfterPhysics(long time) {
		this.behaviours.stream().forEach(behaviour -> {
			if (behaviour.hasLateUpdate())
				behaviour.broadcastMessage("lateUpdate");
		});
		
		for (Node child: this.children)
			child.updateAfterPhysics(time);
		this.transform.updateLastPosition();
//		this.updateTick(time);
		///.set(arg0);
		_timeStamp = time;
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.Node#draw(click.rmx.engine.math.Matrix4)
	 */
	@Override
	public void draw(Matrix4 viewMatrix) {
		if (this.geometry() != null) 
			this.geometry().render();//, modelMatrix);
		for (Node child : this.children) 
			child.draw(viewMatrix);
	}
	
	
		
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.Node#getParent()
	 */
	@Override
	public Node getParent() {
		return parent;
	}

	/* (non-Javadoc)
	 * @see click.rmx.engine.Node#setParent(click.rmx.engine.Node)
	 */
	@Override
	public void setParent(Node parent) {
		if (this.parent != null && parent != this.parent) {
			this.parent.removeChildNode(this);
;		}
		this.parent = parent;
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.Node#broadcastMessage(java.lang.String)
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

	/* (non-Javadoc)
	 * @see click.rmx.engine.Node#broadcastMessage(java.lang.String, java.lang.Object)
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
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.Node#sendMessageToBehaviour(java.lang.Class, java.lang.String)
	 */
	@Override
	public boolean sendMessageToBehaviour(Class<?> theBehaviour, String message) {
		return this.sendMessageToBehaviour(theBehaviour, message, null);
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.Node#sendMessageToBehaviour(java.lang.Class, java.lang.String, java.lang.Object)
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
	
	

	
	/* (non-Javadoc)
	 * @see click.rmx.engine.Node#addToCurrentScene()
	 */
	@Override
	public void addToCurrentScene() {
		Scene.getCurrent().rootNode().addChild(this);
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

	

	@Override
	public Transform transform() {
		return this.transform;
	}

	
	public static GameNode newInstance() {
		return new GameNode();
	}
	

	public static RootNode newRootNode() {
		return RootNodeImpl.newInstance();
	}
	
	
	
}



