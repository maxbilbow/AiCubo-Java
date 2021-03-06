package click.rmx.engine;

import static org.lwjgl.opengl.GL11.*;

import click.rmx.engine.gl.GLView;
import click.rmx.engine.math.Matrix4;

public class Camera extends ANodeComponent {
	private float fovX, fovY, nearZ, farZ, aspect;
	private Matrix4 _projectionMatrix = new Matrix4();
	
	public Camera() {
		super();
		this.stopListening();
		this.fovX = this.fovY = 65;
		this.nearZ = 1;
		this.farZ = 2000;
		this.aspect = 1;
		
	}

	public void setAspect(int width, int height) {
		this.aspect = width / height;
	}
	public float getFarZ() {
		return farZ;
	}

	public void setFarZ(float farZ) {
		this.farZ = farZ;
	}

	public float getFovX() {
		return fovX;
	}

	public void setFovX(float fovX) {
		this.fovX = fovX;
	}

	public float getNearZ() {
		return nearZ;
	}

	public void setNearZ(float nearZ) {
		this.nearZ = nearZ;
	}

	public Matrix4 projectionMatrix() {	
		return _projectionMatrix;
	}
	
	public void perspective(GLView view) {
		aspect = view.width() / view.height();
        double fW, fH;

        //fH = tan( (fovY / 2) / 180 * pi ) * zNear;
        fH = Math.tan( fovY / 360 * Math.PI ) * nearZ;
        fW = fH * aspect;

        glFrustum( -fW, fW, -fH, fH, nearZ, farZ );
       

        	
		 Matrix4 m = this.getNode().transform().worldMatrix().clone();

		 m.setPosition(0,0,0);
		 m.invert();

	     glMultMatrixf(m.rowBuffer());
	     this.modelViewMatrix().resetBuffers();
	}
	
	public Matrix4 modelViewMatrix() {
		
		return this.getNode().transform().worldMatrix().clone();
	}
	
	public Matrix4 makeLookAt() {
		Matrix4 m = this.modelViewMatrix();
//		 m.invert();

//		  glMultMatrixf(m.rowBuffer());
			m.negate();
			 float x = m.m30, y = m.m31, z = m.m32;
//	 m.setPosition(0,0,0);
	
//	 GL11.glPushMatrix();
//			 glMultMatrixf(m.rowBuffer());
	 glTranslatef(
			 x,
			 y,
			 z
			 );
	

		  return m;
	}

	@Override
	protected void onAwake() {
		// TODO Auto-generated method stub
		
	}

//	public void onEventDidEnd(String event, Object args) {
//		if (event == END_OF_GAMELOOP) {
//			
////			System.out.println("Event: " + event);
//		}
//	}

}
