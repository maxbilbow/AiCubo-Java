package click.rmx.engine;

import click.rmx.RMXObject;

public abstract class ANodeComponent extends RMXObject implements NodeComponent {
	private boolean enabled = true;
	
//	public abstract void update();

	public ANodeComponent() {
		super();
		this.setName(this.getClass().getName());
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.NodeComponent#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/* (non-Javadoc)
	 * @see click.rmx.engine.NodeComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	private Node node;
	/* (non-Javadoc)
	 * @see click.rmx.engine.NodeComponent#setNode(click.rmx.engine.Node)
	 */
	@Override
	public void setNode(Node node) {
		this.node = node;
		this.onAwake();
	}
	
	/**
	 * Called when node is set. Override for any aditional setup information once assigend to GameNode
	 */
	protected void onAwake(){};

	/* (non-Javadoc)
	 * @see click.rmx.engine.NodeComponent#getNode()
	 */
	@Override
	public Node getNode() {
		return this.node;
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.NodeComponent#transform()
	 */
	@Override
	public Transform transform() {
		return this.node.transform();
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.NodeComponent#getValue(java.lang.String)
	 */
	@Override
	public Object getValue(String forKey) {
		return this.getNode().getValue(forKey);
	}
	
	/* (non-Javadoc)
	 * @see click.rmx.engine.NodeComponent#setValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public Object setValue(String forKey, Object value) {
		return this.getNode().setValue(forKey, value);
	}
	
	
	

}
