package rmx.engine;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL11.*;


import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import rmx.Bugger;
import rmx.RMXObject;
import rmx.engine.math.Matrix4;
import rmx.engine.math.Vector3;


public class Geometry extends RMXObject {

	private ByteBuffer _elements;
	private ShortBuffer _indexData;
	private int _e = 0;
	private Geometry(int size) {
		_elements = BufferUtils.createByteBuffer(size);
		_indexData = BufferUtils.createShortBuffer(size / 3);
	}
	
	private ByteBuffer vertices() {
		return _elements;
	}
	
	private ShortBuffer indexData() {
		return _indexData;
	}
	public void addVertex(Vector3f v) {
		_cube.addVertex(v.x, v.y, v.z);
	}
	
	public void addVertex(float x, float y, float z)  {
		if (_e >= _elements.capacity())
			System.out.println("ERROR: TOO MANY ELEMENTS");
		_elements.put((byte) x);
		_elements.put((byte) y);
		_elements.put((byte) z);
//		_elements[_e++] = x;
//		_elements[_e++] = y;
//		_elements[_e++] = z;
		
	}
	public void prepare() {
		_elements.flip();
		_indexData.flip();
	}
	private Matrix4 _modelView = new Matrix4();
	private void pushMatrx(Matrix4 m) {
//		  Vector3 a = this.transform().localEulerAngles();
		  GL11.glPushMatrix();
//		  GL11.glMultMatrixf(m.buffer());
	         GL11.glTranslatef(m.m30, m.m31,m.m32);
	         
//	         GL11.glRotatef(a.x,);
//	         GL11.glRotatef(a.y, a.x, a.y, a.z);
//	         GL11.glRotatef(a.z, a.x, a.y, a.z);

	         GL11.glColor3f(0.5f,0.5f,0.1f);  
	}
	
	private void enableTexture() {
		 glEnable(GL_TEXTURE_2D); //Enable texture
//         glBindTexture(GL_TEXTURE_2D,text2D);//Binding texture
	}
	private void popMatrix() {
//		 glDisable(GL_TEXTURE_2D);//TODO perhaps?
		
		GL11.glPopMatrix();
	}
	public void render(Node node, Matrix4 modelMatrix) {
//		 GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
//		         GL11.glLoadIdentity();
		_modelView.set(node.transform.worldMatrix());
		_modelView.mul(modelMatrix);
		        this.pushMatrx(_modelView);
		         GL11.glBegin(GL11.GL_QUADS);    
		            GL11.glColor3f(1.0f,1.0f,0.0f);           
		            GL11.glVertex3f( 1.0f, 1.0f,-1.0f);        
		            GL11.glVertex3f(-1.0f, 1.0f,-1.0f);        
		            GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
		            GL11.glVertex3f( 1.0f, 1.0f, 1.0f);  
		            GL11.glColor3f(1.0f,0.5f,0.0f);            
		            GL11.glVertex3f( 1.0f,-1.0f, 1.0f);
		            GL11.glVertex3f(-1.0f,-1.0f, 1.0f);
		            GL11.glVertex3f(-1.0f,-1.0f,-1.0f);
		            GL11.glVertex3f( 1.0f,-1.0f,-1.0f);
		            GL11.glColor3f(1.0f,0.0f,0.0f);
		            GL11.glVertex3f( 1.0f, 1.0f, 1.0f);
		            GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
		            GL11.glVertex3f(-1.0f,-1.0f, 1.0f);
		            GL11.glVertex3f( 1.0f,-1.0f, 1.0f);
		            GL11.glColor3f(1.0f,1.0f,0.0f);
		            GL11.glVertex3f( 1.0f,-1.0f,-1.0f);
		            GL11.glVertex3f(-1.0f,-1.0f,-1.0f);
		            GL11.glVertex3f(-1.0f, 1.0f,-1.0f);
		            GL11.glVertex3f( 1.0f, 1.0f,-1.0f);
		            GL11.glColor3f(0.0f,0.0f,1.0f);
		            GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
		            GL11.glVertex3f(-1.0f, 1.0f,-1.0f);
		            GL11.glVertex3f(-1.0f,-1.0f,-1.0f);
		            GL11.glVertex3f(-1.0f,-1.0f, 1.0f);
		            GL11.glColor3f(1.0f,0.0f,1.0f);
		            GL11.glVertex3f( 1.0f, 1.0f,-1.0f);
		            GL11.glVertex3f( 1.0f, 1.0f, 1.0f);
		            GL11.glVertex3f( 1.0f,-1.0f, 1.0f);
		            GL11.glVertex3f( 1.0f,-1.0f,-1.0f);
		        GL11.glEnd();    
		       this.popMatrix();
		 }
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
			Bugger.logAndPrint("Cube with " + 6*3*4 + " elements", true);
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
		}
		return _cube;
	}
	
	
}
