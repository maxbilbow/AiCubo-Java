package com.maxbilbow.aicubo;


import com.maxbilbow.aicubo.control.GameController;
import com.maxbilbow.aicubo.engine.ai.ants.AntBehaviour;
import com.maxbilbow.aicubo.engine.behaviours.Behaviour;
import com.maxbilbow.aicubo.engine.behaviours.SpriteBehaviour;
import com.maxbilbow.aicubo.engine.geometry.Shapes;
import com.maxbilbow.aicubo.engine.gl.IKeyCallback;
import com.maxbilbow.aicubo.engine.math.Tools;
import com.maxbilbow.aicubo.engine.physics.PhysicsBody;
import com.maxbilbow.aicubo.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import static com.maxbilbow.aicubo.engine.ai.ants.AntBehaviour.*;
import static com.maxbilbow.aicubo.engine.behaviours.Behaviour.GET_AI_STATE;
import static org.lwjgl.glfw.GLFW.*;

@Component("GameController")
public class AiCubo extends GameController
{


  Node player, cameraNode;
  private Logger mLogger = LoggerFactory.getLogger(AiCubo.class);

  @Resource
  private Scene mScene;

  @Resource
  private Nodes mNodes;

  @Resource
  private AxisGenerator mAxisGenerator;

  @Override
  protected void initpov()
  {
    Node body = mNodes.getCurrent();
    body.setPhysicsBody(PhysicsBody.newDynamicBody());
    body.physicsBody().setMass(5.0f);
    //		body.physicsBody().setFriction(0f);
    //		body.physicsBody().setRestitution(0);
    //		body.physicsBody().setDamping(0);
    body.addBehaviour(new SpriteBehaviour());
    body.transform().setScale(4f, 4.0f, 4f);
    mScene.rootNode().addChild(body);
    //		body.setCollisionBody(new CollisionBody());
    Node head = mNodes.newCameraNode();
    body.addChild(head);
    body.transform().setPosition(10f, 20f, 20f);


    this.mView.setPointOfView(head);
    this.player = body;
    this.cameraNode = head;

  }

  public static final int bounds = 200;

  public AiCubo()
  {
    super();
  }

  EntityGenerator eg;

  public void initActors()
  {
    mLogger.debug("Setting up scene...");
    Scene scene = mScene;
    mLogger.debug("SUCCESS");

    mLogger.debug("Setting up actors...");

    mLogger.debug("POV success");
    mLogger.debug("Initializing entity generator");
    {
      eg = new EntityGenerator()
      {

        @Override
        public Node makeEntity()
        {
          float speed = (float) (Tools.rBounds(30, 100) / 1000);
          float rotation = (float) (Tools.rBounds(1, (int) speed * 1000) / 1000);
          Node body = mNodes.makeCube((float) Tools.rBounds(1, 2), PhysicsBody.newDynamicBody(), new AntBehaviour());

          //				body.addBehaviour(new SpriteBehaviour());
          body.physicsBody().setMass((float) Tools.rBounds(2, 8));
          //				body.physicsBody().setRestitution((float)Tools.rBounds(2,8)/10);
          //				body.transform().setScale(2, 2, 2);
          //				body.physicsBody().setDamping(0f);
          //				body.physicsBody().setFriction(0f);
          //				body.physicsBody().setRestitution(0);
          //				body.setCollisionBody(new CollisionBody());
          Node head = mNodes.makeCube(body.transform().radius() / 2, null, node -> {
            if (node != mView.pointOfView())
            {
              node.transform().move("yaw:0.1");
              node.transform().move("pitch:0.1");
              node.transform().move("roll:0.1");
            }
          });

          body.addChild(head);
          head.transform().setPosition(0, //put head where a head should be
                                       body.transform().scale().y + head.transform().scale().y,
                                       body.transform().scale().z + head.transform().scale().z
          );
          Node trailingCam = mNodes.newCameraNode();
          body.addChild(trailingCam);
          trailingCam.setName("trailingCam");
          trailingCam.transform().translate(0, 20, 50);
          return body;
        }

      };

      mLogger.debug("Entity generator initialized");
      eg.yMin = 0;
      eg.yMax = 50;
      eg.xMax = eg.zMax = 100;
      eg.xMin = eg.zMin = -100;
    }
    mLogger.debug("Generating...");


    float inf = 99999;
    {
      mLogger.debug("Success. Creating floor");
      Node floor = mNodes.newGameNode();
      floor.transform().setPosition(0, -10, 0);
      scene.rootNode().addChild(floor);
      //Float.POSITIVE_INFINITY;
      floor.transform().setScale(inf, 10, inf);
      floor.setGeometry(Shapes.Cube);
    }
    {
      mAxisGenerator.setSize(inf);
      mAxisGenerator.makeShapesAndAddToScene(scene, 1);
    }
    mLogger.debug("Actors set up successfully...");
    {
      Node box = mNodes.makeCube(10, PhysicsBody.newStaticBody(), null);
      //		box.physicsBody().setMass(100);
      box.physicsBody().setRestitution(0.9f);
      box.transform().setPosition(bounds, 5, 30);
    }
    {
      Node wallA = mNodes.makeCube(10, PhysicsBody.newStaticBody(), null);
      wallA.transform().setScale(bounds, 10, 10);
      wallA.transform().setPosition(0, 0, bounds + 10);
      wallA.addToCurrentScene();
    }
    {
      Node wallB = mNodes.makeCube(10, PhysicsBody.newStaticBody(), null);
      wallB.transform().setScale(bounds, 10, 10);
      wallB.transform().setPosition(0, 0, -bounds - 10);
      wallB.addToCurrentScene();
    }
    {
      Node wallC = mNodes.makeCube(10, PhysicsBody.newStaticBody(), null);
      wallC.transform().setScale(10, 10, bounds);
      wallC.transform().setPosition(bounds + 10, 0, 0);
      wallC.addToCurrentScene();
    }
    {
      Node wallD = mNodes.makeCube(10, PhysicsBody.newStaticBody(), null);

      wallD.transform().setScale(10, 10, bounds);
      wallD.transform().setPosition(-bounds - 10, 0, 0);
      wallD.addToCurrentScene();
    }
    {
      Node light = mNodes.makeCube(10, PhysicsBody.newStaticBody(), null);
      light.transform().setPosition(100, 50, 100);
      light.setComponent(LightSource.class, new LightSource());
      light.addToCurrentScene();
    }
  }


