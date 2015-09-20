package click.rmx.engine.math;

import java.nio.FloatBuffer;

import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;

public class Vector3 extends Vector3f {
	/**
	 * 
	 */
	private static final long serialVersionUID = -830815158587681541L;

	public Vector3() {
		super();
	}
	public Vector3(float x, float y, float z) {
		super(x,y,z);
	}
	
	public Vector3(int x, int y, int z) {
		super(x,y,z);
	}

	static final int SIZE = 3;
	
	public static final Vector3 Zero = new Vector3();
	public static final Vector3 X = new Vector3(1,0,0);
	public static final Vector3 Y = new Vector3(0,1,0);
	public static final Vector3 Z = new Vector3(0,0,1);
	
	
	@Override
	public Vector3 clone() {
		return (Vector3)super.clone();	
	}
	
	public boolean isZero() {
		return x == 0 && y == 0 && z == 0;
	}
	
	public float getLength() {
		float lenA = 
				x * x +
				y * y +
				z * z;
		return (float) Math.sqrt(lenA);
	}
	
	public Vector3 getVectorTo(Vector3 b) {
		float dx = b.x - x;
		float dy = b.y - y;
		float dz = b.z - z;
		return new Vector3(dx,dy,dz);
	}
	
	public float getDistanceTo(Vector3 b) {
		float dx = b.x - x;
		float dy = b.y - y;
		float dz = b.z - z;			
		return (float) Math.sqrt(
				dx * dx +
				dy * dy +
				dz * dz
				);
	}
	  /**
     * Returns the Buffer representation of this vector.
     *
     * @return Vector as FloatBuffer
     */
    public FloatBuffer getBuffer() {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(4);
        buffer.put(x).put(y).put(z).put(1);
        buffer.flip();
        return buffer;
    }
    
    public static Vector3 makeSubtraction(Vector3 left, Vector3 right) {
    	Vector3 result = left.clone();
    	result.sub(right);
    	return result;
    }
    
    public static Vector3 makeAddition(Vector3 left, Vector3 right) {
    	Vector3 result = left.clone();
    	result.add(right);
    	return result;
    }
	public Vector3 getNormalized() {
		Vector3 n = this.clone();
		n.normalize();
		return n;
	}
}
