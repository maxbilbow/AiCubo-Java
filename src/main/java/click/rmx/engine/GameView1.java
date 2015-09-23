package click.rmx.engine;


import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
//import org.lwjgl.util.Display;
//import org.lwjgl.util.glu.GLU;

import click.rmx.RMX;
import click.rmx.engine.gl.GameView;
import click.rmx.engine.gl.GLView;
import java.nio.FloatBuffer;
import static org.lwjgl.opengl.GL11.*;

@Deprecated
public class GameView1 extends GameView implements GLView{

	@Override
	protected void onAwake() {
		this.setDebugging(true, 60);
		this.setPerFramDebugInfo(RMX::rmxGarbageCollectorInfo);
		this.setClearColor(0.3f, 0.3f, 0.3f, 0.3f);
	}

	@Override
	public void initGL(long window) {		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); 
		GL11.glClearDepth(1.0); 
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL); 

		setUpLighting();
	}


	protected void setUpLighting() {
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

	@Override
	protected void renderFrame(Scene scene) {
		
		scene.updateSceneLogic();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		pointOfView().camera().perspective(this);

		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer
		
		glMatrixMode(GL_MODELVIEW);
		glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);
		glLoadIdentity();  

		scene.renderScene(pointOfView().camera().makeLookAt());
		
	}



}