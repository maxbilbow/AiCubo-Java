package com.maxbilbow.aicubo.model;

import com.maxbilbow.aicubo.control.PlayerController;
import com.maxbilbow.aicubo.control.SceneController;
import com.maxbilbow.aicubo.engine.behaviours.Behaviour;
import com.maxbilbow.aicubo.engine.behaviours.CameraBehaviour;
import com.maxbilbow.aicubo.engine.behaviours.IBehaviour;
import com.maxbilbow.aicubo.engine.geometry.Shapes;
import com.maxbilbow.aicubo.engine.math.Tools;
import com.maxbilbow.aicubo.engine.physics.PhysicsBody;
import com.maxbilbow.aicubo.model.node.NodeLocation;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Component
public class Nodes
{

  private static Nodes            INSTANCE;

  @Resource
  private PlayerController mPlayerController;

//  @Resource
//  private GameNodeService mNodeService;

  private List<NodeLocation> mExistingNodes;



  @Resource
  private SceneController mSceneController;

  @PostConstruct
  private void init()
  {
    INSTANCE = this;
//    GameNode player = mNodeService.findWithName("Player");
//    if (player != null)
//    {
//      current = player.getEngine();
//    }
//    else
//    {
//      current = newGameNode("Player");
//      current.getGameNode().setRole(NodeRole.Player);
//      mNodeService.save(current.getGameNode());
//    }
  }

  public Node newGameNode()
  {
    return new com.maxbilbow.aicubo.model.NodeEngine();
  }

  public Node newGameNode(String aName)
  {
    Node node = newGameNode();
    node.setName(aName);
//    NodeLocation node = new NodeLocation();//.getEngine();
//    node.setName(name);

    return node;//.getEngine();
  }

  public static RootNode newRootNode()
  {
    return com.maxbilbow.aicubo.model.NodeEngine.newRootNode();
  }


  public Node newCameraNode()
  {
    Node cameraNode = newGameNode("CameraNode");
    cameraNode.setCamera(new Camera());
    cameraNode.addBehaviour(new CameraBehaviour());
    return cameraNode;
  }

  public Node makeCube(float s, PhysicsBody body, IBehaviour b)
  {
    Node n = newGameNode("Cube");
    n.setGeometry(Shapes.Cube);
    if (body != null)
    {
      n.setPhysicsBody(body);
    }
    n.transform().setScale(s, s, s);
    n.addBehaviour(b);
//		n.addToCurrentScene();
    return n;
  }

  public static Node randomAiNode()
  {
    if (INSTANCE == null)
    {
      LoggerFactory.getLogger(Nodes.class).warn("Nodes not set up");
      return null;
    }

    Stream stream;

    @SuppressWarnings("unchecked")
    final Collection<Node> nodes = INSTANCE.mSceneController.getScene().getRootNode().getChildren();//.clone();

    stream = nodes.stream().filter(n -> n.getValue(Behaviour.GET_AI_STATE) == null);

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
