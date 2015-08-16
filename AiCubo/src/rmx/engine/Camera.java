package rmx.engine;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;
import static rmx.RMX.*;

import rmx.engine.math.Matrix4;
import rmx.engine.math.Vector3;
import rmx.gl.GLView;

public class Camera extends NodeComponent {
	private float fovX, fovY, nearZ, farZ, aspect;
	private Matrix4 _projectionMatrix = new Matrix4();
	
	public Camera() {
		super();
		this.stopListening();
		this.fovX = this.fovY = 45;
		this.nearZ = 1;
		this.farZ = 2000;
		this.aspect = 16/9;
		
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
       

//		
		 Matrix4 m = (Matrix4)this.getNode().transform.worldMatrix().clone();
		 Matrix4 root = Scene.getCurrent().rootNode.transform.localMatrix();
		 root.set(m);
		 m.mul(root);
//		 root.negate();
//		 m.mul(Scene.getCurrent().rootNode.transform.localMatrix());
//	        m.set(this.getNode().transform.localMatrix());
//	        m.negatePosition();
	     glMultMatrixf(m.buffer());
	}

	
	public Matrix4 modelViewMatrix() {
		return this.getNode().transform.localMatrix();
	}

	public void onEventDidEnd(String event, Object args) {
		if (event == END_OF_GAMELOOP) {
			this.modelViewMatrix().resetBuffers();
//			System.out.println("Event: " + event);
		}
	}

}
