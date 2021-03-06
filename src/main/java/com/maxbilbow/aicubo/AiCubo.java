
package com.maxbilbow.aicubo;



import com.maxbilbow.aicubo.ants.AntBehaviour;
import com.maxbilbow.aicubo.hud.InfoWindow;
import static com.maxbilbow.aicubo.ants.AntBehaviour.*;

import static org.lwjgl.glfw.GLFW.*;

import click.rmx.Bugger;
import click.rmx.engine.GameController;
import click.rmx.engine.Nodes;
import click.rmx.engine.LightSource;
import click.rmx.engine.Node;
import click.rmx.engine.Scene;
import click.rmx.engine.Transform;
import click.rmx.engine.behaviours.Behaviour;
import click.rmx.engine.behaviours.SpriteBehaviour;
import click.rmx.engine.geometry.Shapes;
import click.rmx.engine.gl.IKeyCallback;
import click.rmx.engine.math.Tools;
import click.rmx.engine.physics.PhysicsBody;
public final class AiCubo extends GameController {	

	Node player, cameraNode;
	@Override
	protected void initpov() {
		Node body = Nodes.getCurrent();
		body.setPhysicsBody(PhysicsBody.newDynamicBody());
		body.physicsBody().setMass(5.0f);
		//		body.physicsBody().setFriction(0f);
		//		body.physicsBody().setRestitution(0);
		//		body.physicsBody().setDamping(0);
		body.addBehaviour(new SpriteBehaviour());
		body.transform().setScale(4f, 4.0f, 4f);	
		Scene.getCurrent().rootNode().addChild(body);
		//		body.setCollisionBody(new CollisionBody());
		Node head = Nodes.newCameraNode();
		body.addChild(head);
		body.transform().setPosition(10f,20f,20f);


		this.view.setPointOfView(head);
		this.player = body;
		this.cameraNode = head;

	}
	public static final int bounds = 200;

	protected AiCubo(){
		super();
	}

	EntityGenerator eg;
	public void initActors() {	
		Bugger.log("Setting up scene...");
		Scene scene = Scene.getCurrent();
		Bugger.log("SUCCESS");

		Bugger.log("Setting up actors...");

		Bugger.log("POV success");
		Bugger.log("Initializing entity generator");
		{
			eg = new EntityGenerator() {

				@Override
				public Node makeEntity() {
					float speed = (float) (Tools.rBounds(30, 100) / 1000);
					float rotation = (float) (Tools.rBounds(1, (int)speed * 1000) / 1000);
					Node body = Nodes.makeCube((float)Tools.rBounds(1, 2), PhysicsBody.newDynamicBody(), new AntBehaviour());

					//				body.addBehaviour(new SpriteBehaviour());
					body.physicsBody().setMass((float) Tools.rBounds(2,8));
					//				body.physicsBody().setRestitution((float)Tools.rBounds(2,8)/10);
					//				body.transform().setScale(2, 2, 2);
					//				body.physicsBody().setDamping(0f);
					//				body.physicsBody().setFriction(0f);
					//				body.physicsBody().setRestitution(0);
					//				body.setCollisionBody(new CollisionBody());
					Node head = Nodes.makeCube(body.transform().radius() / 2, null, node -> {
						if (node != view.pointOfView()) {
							node.transform().move("yaw:0.1");
							node.transform().move("pitch:0.1");
							node.transform().move("roll:0.1");
						}
					});

					body.addChild(head);
					head.transform().setPosition(0, //put head where a head should be
							body.transform().scale().y + head.transform().scale().y,
							body.transform().scale().z + head.transform().scale().z);
					Node trailingCam = Nodes.newCameraNode();
					body.addChild(trailingCam);
					trailingCam.setName("trailingCam");
					trailingCam.transform().translate(0, 20, 50);
					return body;
				}

			};

			Bugger.log("Entity generator initialized");
			eg.yMin = 0; eg.yMax = 50;
			eg.xMax = eg.zMax = 100;
			eg.xMin = eg.zMin = -100;
		}
		Bugger.log("Generating...");


		float inf = 99999;
		{
			Bugger.log("Success. Creating floor");
			Node floor = Nodes.newGameNode();
			floor.transform().setPosition(0,-10,0);
			scene.rootNode().addChild(floor);
			//Float.POSITIVE_INFINITY;
			floor.transform().setScale(inf, 10, inf);
			floor.setGeometry(Shapes.Cube);
		}
		{
			AxisGenerator ag = new AxisGenerator(inf);
			ag.makeShapesAndAddToScene(scene, 1);
		}
		Bugger.log("Actors set up successfully...");
		{
			Node box = Nodes.makeCube(10, PhysicsBody.newStaticBody(), null);
			//		box.physicsBody().setMass(100);
			box.physicsBody().setRestitution(0.9f);
			box.transform().setPosition(bounds,5,30);
		}
		{
			Node wallA = Nodes.makeCube(10, PhysicsBody.newStaticBody(), null);
			wallA.transform().setScale(bounds, 10, 10);
			wallA.transform().setPosition(0,0,bounds+10);
			wallA.addToCurrentScene();
		}
		{
			Node wallB = Nodes.makeCube(10, PhysicsBody.newStaticBody(), null);
			wallB.transform().setScale(bounds, 10, 10);
			wallB.transform().setPosition(0,0,-bounds-10);
			wallB.addToCurrentScene();
		}
		{
			Node wallC = Nodes.makeCube(10, PhysicsBody.newStaticBody(), null);
			wallC.transform().setScale(10, 10, bounds);
			wallC.transform().setPosition(bounds+10,0,0);
			wallC.addToCurrentScene();
		}
		{
			Node wallD = Nodes.makeCube(10, PhysicsBody.newStaticBody(), null);

			wallD.transform().setScale(10, 10, bounds);
			wallD.transform().setPosition(-bounds-10,0,0);
			wallD.addToCurrentScene();
		}
		{
			Node light = Nodes.makeCube(10, PhysicsBody.newStaticBody(), null);
			light.transform().setPosition(100,50,100);
			light.setComponent(LightSource.class, new LightSource());
			light.addToCurrentScene();
		}
	}


