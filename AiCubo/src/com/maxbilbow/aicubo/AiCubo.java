
package com.maxbilbow.aicubo;

import org.lwjgl.opengl.GL11;

import click.rmx.Bugger;
import click.rmx.engine.Behaviour;
import click.rmx.engine.Camera;
import click.rmx.engine.CollisionBody;
import click.rmx.engine.GameController;
import click.rmx.engine.GameView;
import click.rmx.engine.Geometry;
import click.rmx.engine.Node;
import click.rmx.engine.PhysicsBody;
import click.rmx.engine.Scene;
import click.rmx.engine.behaviours.SpriteBehaviour;
import click.rmx.engine.math.Tools;
import click.rmx.engine.math.Vector3;
public final class AiCubo extends GameController {	
	protected void initpov() {
		Node body = Node.getCurrent();
		body.setPhysicsBody(new PhysicsBody());
		body.physicsBody().setMass(5.0f);
//		body.physicsBody().setFriction(0f);
//		body.physicsBody().setRestitution(0);
		body.addBehaviour(new SpriteBehaviour());
		body.transform.setScale(2f, 10.0f, 2f);	
		Scene.getCurrent().rootNode.addChild(body);
		body.setCollisionBody(new CollisionBody());
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
		
	}
	
	public void setup() {	
		Bugger.log("Setting up scene...");
		Scene scene = Scene.getCurrent();
		Bugger.log("SUCCESS");
		scene.setRenderDelegate(this);
		Bugger.log("Setting up actors...");
		initpov();		
		Bugger.log("POV success");
		Bugger.log("Initializing entity generator");
		EntityGenerator eg = new EntityGenerator() {

			@Override
			public Node makeEntity() {
				float speed = (float) (Tools.rBounds(10, 500) / 1000);
				float rotation = (float) (Tools.rBounds(1, (int)speed * 1000) / 1000);
				Node body = Node.makeCube(1f, true, new Behaviour() {
					@Override
					public void update() {
						this.getNode().broadcastMessage("applyForce","forward:"+speed);

						this.getNode().physicsBody().applyTorque(rotation, Vector3.Y, Vector3.Zero);
					}
					
				});
				body.addBehaviour(new SpriteBehaviour());
				body.physicsBody().setMass(2.0f);
				body.physicsBody().setRestitution((float)Tools.rBounds(0,1));
//				body.transform.setScale(2, 2, 2);
				body.physicsBody().setDamping(0f);
//				body.physicsBody().setFriction(0f);
//				body.physicsBody().setRestitution(1);
				body.setCollisionBody(new CollisionBody());
				Node head = Node.makeCube(body.transform.radius() / 2, false, new Behaviour() {
					@Override
					public void update() {
						this.getNode().transform.move("yaw:0.1");
						this.getNode().transform.move("pitch:0.1");
						this.getNode().transform.move("roll:0.1");
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
		eg.yMin = eg.yMax = 0;
//		eg.xMax = eg.zMax = 100;
//		eg.xMin = eg.zMin = -100;
		
		Bugger.log("Generating...");
		eg.makeShapesAndAddToScene(scene, 500);
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
	}
	public static AiCubo getInstance() {
		if(singleton == null) {
			synchronized(GameController.class) {
				if(singleton == null) {
					singleton = new AiCubo();
					singleton.setView(new GameView());
				}
			}
		}
		return (AiCubo) singleton;
	}
	public static void main(String[] args) {
//		try {
			AiCubo.getInstance().Start();
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(1);
//		}
	}
}

	
