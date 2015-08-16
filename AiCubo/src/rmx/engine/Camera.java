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
        Matrix4 m = (Matrix4)getNode().transform.localMatrix().clone();
        m.set(this.getNode().transform.localMatrix());
//        m.negate();
        glMultMatrixf(m.buffer());
	}

	
	public Matrix4 modelViewMatrix() {
		return this.getNode().transform.localMatrix();
	}
	public Matrix4 look() {
		Matrix4 root = Scene.getCurrent().rootNode.transform.localMatrix();
		Transform t = this.getNode().transform;
//		glMultMatrixf(m.buffer());
		root.set(t.localMatrix());
		Vector3 p = (Vector3) t.position().clone();
		p.negate();
		root.setTranslation(p);	
		System.out.println(root);
//		t.localMatrix().setIdentity();
		return root;
	}
	
	public void onEventDidEnd(String event, Object args) {
		if (event == END_OF_GAMELOOP) {
			this.modelViewMatrix().resetBuffers();
//			System.out.println("Event: " + event);
		}
	}

}
