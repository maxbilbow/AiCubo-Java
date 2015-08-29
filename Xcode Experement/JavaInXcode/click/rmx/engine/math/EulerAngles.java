package click.rmx.engine.math;

public class EulerAngles extends Vector3 {
	
//	public EulerAngles(float x, float y, float z) {
//		super(x, y, z);
//		// TODO Auto-generated constructor stub
//	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -399948191577891096L;

	public float pitch() {
		return this.x;
	}
	
	public float yaw() {
		return this.y;
	}
	
	public float roll() {
		return this.z;
	}
}
