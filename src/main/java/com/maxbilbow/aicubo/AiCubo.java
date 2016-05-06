package com.maxbilbow.aicubo;


import com.maxbilbow.aicubo.control.GameController;
import com.maxbilbow.aicubo.control.PlayerController;
import com.maxbilbow.aicubo.control.SceneController;
import com.maxbilbow.aicubo.engine.RenderDelegate;
import com.maxbilbow.aicubo.engine.ai.ants.AntBehaviour;
import com.maxbilbow.aicubo.engine.behaviours.Behaviour;
import com.maxbilbow.aicubo.engine.behaviours.SpriteBehaviour;
import com.maxbilbow.aicubo.engine.geometry.Shapes;
import com.maxbilbow.aicubo.engine.gl.KeyCallback;
import com.maxbilbow.aicubo.engine.math.Tools;
import com.maxbilbow.aicubo.engine.physics.PhysicsBody;
import com.maxbilbow.aicubo.model.*;
import com.maxbilbow.aicubo.model.core.RMXObject;
import com.maxbilbow.aicubo.view.PointOfView;
import com.maxbilbow.aicubo.view.SceneRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.maxbilbow.aicubo.engine.ai.ants.AntBehaviour.*;
import static com.maxbilbow.aicubo.engine.behaviours.Behaviour.GET_AI_STATE;
import static org.lwjgl.glfw.GLFW.*;

@Component
public class AiCubo extends RMXObject implements RenderDelegate, AppLauncher
{
  @Resource
  private GameController mGameController;

  @Resource
  private PlayerController mPlayerController;

  private Logger mLogger = LoggerFactory.getLogger(AiCubo.class);

  @Resource
  private SceneController mSceneController;

  @Resource
  private Nodes mNodes;

  @Resource
  private PointOfView mPointOfView;

  @Resource
  private AxisGenerator mAxisGenerator;

  @Resource
  private KeyCallback mKeyCallback;

  @Resource
  private SceneRenderer mSceneRenderer;


  @Override
  public void launch()
  {
//    mGameController.setView(GameView.newInstance(mGameController));
    setup();
    initpov();
    initActors();
    mSceneRenderer.addDelegate(this);
    mGameController.run();
  }

  private long mTimeStamp = System.currentTimeMillis();
  int  count      = 0;


  protected void initpov()
  {
    Node body = mNodes.newGameNode("Player");
    body.setPhysicsBody(PhysicsBody.newDynamicBody());
    body.physicsBody().setMass(5.0f);
    //		body.physicsBody().setFriction(0f);
    //		body.physicsBody().setRestitution(0);
    //		body.physicsBody().setDamping(0);
    body.addBehaviour(new SpriteBehaviour());
    body.transform().setScale(4f, 4.0f, 4f);

//    mSceneController.getScene().getRootNode().addChild(body);
    //		body.setCollisionBody(new CollisionBody());
    Node head = mNodes.newCameraNode();
    body.addChild(head);
    body.transform().setPosition(10f, 20f, 20f);

    mPointOfView.set(head);
    mPlayerController.setPlayer(body);
    mSceneController.getScene().addToScene(body);

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
    final Scene scene = mSceneController.getScene();
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
            if (node != mPointOfView.get())
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
      scene.getRootNode().addChild(floor);
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
      scene.addToScene(wallA);
    }
    {
      Node wallB = mNodes.makeCube(10, PhysicsBody.newStaticBody(), null);
      wallB.transform().setScale(bounds, 10, 10);
      wallB.transform().setPosition(0, 0, -bounds - 10);
      scene.addToScene(wallB);
    }
    {
      Node wallC = mNodes.makeCube(10, PhysicsBody.newStaticBody(), null);
      wallC.transform().setScale(10, 10, bounds);
      wallC.transform().setPosition(bounds + 10, 0, 0);
      scene.addToScene(wallC);
    }
    {
      Node wallD = mNodes.makeCube(10, PhysicsBody.newStaticBody(), null);

      wallD.transform().setScale(10, 10, bounds);
      wallD.transform().setPosition(-bounds - 10, 0, 0);
      scene.addToScene(wallD);
    }
    {
      Node light = mNodes.makeCube(10, PhysicsBody.newStaticBody(), null);
      light.transform().setPosition(100, 50, 100);
      light.setComponent(LightSource.class, new LightSource());
      scene.addToScene(light);
    }
  }


  @Override
  public void updateBeforeSceneLogic(Object... args)
  {

  }


  @Override
  public void updateBeforeSceneRender(Object... args)
  {
    final long timePassed = System.currentTimeMillis() - mTimeStamp;
    if (count < 500 && timePassed > 50)
    {
      eg.makeShapesAndAddToScene(mSceneController.getScene(), 1);
      this.mTimeStamp = System.currentTimeMillis();
      count++;
    }

  }

  private void setup()
  {

    mKeyCallback.addCallback((window, key, scancode, action, mods) -> {
      if (action == GLFW_RELEASE)
      {
        final Node player = mPlayerController.getPlayer();
        final Scene scene = mSceneController.getScene();
        switch (key)
        {
          case GLFW_KEY_TAB:
            int max = scene.getRootNode().getChildren().size() - 1;
            Node n, cam;
            player.sendMessageToBehaviour(AntBehaviour.class, "setDefaultState");
            do
            {
              n = scene.getRootNode().getChildren().get((int) Tools.rBounds(0, max));
              cam = n.getChildWithName("trailingCam");
            } while (cam == null);
            n.setValue(GET_AI_STATE,
                       Behaviour.AI_STATE_POSSESSED
            );//.sendMessageToBehaviour(Behaviour.class,"setState", Behaviour.AI_STATE_POSSESSED);

            mPlayerController.setPlayer(n);
            mPointOfView.set(cam);
            //						getView().pointOfView().transform().localMatrix().setIdentity();
            break;
          case GLFW_KEY_ENTER:
            Transform t = mPointOfView.get().transform();//.rootTransform().localMatrix();
            player.transform().rootTransform().localMatrix().set(t.rootTransform().localMatrix());
            player.transform().rootTransform().setPosition(t.position());
            //						player.transform().translate(0, player.transform().getHeight(), 0);
            player.sendMessageToBehaviour(AntBehaviour.class, "setDefaultState");
            mPlayerController.setPlayer(player);
            mPointOfView.set(player.camera().getNode());

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
            player.broadcastMessage("setEffectedByGravity", mods == GLFW_MOD_SHIFT ? false : true);
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
              didCauseEvent(TheLeader, player);
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
                Leader = player;
              }
              didCauseEvent(FollowTheLeader);
            }
            break;
        }
      }
      if (mPointOfView.get().physicsBody() != null)
      {
        System.err.println(mPointOfView.get().getName() + " has physicsBody");
      }

    });
  }

}


