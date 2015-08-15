package rmx.engine;

public class Camera {
	private float fov, nearZ, farZ;
	
	public Camera() {
		this.setFov(60);
		this.setNearZ(1);
		this.setFarZ(1000);
	}

	public float getFarZ() {
		return farZ;
	}

	public void setFarZ(float farZ) {
		this.farZ = farZ;
	}

	public float getFov() {
		return fov;
	}

	public void setFov(float fov) {
		this.fov = fov;
	}

	public float getNearZ() {
		return nearZ;
	}

	public void setNearZ(float nearZ) {
		this.nearZ = nearZ;
	}
	
	
}
