package click.rmx.engine;

import static org.lwjgl.opengl.GL11.*;


import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import click.rmx.RMXObject;
import click.rmx.engine.gl.Mesh;
import click.rmx.engine.gl.Tree;
import click.rmx.engine.math.EulerAngles;
import click.rmx.engine.math.Matrix4;
import click.rmx.engine.math.Vector3;


public abstract class Geometry extends RMXObject {



	//	float [] vertices, colors, normals;
	//	int[] indices;
	ByteBuffer vBuffer, cBuffer, nBuffer, iBuffer;
	public Mesh mesh;
	//	public Tree<Mesh> tree;

	private int _e = 0;

	public Geometry(Mesh mesh) {
		this.mesh = mesh;
	}
	public Geometry() {
	}

	private Matrix4 _modelView = new Matrix4();


	//	private void enableTexture() {
	//		 glEnable(GL_TEXTURE_2D); //Enable texture
	////         glBindTexture(GL_TEXTURE_2D,text2D);//Binding texture
	//	}

	private void initWithArrays(float[] verts, float[]normals, float[] colors) {
		this.vBuffer = BufferUtils.createByteBuffer(verts.length);
		this.nBuffer = BufferUtils.createByteBuffer(normals.length);
		this.cBuffer = BufferUtils.createByteBuffer(colors.length);

		for (float v : verts)
			vBuffer.put((byte) v);
		for (float n : normals)
			nBuffer.put((byte) n);
		for (float c : colors)
			cBuffer.put((byte) c);


		vBuffer.flip();
		nBuffer.flip();
		cBuffer.flip();
	}
	public Geometry(float[] verts, float[]normals, float[] colors, int[]indices) {

		this.initWithArrays(verts, normals, colors);
		this.iBuffer = BufferUtils.createByteBuffer(indices.length);
		for (float i : indices)
			iBuffer.put((byte) i);

		iBuffer.flip();

	}
	public Geometry(float[] verts, float[] normals, float[] colors) {
		this.initWithArrays(verts, normals, colors);
	}
	public void drawArrays(Node node) {
		// enable and specify pointers to vertex arrays
		glEnableClientState(GL_NORMAL_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);
		glEnableClientState(GL_VERTEX_ARRAY);

		glNormalPointer(GL_FLOAT, 0, nBuffer);
		glColorPointer(3, GL_FLOAT, 0, cBuffer);
		glVertexPointer(3, GL_FLOAT, 0, vBuffer);

		glPushMatrix();

		Vector3 pos = node.transform.position();
		glTranslatef(pos.x,pos.y,pos.z);                // move to bottom-left corner

		Vector3 a = node.transform.eulerAngles();

		a.scale(1 / click.rmx.RMX.PI_OVER_180);
		glRotatef(a.x, 1,0,0);
		glRotatef(a.y, 0,1,0);
		glRotatef(a.z, 0,0,1);

		Vector3 scale = node.transform.scale();
		glScalef(scale.x, scale.y, scale.z);

		if (iBuffer != null)
			glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_BYTE, iBuffer);
		else
			glDrawArrays(GL_TRIANGLES, 0, 36);

		glPopMatrix();

