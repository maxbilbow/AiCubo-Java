package click.rmx.engine.math;


import java.nio.FloatBuffer;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix4f;

import javax.vecmath.Vector4f;

import org.lwjgl.BufferUtils;

public class Matrix4 extends Matrix4f{
	static int SIZE = 16;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private float[] m = new float[SIZE];

	/**
	 * 
	 * @return float[] representation of matrix. 
	 */
	public float[] elements() {
		m[0]  = m00; m[1]  = m01; m[2]  = m02; m[3]  = m03;
		m[4]  = m10; m[5]  = m11; m[6]  = m12; m[7]  = m13;
		m[8]  = m20; m[9]  = m21; m[10] = m22; m[11] = m23;
		m[12] = m30; m[13] = m31; m[14] = m32; m[15] = m33;
		return m;
	}
	
//	public ByteBuffer byteBuffer() {
//		ByteBuffer buffer = ByteBuffer.allocateDirect(16).order(ByteOrder.nativeOrder());
//		byte[] bytes = new byte[SIZE];
//		float[] m = elements();
//		for (int i = 0; i<SIZE; ++i)
//			bytes[i] = (byte) m[i];
//		buffer.put(bytes);
//		return buffer;
//	}
	
	private FloatBuffer _buffer = BufferUtils.createFloatBuffer(SIZE);
	private boolean _bufferReady = false;

	
	public void resetBuffers() {
		_bufferReady = false;
	}
	
	/**
	 * 
	 * @return saved floatbuffer or new float buffer is resetBuffers() was called;
	 */
	public FloatBuffer buffer() {
		if (_bufferReady)
			return _buffer;
		else 
			return updateBuffer();
	}
	
	private FloatBuffer updateBuffer() {
		_buffer.clear();
		
		_buffer.put(m00);
		_buffer.put(m01);
		_buffer.put(m02);
		_buffer.put(m03);

		_buffer.put(m10);
		_buffer.put(m11);
		_buffer.put(m12);
		_buffer.put(m13);

		_buffer.put(m20);
		_buffer.put(m21);
		_buffer.put(m22);
		_buffer.put(m23);
	
		_buffer.put(m30);
		_buffer.put(m31);
		_buffer.put(m32);
		_buffer.put(m33);
		
//		_buffer.put(m00);
//		_buffer.put(m10);
//		_buffer.put(m20);
//		_buffer.put(m30);
//
//		_buffer.put(m01);
//		_buffer.put(m11);
//		_buffer.put(m21);
//		_buffer.put(m31);
//		
//		_buffer.put(m02);
//		_buffer.put(m12);
//		_buffer.put(m22);
//		_buffer.put(m32);
//		
//		_buffer.put(m03);
//		_buffer.put(m13);
//		_buffer.put(m23);
//		_buffer.put(m33);
		
		_buffer.flip();
		 
		_bufferReady = true;
		return _buffer;
	}

	static boolean rows = true;
	public void translate(float x, float y, float z) {
		if (rows) {
			m30 += x;
			m31 += y;
			m32 += z;
		} else {
			m03 += x;
			m13 += y;
			m23 += z;
		}
	}
	public boolean translate(String direction, float scale) {
		Vector3 v;
		switch (direction) {
		case "forward":
			scale *= -1;
			v = this.forward();
			break;
		case "up":
//			scale *= -1;
			v = this.up();
			break;
		case "left":
			scale *= -1;
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
	
	private void rotate(float radians, float x, float y, float z) {
		Matrix4 rMatrix = new Matrix4();
		rMatrix.setIdentity();
		rMatrix.setRotation(new AxisAngle4f(x,y,z,radians * 0.2f ));//*  RMX.PI_OVER_180));
//		_rMatrix.transpose();
		
//		_quaternion.set(new AxisAngle4f(v.x,v.y,v.z,degrees * 0.1f));
		Vector3 p = this.position();
		this.mul(rMatrix);
		this.setPosition(p);
		
	}
	
	public void setPosition(Vector3 v) {
		m30 = v.x;
		m31 = v.y;
		m32 = v.z;
	}
	
	public void setPosition(int x, int y, int z) {
		m30 = x;
		m31 = y;
		m32 = z;
	}
	
	public void negatePosition() {
		m30 *= -1;
		m31 *= -1;
		m32 *= -1;
	}
	public void translate(Vector4f v) {
		m30 += v.x;
		m31 += v.y;
		m32 += v.z;
		m33 += v.w; //TODO check this is correct
	}
	private Vector3 _left = new Vector3();
	private Vector3 _up = new Vector3();
	private Vector3 _fwd = new Vector3();
	private Vector3 _pos = new Vector3();
	
	public Vector3 left() {
		if (rows){
		_left.x = this.m00;
		_left.y = this.m01;
		_left.z = this.m02;
		} else {
		_left.x = this.m00;
		_left.y = this.m10;
		_left.z = this.m20;
		}
		return _left;
	}
	
	public Vector3 up() {
		if (rows) {
		_up.x = this.m10;
		_up.y = this.m11;
		_up.z = this.m12;
		} else {
		_up.x = this.m01;
		_up.y = this.m11;
		_up.z = this.m21;
		}
		return _up;
	}
	
	public Vector3 forward() {
		if (rows) {
		_fwd.x = this.m20;
		_fwd.y = this.m21;
		_fwd.z = this.m22;
		} else {
		_fwd.x = this.m02;
		_fwd.y = this.m12;
		_fwd.z = this.m22;
		}
		return _fwd;
	}
	
	public Vector3 position() {
		if (rows) {
		_pos.x = this.m30;
		_pos.y = this.m31;
		_pos.z = this.m32;
		} else {
		_pos.x = this.m03;
		_pos.y = this.m13;
		_pos.z = this.m23;
		}
		return _pos;
	}
	
	private EulerAngles _eulerAngles = new EulerAngles();
	public EulerAngles eulerAngles() {
		_eulerAngles.y = (float) Math.atan2(-m20,m00);
		_eulerAngles.x = (float) Math.asin(m10);
		_eulerAngles.z = (float) Math.atan2(-m12,m11);
				
				
//		_eulerAngles.x = (float) Math.atan2( m22, m23);
//		_eulerAngles.y = (float) Math.atan2(-m21, Math.sqrt(m22 * m22 + m23 * m23));
//		_eulerAngles.z = (float) Math.atan2( m11, m01);
		return _eulerAngles;
	}

	
}