package com.maxbilbow.aicubo;


import com.maxbilbow.aicubo.control.SceneController;
import com.maxbilbow.aicubo.engine.physics.PhysicsBody;
import com.maxbilbow.aicubo.model.Node;
import com.maxbilbow.aicubo.model.Nodes;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AxisGenerator extends EntityGenerator
{

  @Resource
  private Nodes mNodes;

  @Resource
  private SceneController mSceneController;

  private float mSize = 1000.0f;

  @Override
  public Node makeEntity()
  {
//		Node X = Node.makeCube(1, PhysicsBody.newStaticBody(), null);
//		Node Y = Node.makeCube(1, PhysicsBody.newStaticBody(), null);
//		Node Z = Node.makeCube(1, PhysicsBody.newStaticBody(), null);

    Node X = mNodes.makeCube(1, PhysicsBody.newStaticBody(), null);
    Node Y = mNodes.makeCube(1, PhysicsBody.newStaticBody(), null);
    Node Z = mNodes.makeCube(1, PhysicsBody.newStaticBody(), null);

    X.transform().setScale(mSize, 1.0f, 1.0f);
//		Y.transform.setScale(1.0f, size, 1.0f);
    Z.transform().setScale(1.0f, 1.0f, mSize);

    mSceneController.getScene().addToScene(X,Z);


//		X.physicsBody().setMass(size);
//		Y.physicsBody().setMass(size);
//		Z.physicsBody().setMass(size);
//		Node axis = new Node();
//		axis.addChild(X);
//		axis.addChild(Y);
//		axis.addChild(Z);
    return null;
  }

  public void setSize(float aSize)
  {
    mSize = aSize;
    xMin = xMax = yMin = yMax = zMin = zMax = 0;

  }
}