		glDisableClientState(GL_VERTEX_ARRAY);  // disable vertex arrays
		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_NORMAL_ARRAY);
	}
	public void render(Node node) {//, Object modelMatrix) {

		if (this.vBuffer != null) {
			drawArrays(node);
			return;
		}
		_modelView.set(node.transform.worldMatrix());

		Vector3 modelA = node.transform.eulerAngles();
		//		EulerAngles modelB = base.eulerAngles();
		modelA.scale(1 / click.rmx.RMX.PI_OVER_180);

		glPushMatrix();

		glTranslatef(
				_modelView.m30,// + m.m30, 
				_modelView.m31,// + m.m31,
				_modelView.m32 // + m.m32
				);

		//		GL11.glTranslatef(
		//				 m.m30,
		//				 m.m31,
		//				 m.m32
		//				 );
		glRotatef(modelA.x, 1,0,0);
		glRotatef(modelA.y, 0,1,0);
		glRotatef(modelA.z, 0,0,1);

		float 
		X = node.transform.scale().x,
		Y = node.transform.scale().y,
		Z = node.transform.scale().z;
		drawWithScale(X, Y, Z);

		GL11.glPopMatrix();

	}
	protected abstract void drawWithScale(float x, float y, float z);

	public void _render() {
		//this.pushMatrx();
		//		GL11.glVertex3dv(_elements);
		//glColor3f(0.0f,0.0f,1.0f);
		//GL11.glDrawElements(GL_QUADS, _elements);
		//this.popMatrix();
	}
	private static Geometry _cube;

	public static Geometry cube() {
		if (_cube != null){
			return _cube;
		} else {
			_cube = new Geometry() {//Mesh.vertices1, Mesh.normals1, Mesh.colors1) {
				@Override
				protected void drawWithScale(float X, float Y, float Z) {
					GL11.glBegin(GL11.GL_QUADS);    
					GL11.glColor3f(1.0f,1.0f,0.0f);   
					glNormal3f(0,1,0);
					GL11.glVertex3f( X, Y,-Z);        
					GL11.glVertex3f(-X, Y,-Z);        
					GL11.glVertex3f(-X, Y, Z);
					GL11.glVertex3f( X, Y, Z);  
					GL11.glColor3f(1.0f,0.5f,0.0f);  
					glNormal3f(0,-1,0);
					GL11.glVertex3f( X,-Y, Z);
					GL11.glVertex3f(-X,-Y, Z);
					GL11.glVertex3f(-X,-Y,-Z);
					GL11.glVertex3f( X,-Y,-Z);
					GL11.glColor3f(1.0f,0.0f,0.0f);
					glNormal3f(0,0,1);
					GL11.glVertex3f( X, Y, Z);
					GL11.glVertex3f(-X, Y, Z);
					GL11.glVertex3f(-X,-Y, Z);
					GL11.glVertex3f( X,-Y, Z);
					GL11.glColor3f(1.0f,1.0f,0.0f);
					glNormal3f(0,0,-1);
					GL11.glVertex3f( X,-Y,-Z);
					GL11.glVertex3f(-X,-Y,-Z);
					GL11.glVertex3f(-X, Y,-Z);
					GL11.glVertex3f( X, Y,-Z);
					GL11.glColor3f(0.0f,0.0f,1.0f);
					glNormal3f(-1,0,0);
					GL11.glVertex3f(-X, Y, Z);
					GL11.glVertex3f(-X, Y,-Z);
					GL11.glVertex3f(-X,-Y,-Z);
					GL11.glVertex3f(-X,-Y, Z);
					GL11.glColor3f(1.0f,0.0f,1.0f);
					glNormal3f(1,0,0);
					GL11.glVertex3f( X, Y,-Z);
					GL11.glVertex3f( X, Y, Z);
					GL11.glVertex3f( X,-Y, Z);
					GL11.glVertex3f( X,-Y,-Z);
					GL11.glEnd();    
				}

			};

		}
		/*Bugger.logAndPrint("Cube with " + 6*3*4 + " elements", true);
			_cube = new Geometry(6 * 3 * 4);
			_cube.addVertex( 1.0f, 1.0f,-1.0f);        
            _cube.addVertex(-1.0f, 1.0f,-1.0f);        
            _cube.addVertex(-1.0f, 1.0f, 1.0f);
            _cube.addVertex( 1.0f, 1.0f, 1.0f);  
//            GL11.glColor3f(1.0f,0.5f,0.0f);            
            _cube.addVertex( 1.0f,-1.0f, 1.0f);
            _cube.addVertex(-1.0f,-1.0f, 1.0f);
            _cube.addVertex(-1.0f,-1.0f,-1.0f);
            _cube.addVertex( 1.0f,-1.0f,-1.0f);
//            GL11.glColor3f(1.0f,0.0f,0.0f);
            _cube.addVertex( 1.0f, 1.0f, 1.0f);
            _cube.addVertex(-1.0f, 1.0f, 1.0f);
            _cube.addVertex(-1.0f,-1.0f, 1.0f);
            _cube.addVertex( 1.0f,-1.0f, 1.0f);
//            GL11.glColor3f(1.0f,1.0f,0.0f);
            _cube.addVertex( 1.0f,-1.0f,-1.0f);
            _cube.addVertex(-1.0f,-1.0f,-1.0f);
            _cube.addVertex(-1.0f, 1.0f,-1.0f);
            _cube.addVertex( 1.0f, 1.0f,-1.0f);
//            GL11.glColor3f(0.0f,0.0f,1.0f);
            _cube.addVertex(-1.0f, 1.0f, 1.0f);
            _cube.addVertex(-1.0f, 1.0f,-1.0f);
            _cube.addVertex(-1.0f,-1.0f,-1.0f);
            _cube.addVertex(-1.0f,-1.0f, 1.0f);
//            GL11.glColor3f(1.0f,0.0f,1.0f);
            _cube.addVertex( 1.0f, 1.0f,-1.0f);
            _cube.addVertex( 1.0f, 1.0f, 1.0f);
            _cube.addVertex( 1.0f,-1.0f, 1.0f);
            _cube.addVertex( 1.0f,-1.0f,-1.0f);
            _cube.prepare();
		}*/
		return _cube;
	}


}
