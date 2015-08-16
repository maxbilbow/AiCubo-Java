package rmx.engine;

import javax.vecmath.AxisAngle4f;
//import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import rmx.Bugger;
import rmx.engine.math.Matrix4;
//import static rmx.db.RMX;
import rmx.engine.math.Vector3;



public class Transform extends NodeComponent {
	
	public final Node node;
	
	private Matrix4 _rMatrix = new Matrix4();
	private Matrix4 _worldMatrix = new Matrix4();
	private Quat4f   _quaternion = new Quat4f();
	private Vector3f _eulerAngles = new Vector3f();
	public Transform(Node node) {
		this.node = node;
		this._localMatrix = new Matrix4();
		this._localMatrix.setIdentity();
		
		this._axis = new Matrix4();
		this._axis.setIdentity();
	}
	
	private Matrix4 _axis;
	private Matrix4 _localMatrix;
	
	/**
	 * TODO probably doesnt work. How do you do this maths?
	 * @return
	 */
	public Matrix4 worldMatrix() {
		Transform parent = this.parent();
		if (parent != null) {
			_worldMatrix.set(this._localMatrix);//.clone();
			_worldMatrix.mul(this.parent().worldMatrix());
			return _worldMatrix;
		} else {
			return this._localMatrix;
		}
	}
	
	public Transform parent() {
		Node parentNode = this.node.getParent();
		return parentNode != null ? this.node.getParent().transform : null;
	}
	public Vector3f localPosition() {
		return _localMatrix.position();
	}
	
	public Vector3f position() {
		Transform parent = this.parent();
		if (parent != null) {
			Vector3f result = (Vector3f) this.localPosition().clone();
			result.add(parent.position());
			return result;
		}
		return this.localPosition();
	}
	
	public void setPosition(Vector3f position) {
		this._localMatrix.m30 = position.x;
		this._localMatrix.m31 = position.y;
		this._localMatrix.m32 = position.z;
	}
	
	public void setPosition(float x, float y, float z) {
		this._localMatrix.m30 = x;
		this._localMatrix.m31 = y;
		this._localMatrix.m32 = z;
	}
	
	
	
	public void move(String args) {
		String[] options = args.split(":");
		String direction = options[0];
		float scale = (float) (Float.parseFloat(options[1]) * 0.1);
		if (_localMatrix.translate(direction, scale)) {
			Bugger.logAndPrint("\n"+ this.worldMatrix(), false);
			return;
		}
		switch (direction) {
		case "pitch":
			this.rotateAround(_localMatrix.left(), scale);
			break;
		case "yaw":
			this.rotateAround(_localMatrix.up(), scale);
			break;
		case "roll":
			this.rotateAround(_localMatrix.forward(), scale);
			break;
		}
//		Transform child = node.getChildren().get(0).transform;
//		Bugger.logAndPrint("\nParent: \n" + this.worldMatrix() + 
//				"\nChild: \n" + child.worldMatrix(), false);
		Bugger.logAndPrint("\n"+ this.worldMatrix(), false);
	}
	
	public Quat4f quaternion() {
		_quaternion.set(this.worldMatrix());
		return _quaternion;
	}
	
	public Quat4f localRotation() {
		Quat4f q = new Quat4f();
		q.set(_localMatrix);
		return q;
	}
	
	public Vector3f eulerAngles() {
//		_rotation.set(this.worldMatrix());
		return localEulerAngles();
	}
	
	
	public Vector3f localEulerAngles() {
		Matrix4 m = this.worldMatrix();
		_eulerAngles.x = (float) Math.atan2( m.m22, m.m23);
		_eulerAngles.y = (float) Math.atan2(-m.m21, Math.sqrt(m.m22 * m.m22 + m.m23 * m.m23));
		_eulerAngles.z = (float) Math.atan2( m.m11, m.m01);
		return _eulerAngles;
	}
	
	
	public void rotateAround(Vector3f v, float degrees) {
		_rMatrix.setIdentity();
		_rMatrix.setRotation(new AxisAngle4f(v.x,v.y,v.z,degrees * 0.2f ));//*  RMX.PI_OVER_180));
//		_rMatrix.transpose();
		
//		_quaternion.set(new AxisAngle4f(v.x,v.y,v.z,degrees * 0.1f));
		Vector3f p = this.localPosition();
		_localMatrix.mul(_rMatrix);
		this.setPosition(p);
		
	}

	public Matrix4 localMatrix() {
		return _localMatrix;
	}
	
//	public void moveForward(String speed) {
//		this.localMatrix.m32 += 1;
//		Bugger.logAndPrint(this.localMatrix.m32, false);
//	}

}
