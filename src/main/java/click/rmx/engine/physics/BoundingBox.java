package click.rmx.engine.physics;

import click.rmx.engine.Node;
import click.rmx.engine.math.Vector3;

public class BoundingBox extends CollisionBounds {


//	static final int MIN = 0, MAX = 1;
//	static final int FRONT = 0, BACK = 1, LEFT = 2 , RIGHT = 3, TOP = 4, BOTTOM = 5;

	public BoundingBox(Node node) {
		super(node);
		// TODO Auto-generated constructor stub
	}
	public float xMin() {
		return - this.transform.getWidth() / 2;
	}
	public float yMin() {
		return - this.transform.getHeight() / 2;
	}
	public float zMin() {
		return - this.transform.getLength() / 2;
	}
	public float xMax() {
		return + this.transform.getWidth() / 2;
	}
	public float yMax() {
		return + this.transform.getHeight() / 2;
	}
	public float zMax() {
		return + this.transform.getLength() / 2;
	}

	@Override
	public boolean intersects(CollisionBounds bounds) {
		BoundingBox other = (BoundingBox) bounds;
		Vector3 a = this.transform.position();
		Vector3 b = other.transform.position();

		if (this.xMin() + a.x > other.xMax() + b.x)
			return false;
		if (this.yMin() + a.y > other.yMax() + b.y)
			return false;
		if (this.zMin() + a.z > other.zMax() + b.z)
			return false;
		if (this.xMax() + a.x < other.xMin() + b.x)
			return false;
		if (this.yMax() + a.y < other.yMin() + b.y)
			return false;
		if (this.zMax() + a.z < other.zMin() + b.z)
			return false;

		return true;
	}

	public Vector3 getCollisionNormal(BoundingBox other) {
		Vector3 a = this.transform.position();
		Vector3 b = other.transform.position();
		Vector3 normal = new Vector3();
		
		float xMinA = this.xMin() + a.x;
		float xMaxA = this.xMax() + a.x;
		float yMinA = this.yMin() + a.y;
		float yMaxA = this.yMax() + a.y;
		float zMinA = this.zMin() + a.z;
		float zMaxA = this.zMax() + a.z;
		
		float xMinB = other.xMin() + b.x;
		float xMaxB = other.xMax() + b.x;
		float yMinB = other.yMin() + b.y;
		float yMaxB = other.yMax() + b.y;
		float zMinB = other.zMin() + b.z;
		float zMaxB = other.zMax() + b.z;
		
		//find the x value
//		if (xMinA < xMaxB && xMinA > xMinB)
			

		return normal;
	}




}
