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
