package click.rmx.engine.behaviours;

import click.rmx.engine.INode;

@FunctionalInterface
public interface IBehaviour {
//	public default void lateUpdate() ;
	
//	public default void setNode(Node node) {}
	
	public default boolean isEnabled() {
		return true; 
	}
	
	public default boolean hasLateUpdate() {
		return false;
	}

	public default String getName() {
		return "Anonomous: " + getClass().getName();
	}
	
	public default void broadcastMessage(String message){}


	public default void broadcastMessage(String message, Object args) {}
	
	void update(INode node);
}
