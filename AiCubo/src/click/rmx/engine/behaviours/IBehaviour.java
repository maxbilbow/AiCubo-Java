package click.rmx.engine.behaviours;

import click.rmx.engine.Node;


public interface IBehaviour {
	public void update();
	public void lateUpdate();
	public void setNode(Node node);
	public boolean isEnabled();

	public void broadcastMessage(String message);


	public void broadcastMessage(String message, Object args);
}