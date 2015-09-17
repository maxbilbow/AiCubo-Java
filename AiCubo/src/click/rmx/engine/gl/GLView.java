package click.rmx.engine.gl;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;

import click.rmx.engine.INode;
import click.rmx.engine.Node;
import click.rmx.engine.RenderDelegate;

public interface GLView {
	public void initGL();
	public void enterGameLoop();
	public long window();
	public GLFWErrorCallback errorCallback();
    public KeyCallback keyCallback();
    public void setDelegate(RenderDelegate delegate);
    public int height();
    public int width();
    public void setSize(int width, int height);
    public boolean setPointOfView(INode cam);
    public INode pointOfView();
}
