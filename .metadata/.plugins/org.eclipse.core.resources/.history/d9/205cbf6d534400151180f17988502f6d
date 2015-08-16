
import rmx.Bugger;
import rmx.engine.Behaviour;
import rmx.engine.GameController;
import rmx.engine.NodeComponent;
class ABehaviour extends NodeComponent implements Behaviour {

	
	@Override
	public void lateUpdate() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void update() {
		Bugger.logAndPrint(this.getNode().transform.position(), false);
	}
	
}



public final class Main {	
	
	public static void main(String[] args) {	
		GameController.getInstance().Start(); 
	}
}
