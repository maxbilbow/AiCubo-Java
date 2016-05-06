package com.maxbilbow.aicubo.model;


import com.maxbilbow.aicubo.engine.math.Matrix4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

public class Scene //extends RMXObject
{

  private final Logger       mLogger      = LoggerFactory.getLogger(Scene.class);

  private final RootNode mRootNode;

  public Scene()
  {
    mLogger.debug("Scene initializing...");
    this.mRootNode = Nodes.newRootNode();
  }

  public RootNode getRootNode()
  {
    return this.mRootNode;
  }


//  protected static class node
//  {
//    Scene scene;
//    node  next;
//    node  prev;
//
//    node(node prev, Scene scene, node next)
//    {
//      this.scene = scene;
//      this.next = next;
//      this.prev = prev;
//    }
//
//  }




  public void renderScene(Matrix4 viewMatrix)
  {

    Stream<Node> stream = this.mRootNode.getChildren().stream();

    stream.forEach(n -> {
      n.shine();
      if (viewMatrix != null)
      {
        n.draw(viewMatrix);
      }
    });


  }

  public void addToScene(Node... aNodes)
  {
    for (Node aNode : aNodes)
    {
      if (aNode.getScene() != null)
      {
        aNode.getScene().mRootNode.removeChild(aNode);
      }
      mRootNode.addChild(aNode);
      aNode.setScene(this);
    }
  }
}