	public static void main(String[] args) {
		//		try {
		InfoWindow.open();
		AiCubo game = new AiCubo();
		game.initActors();
		game.Start();
		//		} catch (Exception e) {
		//			e.printStackTrace();
		//			System.exit(1);
		//		}
	}

	long tick = 0; int count = 0;
	@Override
	public void updateBeforeSceneRender(Object... args) {
		long timePassed = Scene.getCurrent().tick() -  tick;
		if (count < 500 && timePassed > 50) {
			eg.makeShapesAndAddToScene(Scene.getCurrent(), 1);
			this.tick = Scene.getCurrent().tick();
			count++;
		}

	}



	@Override
	public void setup() {
		// TODO Auto-generated method stub
		//		initActors();
		Scene.getCurrent().setRenderDelegate(this);

		this.addKeyCallback(new IKeyCallback() {

			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				if (action == GLFW_RELEASE)
					switch (key) {
					case GLFW_KEY_TAB:
						int max = Scene.getCurrent().rootNode().getChildren().size() - 1;
						Node n, cam;
						Nodes.getCurrent().sendMessageToBehaviour(AntBehaviour.class,"setDefaultState");
						do {
							n = Scene.getCurrent().rootNode().getChildren().get((int)Tools.rBounds(0, max));
							cam = n.getChildWithName("trailingCam");
						} while (cam == null);
						n.setValue(Behaviour.GET_AI_STATE, Behaviour.AI_STATE_POSSESSED);//.sendMessageToBehaviour(Behaviour.class,"setState", Behaviour.AI_STATE_POSSESSED);

						Nodes.setCurrent(n);
						getView().setPointOfView(cam);
						//						getView().pointOfView().transform().localMatrix().setIdentity();
						break;
					case GLFW_KEY_ENTER:
						Transform t = view.pointOfView().transform();//.rootTransform().localMatrix();
						player.transform().rootTransform().localMatrix().set(t.rootTransform().localMatrix());
						player.transform().rootTransform().setPosition(t.position());
						//						player.transform().translate(0, player.transform().getHeight(), 0);
						Nodes.getCurrent().sendMessageToBehaviour(AntBehaviour.class,"setDefaultState");
						Nodes.setCurrent(player);
						getView().setPointOfView(cameraNode);

						break;
					case GLFW_KEY_I:
						didCauseEvent(AntBehaviour.CanWalkThroughWalls, mods == GLFW_MOD_SHIFT ? "true" : "false");
						break;
					case GLFW_KEY_F:
						switch (mods) {
						//						case GLFW_MOD_SHIFT:
						//							this.didCauseEvent(FollowTheLeader, "current");
						//							break;
						//						case GLFW_MOD_ALT:
						//							this.didCauseEvent(FollowTheLeader, "random");
						//							break;
						default:
							didCauseEvent(GET_AI_STATE, FollowTheLeader);
							break;	
						}
						break;
					case GLFW_KEY_G:
						Nodes.getCurrent().broadcastMessage("setEffectedByGravity",mods == GLFW_MOD_SHIFT ? false : true);
						break;
					case GLFW_KEY_A:
						if (mods == GLFW_MOD_SHIFT) 
							didCauseEvent(GET_AI_STATE, Amble);
						break;
					case GLFW_KEY_L:
						if (mods == GLFW_MOD_SHIFT) 
							didCauseEvent(TheLeader, Nodes.getCurrent());
						else if (mods == GLFW_MOD_CONTROL) 
							didCauseEvent(TheLeader, Nodes.randomAiNode());
						else if (mods == GLFW_MOD_ALT)
							Leader = null;
						else {
							if (Leader == null)
								Leader = Nodes.getCurrent();
							didCauseEvent(FollowTheLeader);
						}
						break;
					}
				if (view.pointOfView().physicsBody() != null)
					System.err.println(view.pointOfView().getName() + " has physicsBody");

			}


		});
	}
}


