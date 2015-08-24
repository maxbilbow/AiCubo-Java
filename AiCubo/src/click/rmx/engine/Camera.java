package click.rmx.engine;

import static click.rmx.RMX.*;
import static org.lwjgl.opengl.GL11.*;

import click.rmx.engine.gl.GLView;
import click.rmx.engine.math.Matrix4;

public class Camera extends NodeComponent {
	private float fovX, fovY, nearZ, farZ, aspect;
	private Matrix4 _projectionMatrix = new Matrix4();
	
	public Camera() {
		super();
		this.stopListening();
		this.fovX = this.fovY = 45;
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
       

        	
		 Matrix4 m = (Matrix4)this.getNode().transform.worldMatrix().clone();
//		 Bugger.logAndPrint("\n"+m, false);
		 m.setPosition(0,0,0);
		 m.invert();
//		 Bugger.logAndPrint("\n"+m, false);
//		 m.mul(Scene.getCurrent().rootNode.transform.localMatrix());
//	        m.set(this.getNode().transform.localMatrix());
//	        m.negatePosition();
		 
//		 m.setIdentity();
	     glMultMatrixf(m.rowBuffer());
	}
	
	public Matrix4 modelViewMatrix() {

		Matrix4 m = (Matrix4)this.getNode().transform.worldMatrix().clone();
//		Bugger.logAndPrint("\n"+m, false);
		m.negate();
//		Bugger.logAndPrint("\n"+m, false);
		return m;
	}

	public void onEventDidEnd(String event, Object args) {
		if (event == END_OF_GAMELOOP) {
			this.modelViewMatrix().resetBuffers();
//			System.out.println("Event: " + event);
		}
	}

}
