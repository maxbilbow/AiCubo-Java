package click.rmx.engine;


import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;
import java.util.Map;
import java.util.Set;

import org.lwjgl.BufferUtils;

import click.rmx.engine.gl.GameView;
import click.rmx.engine.gl.Shader;
import click.rmx.engine.math.Matrix4;
import click.rmx.engine.geometry.Geometry;
import click.rmx.engine.geometry.Shape;
import click.rmx.engine.geometry.Shapes;
import click.rmx.engine.gl.GLView;


//import org.lwjgl.util.glu.GLU;

public class GLShaderView extends GameView implements GLView {

	Shader shader;
	public static final int 
	UNIFORM_VIEWPROJECTION_MATRIX = 0,
	UNIFORM_NORMAL_MATRIX = 1,
	UNIFORM_MODEL_MATRIX = 2,
	UNIFORM_SCALE_VECTOR = 3,
	UNIFORM_TIME_FLOAT = 4,
	UNIFORM_COLOR_VECTOR = 5
	;
	int[] uniforms = new int[6];

	int vertexArray;

	@Override
	protected void onAwake() {
		this.setDebugging(true, 60);
		this.setClearColor(0.3f, 0.3f, 0.8f, 1.0f);
	}
	public static final int 
	ERR_SHADER_LOAD = 1, 
	ERR_SHADER_EXCEPTION = 2,
	ERR_RENDER = 3;
	private void initShaders() {
		try {
			shader = new Shader(Shader.DEFAULT);
			for (Shape shape : Shapes.values()) {
				shape.initBuffers();
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(ERR_SHADER_EXCEPTION);
		} finally {
			if(shader.vertexShader == 0 || shader.fragmentShader == 0 || shader.program == 0) {
				System.err.println("Shaders failed to initialize");
				System.exit(ERR_SHADER_LOAD);
			}
		}
	}

	@Override
	public void initGL(long window) {

		initShaders();

		glEnable(GL_TEXTURE_2D);
		glShadeModel(GL_SMOOTH);
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f); 
		glClearDepth(1.0); 
		glEnable(GL_DEPTH_TEST);
		glDepthFunc(GL_LEQUAL); 

		setUpLighting();
	}

	protected void bindAttributes() {

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
		Map<Shape,Set<Geometry>> shapes = scene.rootNode().getGeometries();
		try {
			this.bindAttributes();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(ERR_RENDER);
		} finally {
			glMatrixMode(GL_PROJECTION);
			glLoadIdentity();
			pointOfView().camera().perspective(this);
			

			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer



			glMatrixMode(GL_MODELVIEW);
			glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
			glLoadIdentity();  

			scene.renderScene(null);
			Matrix4 m = pointOfView().camera().makeLookAt();
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






}
