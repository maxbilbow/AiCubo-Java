
package com.maxbilbow.aicubo;



import org.lwjgl.opengl.GL11;
import static org.lwjgl.glfw.GLFW.*;

import click.rmx.Bugger;
import click.rmx.engine.Camera;
import click.rmx.engine.GameController;

import click.rmx.engine.Geometry;
import click.rmx.engine.LightSource;
import click.rmx.engine.Node;
import click.rmx.engine.Scene;
import click.rmx.engine.behaviours.Behaviour;
import click.rmx.engine.behaviours.CollisionHandler;
import click.rmx.engine.behaviours.SpriteBehaviour;
import click.rmx.engine.gl.IKeyCallback;
import click.rmx.engine.math.Matrix4;
import click.rmx.engine.math.Tools;
import click.rmx.engine.math.Vector3;

import click.rmx.engine.physics.CollisionEvent;
import click.rmx.engine.physics.PhysicsBody;
public final class AiCubo extends GameController {	

	Node player, cameraNode;
	protected void initpov() {
		Node body = Node.getCurrent();
		body.setPhysicsBody(PhysicsBody.newDynamicBody());
		body.physicsBody().setMass(5.0f);
		//		body.physicsBody().setFriction(0f);
		//		body.physicsBody().setRestitution(0);
		//		body.physicsBody().setDamping(0);
		body.addBehaviour(new SpriteBehaviour());
		body.transform.setScale(4f, 4.0f, 4f);	
		Scene.getCurrent().rootNode.addChild(body);
		//		body.setCollisionBody(new CollisionBody());
		Node head = new Node();
		body.addChild(head);
		head.setCamera(new Camera());
		head.addBehaviour(new Behaviour(){

			//			public void lookUp(String speed) {
			//				
			//			}

			@Override
			public void broadcastMessage(String message, Object args) {

				if (message == "lookUp") {
					//					Bugger.logAndPrint(message + ": "+ args , false);
					head.transform.rotate("pitch", (float) args);
				}
			}
			@Override
			public void update() {
				// TODO Auto-generated method stub

			}

		});


		this.view.setPointOfView(head);
		this.player = body;
		this.cameraNode = head;

	}
	static final int bounds = 200;

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

		eg = new EntityGenerator() {

			@Override
			public Node makeEntity() {
				float speed = (float) (Tools.rBounds(30, 100) / 1000);
				float rotation = (float) (Tools.rBounds(1, (int)speed * 1000) / 1000);
				Node body = Node.makeCube((float)Tools.rBounds(1, 2), PhysicsBody.newDynamicBody(), new Behaviour() {

					@Override
					public void update() {
						limit(AiCubo.bounds * 2);
						if (this.getNode() != view.pointOfView()) {
							this.getNode().broadcastMessage("applyForce","forward:"+speed);

							this.getNode().physicsBody().applyTorque(rotation, Vector3.Y, Vector3.Zero);

						}

					}


					void limit(int bounds) {
						Matrix4 m = this.transform().localMatrix();
						if (m.m30 > bounds) {
							m.m30 *= -0.5;
							m.m31 = (float)Tools.rBounds(10, 100);
						} else if (m.m30 <= -bounds) {
							m.m30 *= -0.5;
							m.m31 = (float)Tools.rBounds(10, 100);
						}
						if (m.m32 > bounds) {
							m.m32 *= -0.5;
							m.m31 = (float)Tools.rBounds(10, 100);
						} else if (m.m32 <= -bounds) {
							m.m32 *= -0.5;
							m.m31 = (float)Tools.rBounds(10, 100);
						}
					}

				});

				body.addBehaviour(new CollisionHandler() {

					@Override
					public void onCollision(CollisionEvent e) {
						//						if (this.transform().position().y < this.transform().scale().y)
						//							this.getNode().physicsBody().applyForce(2f, Vector3.Y, Vector3.Zero);
					}


				});



				body.addBehaviour(new SpriteBehaviour());
				body.physicsBody().setMass((float) Tools.rBounds(2,8));
				//				body.physicsBody().setRestitution((float)Tools.rBounds(2,8)/10);
				//				body.transform.setScale(2, 2, 2);
				//				body.physicsBody().setDamping(0f);
				//				body.physicsBody().setFriction(0f);
				//				body.physicsBody().setRestitution(0);
				//				body.setCollisionBody(new CollisionBody());
				Node head = Node.makeCube(body.transform.radius() / 2, null, new Behaviour() {
					@Override
					public void update() {
						if (this.getNode() != view.pointOfView()) {
							this.getNode().transform.move("yaw:0.1");
							this.getNode().transform.move("pitch:0.1");
							this.getNode().transform.move("roll:0.1");
						}
					}
				});

				body.addChild(head);
				head.transform.setPosition(0, //put head where a head should be
						body.transform.scale().y + head.transform.scale().y,
						body.transform.scale().z + head.transform.scale().z);
				return body;
			}

		};

