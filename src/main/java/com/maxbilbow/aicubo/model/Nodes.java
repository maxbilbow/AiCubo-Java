package com.maxbilbow.aicubo.model;

import com.maxbilbow.aicubo.engine.behaviours.Behaviour;
import com.maxbilbow.aicubo.engine.behaviours.CameraBehaviour;
import com.maxbilbow.aicubo.engine.behaviours.IBehaviour;
import com.maxbilbow.aicubo.engine.geometry.Shapes;
import com.maxbilbow.aicubo.engine.math.Tools;
import com.maxbilbow.aicubo.engine.physics.PhysicsBody;
import com.maxbilbow.aicubo.model.node.GameNode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Component
public class Nodes
{

  private static Nodes INSTANCE;
//  @Resource
//  private GameNodeService mNodeService;

  private List<GameNode> mExistingNodes;

  public Nodes()
  {
    INSTANCE = this;
  }
  @PostConstruct
  private void init()
  {


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
    if (mExistingNodes == null || mExistingNodes.isEmpty())
    {
      return new GameNode().getEngine(); //mNodeService.newEntity().getEngine();
    }
    return mExistingNodes.remove(0).getEngine();
  }

  public Node newGameNode(String name)
  {
    GameNode node = new GameNode();//.getEngine();
    node.setName(name);
    return node.getEngine();
  }

  public static RootNode newRootNode()
  {
    return com.maxbilbow.aicubo.model.NodeEngine.newRootNode();
  }

  private Node current;

  public void setCurrent(Node n)
  {
    current = n;
  }

  public static Node getCurrent()
  {
    if (INSTANCE.current == null)
    {
      INSTANCE.current = INSTANCE.newGameNode("Player");
//      INSTANCE.mNodeService.save(INSTANCE.current.getGameNode());
    }
    return INSTANCE.current;
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
    Stream stream;

    @SuppressWarnings("unchecked")
    Collection<Node> nodes = Scene.getCurrent().rootNode().getChildren();//.clone();

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
