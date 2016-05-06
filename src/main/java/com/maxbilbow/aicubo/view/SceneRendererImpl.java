package com.maxbilbow.aicubo.view;

import com.maxbilbow.aicubo.control.SceneController;
import com.maxbilbow.aicubo.engine.RenderDelegate;
import com.maxbilbow.aicubo.engine.geometry.Geometry;
import com.maxbilbow.aicubo.engine.geometry.Shape;
import com.maxbilbow.aicubo.engine.math.Matrix4;
import com.maxbilbow.aicubo.engine.physics.CollisionManager;
import com.maxbilbow.aicubo.engine.physics.PhysicsWorld;
import com.maxbilbow.aicubo.model.Camera;
import com.maxbilbow.aicubo.model.RootNode;
import com.maxbilbow.aicubo.model.Scene;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Max on 06/05/2016.
 */
@Component
public class SceneRendererImpl implements SceneRenderer
{

  @Resource
  private PointOfView mPointOfView;

  @Resource
  private SceneController mSceneController;

  private Collection<RenderDelegate> mRenderDelegates = new HashSet<>();

  @Resource
  private CollisionManager mCollisionManager;

  @Resource
  private PhysicsWorld     mPhysicsWorld;

  @Override
  public void addDelegate(RenderDelegate aRenderDelegate)
  {
    mRenderDelegates.add(aRenderDelegate);
  }

  @Override
  public boolean removeDelegate(RenderDelegate aRenderDelegate)
  {
    return mRenderDelegates.remove(aRenderDelegate);
  }

  @Override
  public void renderFrame(int aWidth, int aHeight)
  {

    final Camera aCamera = mPointOfView.get().camera();

    final Scene scene = mSceneController.getScene();
    mRenderDelegates.forEach(RenderDelegate::updateBeforeSceneLogic);

    final RootNode rootNode = mSceneController.getScene().getRootNode();
    scene.updateLogic();
    mPhysicsWorld.updatePhysics(rootNode);
    mCollisionManager.updateCollisionEvents(rootNode);
    scene.updateAfterPhysics();

    mRenderDelegates.forEach(RenderDelegate::updateBeforeSceneRender);

    Map<Shape, Set<Geometry>> shapes = rootNode.getGeometries();

    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    aCamera.perspective(aWidth,aHeight);


    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer


    glMatrixMode(GL_MODELVIEW);
    glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    glLoadIdentity();


    scene.renderScene(null);
    Matrix4 m = aCamera.makeLookAt();
    shapes.forEach((shape, geometries) -> {
      geometries.forEach(geo -> {
//					GL11.glDrawElements(
//							GL_TRIANGLES,
//							shape.indexSize(),
//							GL_UNSIGNED_SHORT,
//							shape.getIndexBuffer());
        geo.render();
      });
    });

  }

}