		Bugger.log("Entity generator initialized");
		eg.yMin = 0; eg.yMax = 50;
		eg.xMax = eg.zMax = 100;
		eg.xMin = eg.zMin = -100;

		Bugger.log("Generating...");
		
		Bugger.log("Success. Creating floor");
		Node floor = new Node();
		floor.transform.setPosition(0,0,0);
		scene.rootNode.addChild(floor);
		float inf = 99999;//Float.POSITIVE_INFINITY;
		floor.setGeometry(new Geometry(4*3){

			@Override
			protected void drawWithScale(float x, float y, float z) {

				GL11.glBegin(GL11.GL_POLYGON);    
				GL11.glColor3f(0.8f,0.8f,0.8f);           
				GL11.glVertex3f( inf, -y,-inf);        
				GL11.glVertex3f(-inf, -y,-inf);        
				GL11.glVertex3f(-inf, -y, inf);
				GL11.glVertex3f( inf, -y, inf);  	
				GL11.glEnd(); 
			}

		});

		AxisGenerator ag = new AxisGenerator(inf);
		ag.makeShapesAndAddToScene(scene, 1);
		Bugger.log("Actors set up successfully...");

		Node box = Node.makeCube(10, PhysicsBody.newStaticBody(), null);
		//		box.physicsBody().setMass(100);
		box.physicsBody().setRestitution(0.9f);
		box.transform.setPosition(bounds,5,30);

		Node wallA = Node.makeCube(10, PhysicsBody.newStaticBody(), null);
		wallA.transform.setScale(bounds, 10, 10);
		wallA.transform.setPosition(0,0,bounds+10);
		wallA.addToCurrentScene();
		Node wallB = Node.makeCube(10, PhysicsBody.newStaticBody(), null);
		wallB.transform.setScale(bounds, 10, 10);
		wallB.transform.setPosition(0,0,-bounds-10);
		wallB.addToCurrentScene();
		Node wallC = Node.makeCube(10, PhysicsBody.newStaticBody(), null);
		wallC.transform.setScale(10, 10, bounds);
		wallC.transform.setPosition(bounds+10,0,0);
		wallC.addToCurrentScene();
		Node wallD = Node.makeCube(10, PhysicsBody.newStaticBody(), null);
		wallD.transform.setScale(10, 10, bounds);
		wallD.transform.setPosition(-bounds-10,0,0);
		wallD.addToCurrentScene();

		Node light = Node.makeCube(10, PhysicsBody.newStaticBody(), null);
		light.transform.setPosition(100,50,100);
		light.setComponent(LightSource.class, new LightSource());
		light.addToCurrentScene();

	}


	//	public static AiCubo getInstance() {
	//		if(singleton == null) {
	//			synchronized(GameController.class) {
	//				if(singleton == null) {
	//					singleton = new AiCubo();
	//					singleton.setView(new GameView());
	//				}
	//			}
	//		}
	//		return (AiCubo) singleton;
	//	}
	public static void main(String[] args) {
		//		try {
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
		if (count < 500 && timePassed > 500) {
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
						int max = Scene.getCurrent().rootNode.getChildren().size() - 1;
						Node n = Scene.getCurrent().rootNode.getChildren().get((int)Tools.rBounds(0, max));
						Node.setCurrent(n);

						getView().setPointOfView(n.getChildren().isEmpty() ? n : n.getChildren().get(0));
						getView().pointOfView().transform.localMatrix().setIdentity();
						break;
					case GLFW_KEY_ENTER:
						Node.setCurrent(player);
						getView().setPointOfView(cameraNode);
						break;
					}
			}

		});
	}
}


