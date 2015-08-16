package rmx.gl;
import static org.lwjgl.glfw.GLFW.*;
//import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import org.lwjgl.glfw.GLFWKeyCallback;




public class KeyCallback extends GLFWKeyCallback {
	public static char forward = 'w', back = 's', left = 'a', right = 'd', up = 'e', down = 'q', stop = 'c', jump = ' ';
//	public static boolean update = true;
    public final KeyStates keyStates = new KeyStates();//, keySpecialStates[] = new boolean[246];
	
    private static KeyCallback singleton = new KeyCallback();
    private KeyCallback() {    }
    boolean mouseLocked = false;
    public static KeyCallback getInstance() {
    	return singleton;
    }
	 @Override
     public void invoke(long window, int key, int scancode, int action, int mods) {
		if (action == GLFW_PRESS) {
			this.keyStates.put(key, true);
			System.out.println("Key Down: " + (char) key + " "+ scancode + " "+ action + " "+ mods);
		} else if (action == GLFW_RELEASE) {
			this.keyStates.put(key, false);
			System.out.println("  Key Up: " + (char) key + " "+ scancode + " "+ action + " "+ mods);
		}
		
		if (action == GLFW_RELEASE)
			switch (key) {
			case GLFW_KEY_ESCAPE:
				glfwSetWindowShouldClose(window, GL_TRUE);
				break;
			case GLFW_KEY_W:
//			 Node.getCurrent().transform.moveForward(1);
				break;
			case GLFW_KEY_M:
				if (mouseLocked) {
					glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
					mouseLocked = false;
				} else {
					glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
					mouseLocked = true;
				}
				break;			 
			}
     }

}
