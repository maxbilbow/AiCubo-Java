package click.rmx.engine;



import javax.vecmath.AxisAngle4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import click.rmx.Bugger;
import click.rmx.engine.math.EulerAngles;
import click.rmx.engine.math.Matrix4;
import click.rmx.engine.math.Vector3;
import click.rmx.engine.physics.CollisionBody;
import click.rmx.engine.physics.PhysicsBody;



public class Transform extends ANodeComponent {
	
	public final Node node;
	
//	private Matrix4 _rMatrix = new Matrix4();
	private final Matrix4 _worldMatrix;
	private final Matrix4 _axis;
	private final Matrix4 _localMatrix;
	private final Quat4f  _quaternion = new Quat4f();
	private final Vector3 _left = new Vector3();
	private final Vector3 _up = new Vector3();
	private final Vector3 _fwd = new Vector3();
	private final Vector3 _localPosition = new Vector3();
//	private final Vector3 _lastPosition = new Vector3();
	
	
	public Vector3[] getHistory() {
		return this.history;
	}
	public Vector3 lastPosition() {
		return this.history[0];
	}
	private int stepsBack = 0;
	public synchronized boolean stepBack(String args) {
		if (stepsBack >= history.length)
			return false;
		if (args.contains("x"))
			this.localMatrix().m30 = history[stepsBack].x;
		if (args.contains("y"))
			this.localMatrix().m31 = history[stepsBack].y;
		if (args.contains("z"))
			this.localMatrix().m32 = history[stepsBack].z;
//		stepsBack++;
		return !history[stepsBack++].isZero();
//		this.setPosition(lastPosition());
	}
	
	
	public synchronized void moveAlongAxis(String args, float n) {
		if (args.contains("x"))
			this.localMatrix().m30 += n;
		if (args.contains("y"))
			this.localMatrix().m31 += n;
		if (args.contains("z"))
			this.localMatrix().m32 += n;
	}
//	private EulerAngles _eulerAngles = new EulerAngles();
	private Vector3 _scale = new Vector3(1f,1f,1f);
	public Transform(Node node) {
		this.node = node;
		node.setComponent(Transform.class, this);
		this._localMatrix = new Matrix4();
		this._localMatrix.setIdentity();
		this._worldMatrix = new Matrix4();
		this._worldMatrix.set(_localMatrix);
		
		this._axis = new Matrix4();
		this._axis.setIdentity();
		
		for (int i = 0; i< history.length; ++i)
			history[i] = new Vector3();
	}
	
	public Vector3 left() {
		_left.x = _localMatrix.m00;
		_left.y = _localMatrix.m01;
		_left.z = _localMatrix.m02;
		return _left;
	}
	
	public Vector3 up() {
		_up.x = _localMatrix.m10;
		_up.y = _localMatrix.m11;
		_up.z = _localMatrix.m12;
		return _up;
	}
	
	public Vector3 forward() {
		_fwd.x = _localMatrix.m20;
		_fwd.y = _localMatrix.m21;
		_fwd.z = _localMatrix.m22;
		
		return _fwd;
	}
	
	public Vector3 scale() {
		return _scale;
	}
	
	public synchronized void setScale(float x, float y, float z) {
		_scale.x = x;
		_scale.y = y;
		_scale.z = z;
		
	}
	
	
	private float totalMass; private long _tmTimestamp = -1;
	/**
	 * TODO: Test with children
	 * @return
	 */
	public float mass() {
		if (Scene.getCurrent().tick() == _tmTimestamp)
			return this.totalMass;
		totalMass = node.physicsBody() != null ? node.physicsBody().getMass() : 0;
		for (Node child : node.getChildren()){
			totalMass += child.transform().mass();
		}
		return totalMass;
	}
	
	
	/**
	 * TODO probably doesnt work. How do you do this maths?
	 * @return
	 */
//	private long _wmTimestamp = -1;//stops this algorithm repeating everytime called.
	public Matrix4 worldMatrix() {
//		if (node.tick() == _wmTimestamp)
//			return _worldMatrix;
		Transform parent = this.parent();
		if (parent != null && parent.parent() != null) {
			_worldMatrix.set(this._localMatrix);//.clone();
			_worldMatrix.mul(this.parent().worldMatrix());
			return _worldMatrix;
		} else {
			this._worldMatrix.set(_localMatrix);
			return this._localMatrix;
		}
//		_wmTimestamp = this.node.tick();
//		return _worldMatrix;
	}
	
	public Transform parent() {
		Node parentNode = this.node.getParent();
		return parentNode != null ? this.node.getParent().transform() : null;
	}
	
	public Vector3 localPosition() {
		_localPosition.x = _localMatrix.m30;
		_localPosition.y = _localMatrix.m31;
		_localPosition.z = _localMatrix.m32;
		return _localPosition;
	}
	

	public Vector3 position() {
		return this.worldMatrix().position();
	}
	

	public Transform rootTransform() {
		Transform parent = this.parent();
		if (parent != null && parent.parent() != null) {
			return parent.rootTransform();
		}
		return this;
	}
	
