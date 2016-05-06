package com.maxbilbow.aicubo.view;


import com.maxbilbow.aicubo.engine.geometry.Shape;
import com.maxbilbow.aicubo.engine.geometry.Shapes;
import com.maxbilbow.aicubo.engine.gl.Shader;
import org.lwjgl.BufferUtils;
import org.springframework.stereotype.Component;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;


//import org.lwjgl.util.glu.GLU;
@Component
public class GLInitializerImpl implements GLInitializer
{

  Shader shader;
  public static final int
          UNIFORM_VIEWPROJECTION_MATRIX = 0,
          UNIFORM_NORMAL_MATRIX         = 1,
          UNIFORM_MODEL_MATRIX          = 2,
          UNIFORM_SCALE_VECTOR          = 3,
          UNIFORM_TIME_FLOAT            = 4,
          UNIFORM_COLOR_VECTOR          = 5;
  int[] uniforms = new int[6];

  int vertexArray;

//  public GLViewImpl()
//  {
//
//  }


  public static final int
          ERR_SHADER_LOAD      = 1,
          ERR_SHADER_EXCEPTION = 2,
          ERR_RENDER           = 3;

  private void initShaders()
  {
    try
    {
      shader = new Shader(Shader.DEFAULT);
      for (Shape shape : Shapes.values())
      {
        shape.initBuffers();
      }

    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.exit(ERR_SHADER_EXCEPTION);
    }
    finally
    {
      if (shader.vertexShader == 0 || shader.fragmentShader == 0 || shader.program == 0)
      {
        System.err.println("Shaders failed to initialize");
        System.exit(ERR_SHADER_LOAD);
      }
    }
  }

  @Override
  public void initGL(long window)
  {

    initShaders();

    glEnable(GL_TEXTURE_2D);
    glShadeModel(GL_SMOOTH);
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    glClearDepth(1.0);
    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LEQUAL);

    setUpLighting();
  }

  protected void bindAttributes()
  {

  }


  private void setUpLighting()
  {
    glShadeModel(GL_SMOOTH);
    glEnable(GL_DEPTH_TEST);
    glEnable(GL_LIGHTING);
    glEnable(GL_LIGHT0);
    FloatBuffer fb = BufferUtils.createFloatBuffer(4);
    fb.put(new float[]{0.05f, 0.05f, 0.05f, 1f});
    fb.flip();
    glLightModelfv(GL_LIGHT_MODEL_AMBIENT, fb);
    FloatBuffer fp = BufferUtils.createFloatBuffer(4);
    fp.put(new float[]{50, 100, 50, 1});
    fp.flip();
    glLightfv(GL_LIGHT0, GL_POSITION, fp);
    glEnable(GL_CULL_FACE);
    glCullFace(GL_BACK);
    glEnable(GL_COLOR_MATERIAL);
    glColorMaterial(GL_FRONT, GL_DIFFUSE);
  }




}
