package rattleGL;

public abstract class RMXGLKeyProcessor {
	public static char forward = 'w', back = 's', left = 'a', right = 'd', up = 'e', down = 'q', stop = 'c', jump = ' ';
	public static boolean update = true;
    public static boolean keyStates[] = new boolean[256], keySpecialStates[] = new boolean[246];
    
	public static void repeatedKeys(){}
	public void movement(float speed, int key){}
	public void keyDownOperations (int key){}
	public void keyUpOperations(int key){}
	public void keySpecialDownOperations(int key){}
	public void keySpecialUpOperations(char key){}
	public void keySpecialOperations(){}
	public void RMXkeyPressed (char key, int x, int y){}
	public void RMXkeyUp (char key, int x, int y){}
	public void RMXkeySpecial (int key, int x, int y){}
	public void RMXkeySpecialUp (int key, int x, int y){}
}
