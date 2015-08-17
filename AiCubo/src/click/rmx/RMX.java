package click.rmx;

import click.rmx.engine.math.Matrix4;

public final class RMX {
	public static final float PI_OVER_180 = (float) (Math.PI / 180);

	/**
	 * Events
	 */
	public static final String
		END_OF_GAMELOOP = "END_OF_GAMELOOP";
	
	public static final Matrix4 Matrix4Identity = new Matrix4();
	
	static {
		Matrix4Identity.setIdentity();
	}

	public static float getCurrentFramerate() {
		return 0.0167f;
	}
}