	public synchronized void translate(Vector3 v) {
		this._localMatrix.m30 += v.x;
		this._localMatrix.m31 += v.y;
		this._localMatrix.m32 += v.z;
	}
	
	public void setPosition(Vector3f position) {
		this._localMatrix.m30 = position.x;
		this._localMatrix.m31 = position.y;
		this._localMatrix.m32 = position.z;
		if (Scene.getCurrent().tick() <= 0)
			this.updateLastPosition();
	}
	
	public void setPosition(double d, double e, double f) {
		this._localMatrix.m30 = (float) d;
		this._localMatrix.m31 = (float) e;
		this._localMatrix.m32 = (float) f;
	}
	
	public PhysicsBody physicsBody() {
		return this.node.physicsBody();
	}
	
	public CollisionBody collisionBody() {
		return this.node.collisionBody();
	}
	
	public void move(String args) {
		String[] options = args.split(":");
		String direction = options[0];
		float scale = (float) (Float.parseFloat(options[1]) * 0.1);
		if (this.translate(direction, scale) 
				|| this.rotate(direction, scale)) 
			return;
		else
			Bugger.logAndPrint("Warning: \"" + args + "\" was not recognised", true);
		
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
	
	public Vector3 eulerAngles() {
//		_rotation.set(this.worldMatrix());
		return this.worldMatrix().eulerAngles();
	}
	
	
	public EulerAngles localEulerAngles() {
		return this.localMatrix().eulerAngles();
	}
	
	
	private Matrix4 _rMatrix = new Matrix4();
	public void rotate(float radians, float x, float y, float z) {
//		Matrix4 rMatrix = new Matrix4();
		_rMatrix.setIdentity();
		_rMatrix.setRotation(new AxisAngle4f(x,y,z,radians));//*  RMX.PI_OVER_180));
//		_rMatrix.transpose();
		
//		_quaternion.set(new AxisAngle4f(v.x,v.y,v.z,degrees * 0.1f));
		Vector3 p = this.localPosition();
		this._localMatrix.mul(_rMatrix);
		this.setPosition(p);
	}
	

	public Matrix4 localMatrix() {
		return _localMatrix;
	}
	
	
	public boolean translate(String direction, float scale) {
		Vector3 v;
		switch (direction) {
		case "forward":
//			scale *= -1;
			v = this.forward();
			break;
		case "up":
//			scale *= -1;
			v = this.up();
			break;
		case "left":
//			scale *= -1;
			v = this.left();
			break;
		case "x":
//			scale *= -1;
			v = new Vector3(1,0,0);
			break;
		case "y":
//			scale *= -1;
			v = new Vector3(0,1,0);
			break;
		case "z":
//			scale *= -1;
			v = new Vector3(0,0,1);
			break;
		default:
			return false;
		}
		this.translate(
				v.x * scale, 
				v.y * scale,
				v.z * scale
				);
		return true;
	}
	
	
	public synchronized void translate(float x, float y, float z) {
		this._localMatrix.m30 += x;
		this._localMatrix.m31 += y;
		this._localMatrix.m32 += z;
	}
	
	public synchronized void translate(float scale, Vector3 v) {
		this._localMatrix.m30 += v.x * scale;
		this._localMatrix.m31 += v.y * scale;
		this._localMatrix.m32 += v.z * scale;
	}

	public boolean rotate(String direction, float scale) {
		Vector3 v;
		switch (direction) {
		case "pitch":
//			scale *= -1;
			v = this.left();
			break;
		case "yaw":
//			scale *= -1;
			v = this.up();
			break;
		case "roll":
//			scale *= -1;
			v = this.forward();
			break;
		case "x":
//			scale *= -1;
			v = Vector3.X;
			break;
		case "y":
//			scale *= -1;
			v = Vector3.Y;
			break;
		case "z":
//			scale *= -1;
			v = Vector3.Z;
			break;
		default:
			return false;
		}
		this.rotate(scale,v.x,v.y,v.z);
//				v.x * scale, 
//				v.y * scale,
//				v.z * scale
//				);
		return true;
	}
	
	
	@Override
	public void setNode(Node node) {
		if (this.node != node)
			throw new IllegalArgumentException("Transform can only be assigned once");
		super.setNode(this.node);
	}

	public float radius() {
		return (_scale.x + _scale.y + _scale.z) / 3;
	}

	public float getWidth() {
		// TODO Auto-generated method stub
		return scale().x * 2;
	}

	public float getHeight() {
		// TODO Auto-generated method stub
		return scale().y * 2;
	}

	public float getLength() {
		// TODO Auto-generated method stub
		return scale().z * 2;
	}

	Vector3[] history = new Vector3[3];
//	private int historyCheck = 0;
	public synchronized void updateLastPosition() {
		for (int i=history.length-1; i>0; --i) {
			history[i].set(history[i-1]);
		}
		history[0].set(this.localPosition());
		stepsBack = 0;
	}

}
