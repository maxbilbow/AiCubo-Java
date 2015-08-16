package rmx.gl;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

import rmx.engine.Node;
import rmx.engine.RenderDelegate;

public interface GLView {
	public void initGL();
	public void enterGameLoop();
	public long window();
	public GLFWErrorCallback errorCallback();
    public GLFWKeyCallback   keyCallback();
    public void setDelegate(RenderDelegate delegate);
    public int height();
    public int width();
    
    public boolean setPointOfView(Node pov);
    public Node pointOfView();
}
