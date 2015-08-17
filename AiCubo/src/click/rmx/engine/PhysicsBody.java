package click.rmx.engine;


public class PhysicsBody extends NodeComponent {

	float mass = 0;
	public float getMass() {
		return mass;
	}
	
	public float TotalMass() {
		return this.getNode().transform.mass();
	}
	
	
	
	public void setMass(float mass) {
		this.mass = mass;
	}

	
}
