package click.rmx.engine;

import static org.lwjgl.opengl.GL11.*;


import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import click.rmx.RMXObject;
import click.rmx.engine.math.EulerAngles;
import click.rmx.engine.math.Matrix4;


public abstract class Geometry extends RMXObject {

	private ByteBuffer _elements;
	private ShortBuffer _indexData;
	private int _e = 0;
	public Geometry(int size) {
		_elements = BufferUtils.createByteBuffer(size);
		_indexData = BufferUtils.createShortBuffer(size / 3);
	}
	
	ByteBuffer vertices() {
		return _elements;
	}
	
	ShortBuffer indexData() {
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
	private void pushMatrx(Node node, Matrix4 base) {
//		Matrix4 m = (Matrix4) modelViewMatrix.clone();
		_modelView.set(node.transform.worldMatrix());
//		AxisAngle4f baseA = new AxisAngle4f();
//		baseA.set(base);
//		AxisAngle4f modelA = new AxisAngle4f();
//		modelA.set(_modelView);
		
		EulerAngles baseA = base.eulerAngles();
		EulerAngles modelA = _modelView.eulerAngles();
		baseA.scale(1 / click.rmx.RMX.PI_OVER_180);
		modelA.scale(1 / click.rmx.RMX.PI_OVER_180);
		
		glPushMatrix();
		
		
//		glRotatef(baseA.angle, baseA.x, baseA.y, baseA.z);
//		glRotatef(modelA.angle, modelA.x, modelA.y, modelA.z);
		
//		glRotatef(baseA.x, 1,0,0);
//        glRotatef(baseA.y, 0,1,0);
//        glRotatef(baseA.z, 0,0,1);
		
		
        
		
//		 glMultMatrixf(_modelView.buffer())
		glTranslatef(
				base.m30, 
				base.m31,
				base.m32
				);
		
		
		glTranslatef(
				_modelView.m30, 
				_modelView.m31,
				_modelView.m32
				);
		glRotatef(modelA.x, 1,0,0);
        glRotatef(modelA.y, 0,1,0);
        glRotatef(modelA.z, 0,0,1);
		
		
		
//		Bugger.logAndPrint("\n BASEA: "+ baseA, false);
//		Bugger.logAndPrint("\nMODELA: "+ modelA, false);
	    
	         
	         

	         
	}
	
//	private void enableTexture() {
//		 glEnable(GL_TEXTURE_2D); //Enable texture
////         glBindTexture(GL_TEXTURE_2D,text2D);//Binding texture
//	}
	private void popMatrix() {
//		 glDisable(GL_TEXTURE_2D);//TODO perhaps?
		
		GL11.glPopMatrix();
	}
	public void render(Node node, Matrix4 modelMatrix) {
//		 GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
//		         GL11.glLoadIdentity();
		
		        this.pushMatrx(node, modelMatrix);
		        float 
		        X = node.transform.scale().x,
		        Y = node.transform.scale().y,
		        Z = node.transform.scale().z;
		        drawWithScale(X, Y, Z);
		         
		       this.popMatrix();
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
			_cube = new Geometry(6*3*4) {
				@Override
				protected void drawWithScale(float X, float Y, float Z) {
					GL11.glBegin(GL11.GL_QUADS);    
		            GL11.glColor3f(1.0f,1.0f,0.0f);           
		            GL11.glVertex3f( X, Y,-Z);        
		            GL11.glVertex3f(-X, Y,-Z);        
		            GL11.glVertex3f(-X, Y, Z);
		            GL11.glVertex3f( X, Y, Z);  
		            GL11.glColor3f(1.0f,0.5f,0.0f);            
		            GL11.glVertex3f( X,-Y, Z);
		            GL11.glVertex3f(-X,-Y, Z);
		            GL11.glVertex3f(-X,-Y,-Z);
		            GL11.glVertex3f( X,-Y,-Z);
		            GL11.glColor3f(1.0f,0.0f,0.0f);
		            GL11.glVertex3f( X, Y, Z);
		            GL11.glVertex3f(-X, Y, Z);
		            GL11.glVertex3f(-X,-Y, Z);
		            GL11.glVertex3f( X,-Y, Z);
		            GL11.glColor3f(1.0f,1.0f,0.0f);
		            GL11.glVertex3f( X,-Y,-Z);
		            GL11.glVertex3f(-X,-Y,-Z);
		            GL11.glVertex3f(-X, Y,-Z);
		            GL11.glVertex3f( X, Y,-Z);
		            GL11.glColor3f(0.0f,0.0f,1.0f);
		            GL11.glVertex3f(-X, Y, Z);
		            GL11.glVertex3f(-X, Y,-Z);
		            GL11.glVertex3f(-X,-Y,-Z);
		            GL11.glVertex3f(-X,-Y, Z);
		            GL11.glColor3f(1.0f,0.0f,1.0f);
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