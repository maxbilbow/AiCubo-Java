package click.rmx.engine.math;


import java.nio.FloatBuffer;

import javax.vecmath.Matrix4f;

import javax.vecmath.Vector4f;

import org.lwjgl.BufferUtils;

public class Matrix4 extends Matrix4f{
	private static int SIZE = 16;
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
	
	private FloatBuffer buffer = BufferUtils.createFloatBuffer(SIZE);
	private boolean _bufferReady = false;

	
	public void resetBuffers() {
		_bufferReady = false;
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

	 /**
     * Returns the Buffer representation of this vector.
     *
     * @return Vector as FloatBuffer
     */
	public FloatBuffer rowBuffer() {
		if (_bufferReady)
			return buffer;
		else {
			buffer.clear();
			buffer.put(m00).put(m01).put(m02).put(m03);
			buffer.put(m10).put(m11).put(m12).put(m13);
			buffer.put(m20).put(m21).put(m22).put(m23);
			buffer.put(m30).put(m31).put(m32).put(m33);
			buffer.flip();
			return buffer;
		}
	}
	
	 /**
     * Returns the Buffer representation of this vector.
     *
     * @return Vector as FloatBuffer
     */
	public FloatBuffer colBuffer() {
		if (_bufferReady)
			return buffer;
		else {
			buffer.clear();
			buffer.put(m00).put(m10).put(m20).put(m30);
	        buffer.put(m01).put(m11).put(m21).put(m31);
	        buffer.put(m02).put(m12).put(m22).put(m32);
	        buffer.put(m03).put(m13).put(m23).put(m33);
			buffer.flip();
			return buffer;
		}
	}

    /**
     * Creates a orthographic projection matrix. Similar to
     * <code>glOrtho(left, right, bottom, top, near, far)</code>.
     *
     * @param left Coordinate for the left vertical clipping pane
     * @param right Coordinate for the right vertical clipping pane
     * @param bottom Coordinate for the bottom horizontal clipping pane
     * @param top Coordinate for the bottom horizontal clipping pane
     * @param near Coordinate for the near depth clipping pane
     * @param far Coordinate for the far depth clipping pane
     * @return Orthographic matrix
     */
    public static Matrix4 orthographic(float left, float right, float bottom, float top, float near, float far) {
        Matrix4 ortho = new Matrix4();

        float tx = -(right + left) / (right - left);
        float ty = -(top + bottom) / (top - bottom);
        float tz = -(far + near) / (far - near);

        ortho.m00 = 2f / (right - left);
        ortho.m11 = 2f / (top - bottom);
        ortho.m22 = -2f / (far - near);
        ortho.m03 = tx;
        ortho.m13 = ty;
        ortho.m23 = tz;

        return ortho;
    }

    /**
     * Creates a perspective projection matrix. Similar to
     * <code>glFrustum(left, right, bottom, top, near, far)</code>.
     *
     * @param left Coordinate for the left vertical clipping pane
     * @param right Coordinate for the right vertical clipping pane
     * @param bottom Coordinate for the bottom horizontal clipping pane
     * @param top Coordinate for the bottom horizontal clipping pane
     * @param near Coordinate for the near depth clipping pane, must be positive
     * @param far Coordinate for the far depth clipping pane, must be positive
     * @return Perspective matrix
     */
    public static Matrix4f frustum(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f frustum = new Matrix4f();

        float a = (right + left) / (right - left);
        float b = (top + bottom) / (top - bottom);
        float c = -(far + near) / (far - near);
        float d = -(2f * far * near) / (far - near);

        frustum.m00 = (2f * near) / (right - left);
        frustum.m11 = (2f * near) / (top - bottom);
        frustum.m02 = a;
        frustum.m12 = b;
        frustum.m22 = c;
        frustum.m32 = -1f;
        frustum.m23 = d;
        frustum.m33 = 0f;

        return frustum;
    }

    /**
     * Creates a perspective projection matrix. Similar to
     * <code>gluPerspective(fovy, aspec, zNear, zFar)</code>.
     *
     * @param fovy Field of view angle in degrees
     * @param aspect The aspect ratio is the ratio of width to height
     * @param near Distance from the viewer to the near clipping plane, must be
     * positive
     * @param far Distance from the viewer to the far clipping plane, must be
     * positive
     * @return Perspective matrix
     */
    public static Matrix4 perspective(float fovy, float aspect, float near, float far) {
        Matrix4 perspective = new Matrix4();

        float f = (float) (1f / Math.tan(Math.toRadians(fovy) / 2f));

        perspective.m00 = f / aspect;
        perspective.m11 = f;
        perspective.m22 = (far + near) / (near - far);
        perspective.m32 = -1f;
        perspective.m23 = (2f * far * near) / (near - far);
        perspective.m33 = 0f;

        return perspective;
    }

    /**
     * Creates a translation matrix. Similar to
     * <code>glTranslate(x, y, z)</code>.
     *
     * @param x x coordinate of translation vector
     * @param y y coordinate of translation vector
     * @param z z coordinate of translation vector
     * @return Translation matrix
     */
    public static Matrix4f translate(float x, float y, float z) {
        Matrix4f translation = new Matrix4f();

        translation.m03 = x;
        translation.m13 = y;
        translation.m23 = z;

        return translation;
    }

	/**
     * Creates a rotation matrix. Similar to
     * <code>glRotate(angle, x, y, z)</code>.
     *
     * @param angle Angle of rotation in degrees
     * @param x x coordinate of the rotation vector
     * @param y y coordinate of the rotation vector
     * @param z z coordinate of the rotation vector
     * @return Rotation matrix
     */
    public static Matrix4 rotate(float angle, float x, float y, float z) {
        Matrix4 rotation = new Matrix4();

        float c = (float) Math.cos(Math.toRadians(angle));
        float s = (float) Math.sin(Math.toRadians(angle));
        Vector3 vec = new Vector3(x, y, z);
        if (vec.length() != 1f) {
            vec.normalize();
            x = vec.x;
            y = vec.y;
            z = vec.z;
        }

        rotation.m00 = x * x * (1f - c) + c;
        rotation.m10 = y * x * (1f - c) + z * s;
        rotation.m20 = x * z * (1f - c) - y * s;
        rotation.m01 = x * y * (1f - c) - z * s;
        rotation.m11 = y * y * (1f - c) + c;
        rotation.m21 = y * z * (1f - c) + x * s;
        rotation.m02 = x * z * (1f - c) + y * s;
        rotation.m12 = y * z * (1f - c) - x * s;
        rotation.m22 = z * z * (1f - c) + c;

        return rotation;
    }



    Vector3 _position = new Vector3();
	public Vector3 position() {
		_position.x = m30;
		_position.y = m31;
		_position.z = m32;
		return _position;
	}
	
	
	@Override
	public Matrix4 clone() {
		Matrix4 clone = new Matrix4();
		clone.set(this);
		return clone;
	}
}
