package rmx.gl;


public interface IKeyProcessor {
    
	public abstract void repeatedKeys();
//	public abstract void movement(float speed, int key);
//	public abstract void keyDownOperations (int key);
//	public abstract void keyUpOperations(int key);
//	public abstract void keySpecialDownOperations(int key);
//	public abstract void keySpecialUpOperations(char key);
//	public abstract void keySpecialOperations();
	public abstract void keyDown (int key);//, int action, int scancode, int mods);
	public abstract void keyUp (int key);//, int action, int scancode, int mods);
//	public abstract void RMXkeySpecial (int key, int x, int y);
//	public abstract void RMXkeySpecialUp (int key, int x, int y);
}
