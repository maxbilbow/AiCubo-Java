package click.rmx.engine;

import java.util.LinkedList;

public class CollisionBody extends NodeComponent {

	private boolean cheked;

	public Object getBoundingBox() {
		return null;
	}
	
	public Object getBoundingSphere() {
		return null;
	}

	public boolean wasChecked() {
		// TODO Auto-generated method stub
		return this.cheked;
	}
	
	public void setChecked(boolean checked) {
		this.cheked = checked;
	}
}
