package click.rmx.engine.gl;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

import click.rmx.engine.Node;
import click.rmx.engine.RenderDelegate;

public interface GLView {
	public void initGL();
	public void enterGameLoop();
	public long window();
	public GLFWErrorCallback errorCallback();
    public GLFWKeyCallback   keyCallback();
    public void setDelegate(RenderDelegate delegate);
    public int height();
    public int width();
    public void setSize(int width, int height);
    public boolean setPointOfView(Node pov);
    public Node pointOfView();
}
