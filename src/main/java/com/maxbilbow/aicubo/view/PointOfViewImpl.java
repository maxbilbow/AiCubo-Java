package com.maxbilbow.aicubo.view;

import com.maxbilbow.aicubo.control.CameraController;
import com.maxbilbow.aicubo.engine.behaviours.Behaviour;
import com.maxbilbow.aicubo.model.Camera;
import com.maxbilbow.aicubo.model.Node;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by Max on 06/05/2016.
 */
@Component
public class PointOfViewImpl implements PointOfView
{
  private Node mPointOfView;

  @Resource
  private CameraController mCameraController;

  @Override
  public boolean set(Node pointOfView)
  {
    if (pointOfView.camera() == null)
    {
      pointOfView.setCamera(new Camera());
      pointOfView.addBehaviour(new Behaviour()
      {

        @Override
        public void broadcastMessage(String message, Object args)
        {

          if (message == "lookUp")
          {
            //					mLogger.debug(message + ": "+ args , false);
            pointOfView.transform().rotate("pitch", (float) args);
          }
        }

        @Override
        public boolean hasLateUpdate()
        {
          return false;
        }

        @Override
        protected void onAwake()
        {

        }

        @Override
        public void update(Node node)
        {
          // TODO Auto-generated method stub

        }

      });
    }
    else if (mPointOfView == pointOfView)
    {
      return false;
    }
    if (mPointOfView != null)
    {
      mPointOfView.camera().stopListening();
    }

    mCameraController.setCameraNode(mPointOfView = pointOfView);
    mPointOfView.camera().startListening();
    return true;
  }

  @Override
  public Node get()
  {
    return mPointOfView;
  }

}
