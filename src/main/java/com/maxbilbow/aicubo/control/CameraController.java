package com.maxbilbow.aicubo.control;

import com.maxbilbow.aicubo.model.Node;
import org.springframework.stereotype.Component;

/**
 * Created by Max on 06/05/2016.
 */
@Component
public class CameraController
{
  private Node mCameraNode;

  public Node getCameraNode()
  {
    return mCameraNode;
  }

  public void setCameraNode(Node aCameraNode)
  {
    if (aCameraNode.camera() == null)
    {
      throw new RuntimeException("Node had no camera: " + aCameraNode);
    }
    mCameraNode = aCameraNode;
  }
}
