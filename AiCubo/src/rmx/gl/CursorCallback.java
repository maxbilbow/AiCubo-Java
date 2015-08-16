package rmx.gl;



import org.lwjgl.glfw.GLFWCursorPosCallback;

import rmx.engine.Node;

public class CursorCallback extends GLFWCursorPosCallback {
//	void center();
//	void MouseButton(int button, int state, int x, int y);
//	void MouseMotion(int x, int y);
//	void mouseFree(int x, int y);
	
	private static CursorCallback singleton = new CursorCallback();
	    private CursorCallback() {    }
	    public static CursorCallback getInstance() {
	    	return singleton;
	    }
	    

	private double xpos ,ypos;
	private boolean restart = true;
	private boolean cusorLocked = false;
	@Override
	public void invoke(long window, double xpos, double ypos) {
		if (!cusorLocked) 
			return;
		if (restart) {
			this.xpos = xpos;
			this.ypos = ypos;
			restart = false;
			return;
		} else {
			double dx = xpos - this.xpos;
			double dy = ypos - this.ypos;
			dx *= 0.1; dy *= 0.1;
			this.xpos = xpos;
			this.ypos = ypos;
			Node.getCurrent().broadcastMessage("move","yaw:"+dx);
			Node.getCurrent().broadcastMessage("move","pitch:"+dy);
		}
		
		
	}
	public boolean isCursorLocked() {
		return cusorLocked;
	}
	public void lockCursor(boolean lock) {
		this.cusorLocked = lock;
		this.restart = true;
	}
}
