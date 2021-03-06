package com.maxbilbow.aicubo;

import static org.junit.Assert.*;
import static click.rmx.RMX.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;

import click.rmx.engine.GameNode;
import click.rmx.engine.geometry.Shapes;
import org.junit.Before;
import org.junit.Test;
//import org.lwjgl.glfw.GLFWKeyCallback;
//import static org.lwjgl.glfw.GLFW.*;

import click.rmx.Bugger;
import click.rmx.engine.GameController;
import click.rmx.engine.Node;
import click.rmx.engine.Scene;
import click.rmx.engine.gl.IKeyCallback;
import click.rmx.engine.math.Vector3;
import click.rmx.engine.physics.CollisionDelegate;
import click.rmx.engine.physics.CollisionEvent;
import click.rmx.engine.physics.PhysicsBody;

public class AiCuboCollisionTest implements CollisionDelegate {
	AiCubo game;
	Scene scene;
	Node player, cameraNode, box, front, back, left, right, top, bottom;
	float scale = 100;

	@Before
	public void setUp() throws Exception {
		Bugger.debug = false;
		Bugger.logging  = false;
		game = AiCubo.isInitialized()? (AiCubo) GameController.getInstance() : new AiCubo();
		scene = Scene.getCurrent();
		player = game.player;
		cameraNode = game.cameraNode;
		scene.getPhysicsWorld().setCollisionDelegate(this);
		player.setName("Player");
		player.transform().setPosition(0, scale, -scale / 2);

		front = buildEntity("z+");
		back = buildEntity("z-");
		left = buildEntity("x+");
		right = buildEntity("x-");
		top = buildEntity("y+");
		bottom = buildEntity("y-");


		front.transform().setPosition(0, 0, scale);
		back.transform().setPosition(0, 0, -scale);
		left.transform().setPosition(scale, 0, 0);
		right.transform().setPosition(-scale, 0, 0);
		top.transform().setPosition(0, scale, 0);
		bottom.transform().setPosition(0,   -scale,0);

		box = GameNode.newInstance();//.makeCube(scale / 10, PhysicsBody.newStaticBody(), null);
		box.setGeometry(Shapes.Cube);
		box.setName("Box");
		scene.rootNode().addChild(box);
		scene.getPhysicsWorld().setGravity(0, 0, 0);

		actor = front;
		game.addKeyCallback(new IKeyCallback() {

			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (action == GLFW_RELEASE) {
					switch (key) {
					case GLFW_KEY_TAB:
						switchActor();
						System.out.println("Now Controlling " + actor.getName());
						break;
					case GLFW_KEY_EQUAL:
						actor.physicsBody().applyForce(
								0.5f, 
								Vector3.makeSubtraction(box.transform().position(), actor.transform().position()).getNormalized(),
								Vector3.Zero);
						break;
					case GLFW_KEY_MINUS:
						actor.physicsBody().applyForce(
								-0.5f, 
								Vector3.makeSubtraction(box.transform().position(), actor.transform().position()).getNormalized(),
								Vector3.Zero);
						break;
					case GLFW_KEY_R:
						actor.transform().rotate("z", 90 * PI_OVER_180);
						break;
					case GLFW_KEY_T:
						actor.transform().rotate("x", 90 * PI_OVER_180);
						break;
					case GLFW_KEY_Y:
						actor.transform().rotate("y", 90 * PI_OVER_180);
						break;
					}
//					System.out.println(actor.getName() + " At Angle: " + actor.transform.eulerAngles());
				}
			}

		});


	}
	Node actor;
	void switchActor() {
		if (actor == front) 
			actor = back;
		else if (actor == back)
			actor = left;
		else if (actor == left)
			actor = right;
		else if (actor == right)
			actor = top;
		else if (actor == top)
			actor = bottom;
		else if (actor == bottom)
			actor = front;
	}

	Node buildEntity(String name) {
		Node n = Node.makeCube(2, PhysicsBody.newDynamicBody(), null);
		n.setName(name);
		n.physicsBody().setFriction(0.05f);
		n.physicsBody().setDamping(0);
		scene.rootNode.addChild(n);
		return n;
	}

	@Test
	public void test() {
		//		try {
		//			setUp();
		//		} catch (Exception e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}

		assertTrue(cameraNode == game.getView().pointOfView());
		assertTrue(scene == Scene.getCurrent());
		assertTrue(player == Node.getCurrent());
		game.run();
		//		fail("Not yet implemented");

		sortForCollisions();

		sortForInterval();

		if (nonerrors + errors != 0)
			System.out.println("Total errors: " + errors + " out of " + (errors + nonerrors) + " = " + 
					nonerrors * 100 / (errors + nonerrors) + "% success.");
	}

	void sortForInterval() {
		ArrayList<CollisionData> data = new ArrayList<>(collisions.values());//.toArray();//new ArrayList<>();

		data.sort(new Comparator<CollisionData>() {

			@Override
			public int compare(CollisionData o1, CollisionData o2) {
				if (o1.timeBetweenCollisions < o2.timeBetweenCollisions)
					return -1;
				if (o1.timeBetweenCollisions > o2.timeBetweenCollisions)
					return 1;
				else
					return 0;
			}

		});

		System.out.println("\nTime between Collisions:");
		int i = 0;

		for (CollisionData d : data) {
			if ( d.timeBetweenCollisions > -1 &&  i++ < 10)
				System.out.println(d);
		}

		//		assertTrue(data.get(0).timeBetweenCollisions + " < " + data.get(data.size()-1).timeBetweenCollisions, 
		//				data.get(0).timeBetweenCollisions < data.get(data.size()-1).timeBetweenCollisions);
	}
	void sortForCollisions() {
		ArrayList<CollisionData> data = new ArrayList<>(collisions.values());//.toArray();//new ArrayList<>();

		data.sort(new Comparator<CollisionData>() {

			@Override
			public int compare(CollisionData o1, CollisionData o2) {
				if (o1.count < o2.count)
					return -1;
				if (o1.count > o2.count)
					return 1;
				else
					return 0;
			}

		});

		System.out.println("\nNumber of Collisions:");
		int i = 0;

		for (CollisionData d : data)
			if ( ++i > data.size() - 10 && d.count > 1)
				System.out.println(d);
	}

	class CollisionData {
		private int count = 0;
		private long tick = 0;
		private long total = 0;

		private double timeBetweenCollisions = -1;

		public Node nodeA, nodeB;
		CollisionData(Node a, Node b, long tick) {
			this.tick = tick;
			nodeA = a; nodeB = b;
		}

		public String toString() {
			return nodeA.uniqueName() + " <-> " + nodeB.uniqueName() + ": " + count + ", Avg interval: " + this.timeBetweenCollisions;
		}

		public void update(long tick) {
			if (++count > 1) {
				float time = tick - this.tick;
				total += time;
				this.timeBetweenCollisions = (total / count);
			}
			this.tick = tick;
		}
	}



	HashMap<String,CollisionData> collisions = new HashMap<>();
	long tick = 0;

	int errors = 0, nonerrors = 0;
	@Override
	public void doAfterCollision(Node nodeA, Node nodeB, CollisionEvent event) {
		this.tick = scene.tick();
		if (event.getDistance() < event.startingDistance)  {
			int idA = nodeA.uniqueID();
			int idB = nodeB.uniqueID();

			if (idB < idA) {
				Node temp = nodeA;
				nodeA = nodeB;
				nodeB = temp;
			}
			String key = String.valueOf(nodeA.uniqueID()) +  String.valueOf(nodeB.uniqueID());

			if (collisions.containsKey(key)) {
				collisions.get(key).update(tick);
			} else {
				collisions.put(key, new CollisionData(nodeA, nodeB, tick));
				System.out.println("New Collision: " + key);
			}
			System.err.println(event.getDistance() + " > " + event.startingDistance);
			errors++;
		} else {
			nonerrors++;
		}

		//		System.out.println(nonerrors * 100 / (errors + nonerrors) + "% success");
		//		assertTrue(event.getDistance() + " > " + event.startingDistance,event.getDistance() > event.startingDistance);
		//		assertFalse("Should not touch after collision: ", nodeA.collisionBody().intersects(nodeB.collisionBody()));
	}

	@Override
	public void doBeforeCollision(Node nodeA, Node nodeB, CollisionEvent e) {
		// TODO Auto-generated method stub
		//		this.dist = e.dist;

	}

}
