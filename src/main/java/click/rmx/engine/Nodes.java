package click.rmx.engine;

import java.util.Collection;
import java.util.stream.Stream;

import click.rmx.engine.behaviours.Behaviour;
import click.rmx.engine.behaviours.CameraBehaviour;
import click.rmx.engine.behaviours.IBehaviour;
import click.rmx.engine.geometry.Shapes;
import click.rmx.engine.math.Tools;
import click.rmx.engine.physics.PhysicsBody;

public final class Nodes {
//	public static Node newWeakNode() {
//		return WeakNode.newInstnance();
//	}
//	
//	public static Node newWeakNode(String name) {
//		WeakNode node = WeakNode.newInstnance();
//		node.setName(name);
//		return node;
//	}
	
	public static Node newGameNode() {
		return GameNode.newInstance();
	}
	
	public static Node newGameNode(String name) {
		GameNode node = GameNode.newInstance();
		node.setName(name);
		return node;
	}
	
	public static RootNode newRootNode() {
		return GameNode.newRootNode();
	}
	
	private static Node current;
	public static void setCurrent(Node n) {
		current = n;
	}
	
	public static Node getCurrent() {
		if (current == null)
			current = Nodes.newGameNode("Player");
		return current;
	}
	
	public static Node newCameraNode() {
		Node cameraNode = Nodes.newGameNode("CameraNode");
		cameraNode.setCamera(new Camera());
		cameraNode.addBehaviour(new CameraBehaviour());
		return cameraNode;
	}
	
	public static Node makeCube(float s, PhysicsBody body, IBehaviour b) {
		Node n = Nodes.newGameNode("Cube");
		n.setGeometry(Shapes.Cube);
		if (body != null)
			n.setPhysicsBody(body);
		n.transform().setScale(s, s, s);
		n.addBehaviour(b);
//		n.addToCurrentScene();
		return n;
	}
	
	public static Node randomAiNode() {
		Stream stream;
		
			@SuppressWarnings("unchecked")
			Collection<Node> nodes =  Scene.getCurrent().rootNode().getChildren();//.clone();

			stream = nodes.stream().filter(n -> {
				return n.getValue(Behaviour.GET_AI_STATE) == null;
			});
		
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
		Object[] arr = stream.toArray();
		return (Node) arr[(int) Tools.rBounds(0, arr.length)];
	}
	
	
}