  @PostConstruct
  public void init()
  {
    //		try {
    super.init();

    initActors();
    Start();
    //		} catch (Exception e) {
    //			e.printStackTrace();
    //			System.exit(1);
    //		}
  }

  long tick = 0;
  int count = 0;

  @Override
  public void updateBeforeSceneRender(Object... args)
  {
    long timePassed = mScene.tick() - tick;
    if (count < 500 && timePassed > 50)
    {
      eg.makeShapesAndAddToScene(mScene, 1);
      this.tick = mScene.tick();
      count++;
    }

  }


  @Override
  public void setup()
  {
    // TODO Auto-generated method stub
    //		initActors();
    mScene.setRenderDelegate(this);

    this.addKeyCallback(new IKeyCallback()
    {

      @Override
      public void invoke(long window, int key, int scancode, int action, int mods)
      {
        if (action == GLFW_RELEASE)
        {
          switch (key)
          {
            case GLFW_KEY_TAB:
              int max = mScene.rootNode().getChildren().size() - 1;
              Node n, cam;
              mNodes.getCurrent().sendMessageToBehaviour(AntBehaviour.class, "setDefaultState");
              do
              {
                n = mScene.rootNode().getChildren().get((int) Tools.rBounds(0, max));
                cam = n.getChildWithName("trailingCam");
              } while (cam == null);
              n.setValue(GET_AI_STATE,
                         Behaviour.AI_STATE_POSSESSED
              );//.sendMessageToBehaviour(Behaviour.class,"setState", Behaviour.AI_STATE_POSSESSED);

              mNodes.setCurrent(n);
              getView().setPointOfView(cam);
              //						getView().pointOfView().transform().localMatrix().setIdentity();
              break;
            case GLFW_KEY_ENTER:
              Transform t = mView.pointOfView().transform();//.rootTransform().localMatrix();
              player.transform().rootTransform().localMatrix().set(t.rootTransform().localMatrix());
              player.transform().rootTransform().setPosition(t.position());
              //						player.transform().translate(0, player.transform().getHeight(), 0);
              mNodes.getCurrent().sendMessageToBehaviour(AntBehaviour.class, "setDefaultState");
              mNodes.setCurrent(player);
              getView().setPointOfView(cameraNode);

              break;
            case GLFW_KEY_I:
              didCauseEvent(AntBehaviour.CanWalkThroughWalls, mods == GLFW_MOD_SHIFT ? "true" : "false");
              break;
            case GLFW_KEY_F:
              switch (mods)
              {
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
              mNodes.getCurrent()
                      .broadcastMessage("setEffectedByGravity", mods == GLFW_MOD_SHIFT ? false : true);
              break;
            case GLFW_KEY_A:
              if (mods == GLFW_MOD_SHIFT)
              {
                didCauseEvent(GET_AI_STATE, Amble);
              }
              break;
            case GLFW_KEY_L:
              if (mods == GLFW_MOD_SHIFT)
              {
                didCauseEvent(TheLeader, mNodes.getCurrent());
              }
              else if (mods == GLFW_MOD_CONTROL)
              {
                didCauseEvent(TheLeader, mNodes.randomAiNode());
              }
              else if (mods == GLFW_MOD_ALT)
              {
                Leader = null;
              }
              else
              {
                if (Leader == null)
                {
                  Leader = mNodes.getCurrent();
                }
                didCauseEvent(FollowTheLeader);
              }
              break;
          }
        }
        if (mView.pointOfView().physicsBody() != null)
        {
          System.err.println(mView.pointOfView().getName() + " has physicsBody");
        }

      }


    });
  }

  @Override
  public Scene getScene()
  {
    return mScene;
  }
}


