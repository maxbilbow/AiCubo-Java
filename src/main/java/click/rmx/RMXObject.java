package click.rmx;

import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

import click.rmx.messages.IEventListener;
import click.rmx.messages.KeyValueObserver;
import click.rmx.messages.NotificationCenter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import static click.rmx.RMX.rmxDecrimentObjectCount;
import static click.rmx.RMX.rmxIncrementObjectCount;

public class RMXObject  implements IRMXObject {
	private HashMap<String, Object> values = new HashMap<String, Object> ();
	private HashMap<String, LinkedList<KeyValueObserver>> observers = new HashMap<String, LinkedList<KeyValueObserver>>  ();
	//	protected String name = "Unnamed RMXObject";

	private static int _count = 0;
	private int id = _count++;

	/* (non-Javadoc)
	 * @see click.rmx.IRMXObject#uniqueID()
	 */
	@Override
	public int uniqueID() {
		return this.id;
	}
	public static int Count() {
		return _count;
	}
	
	
	
	public RMXObject() {
		this.setName("Unnamed RMXObject");
		try {
			if (this.addToGlobalListeners())
				NotificationCenter.getInstance().addListener(this);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			rmxIncrementObjectCount(this);
		}
	}

	@Override
	protected void finalize() throws Throwable {
		rmxDecrimentObjectCount(this);
		super.finalize();
	}
	

	@Override
	public void startListening(){
		NotificationCenter.getInstance().addListener(this);
	}

	@Override
	public void stopListening() {
		NotificationCenter.getInstance().removeListener(this);
	}

	protected void willBeginEvent(String theEvent){
		NotificationCenter.getInstance().EventWillStart (theEvent);
	}

	protected void didFinishEvent(String theEvent){
		NotificationCenter.getInstance().EventDidEnd (theEvent);
	}

	protected void didCauseEvent(String theEvent){
		NotificationCenter.getInstance().EventDidOccur (theEvent);
	}

	protected void willBeginEvent(String theEvent, Object args){
		NotificationCenter.getInstance().EventWillStart (theEvent, args);
	}


	protected void didFinishEvent(String theEvent, Object args){
		NotificationCenter.getInstance().EventDidEnd (theEvent, args);
	}

	protected void didCauseEvent(String theEvent, Object args){
		NotificationCenter.getInstance().EventDidOccur (theEvent, args);
	}

	protected void willChangeValueForKey(String key){
		if (this.observers.size() > 0)
			for (KeyValueObserver observer : this.observers.get(key)) {
				observer.onValueForKeyWillChange(key, this.values.get(key), this);
			}
	}
	//	
	protected void didChangeValueForKey(String key) {
		if (this.observers.size() > 0)
			for (KeyValueObserver observer : this.observers.get(key)) {
				observer.onValueForKeyDidChange(key, this.values.get(key), this);
			}
	}

	/* (non-Javadoc)
	 * @see click.rmx.IRMXObject#setValue(java.lang.String, java.lang.Object)
	 */
	@Override
	public Object setValue(String forKey, Object value) {
		return this.values.put(forKey, value);
	}

	/* (non-Javadoc)
	 * @see click.rmx.IRMXObject#getValue(java.lang.String)
	 */
	@Override
	public Object getValue(String forKey) {
		return this.values.getOrDefault(forKey, null);//.get(forKey);
	}

	/* (non-Javadoc)
	 * @see click.rmx.IRMXObject#getValueOrSetDefault(java.lang.String, java.lang.Object)
	 */
	@Override
	public Object getValueOrSetDefault(String forKey, Object value) {
		if (this.values.containsKey(forKey))
			return this.values.get(forKey);
		else
			this.values.put(forKey, value);
		return value;
	}

	/* (non-Javadoc)
	 * @see click.rmx.IRMXObject#AddObserver(click.rmx.messages.KeyValueObserver, java.lang.String)
	 */
	@Override
	public void AddObserver(KeyValueObserver observer, String forKey) {
		if (!this.observers.containsKey(forKey))
			this.observers.put(forKey, new LinkedList<KeyValueObserver>());
		if (!this.observers.get(forKey).contains(observer))
			this.observers.get(forKey).add(observer);
	}

	/* (non-Javadoc)
	 * @see click.rmx.IRMXObject#removeObserver(click.rmx.messages.KeyValueObserver, java.lang.String)
	 */
	@Override
	public void removeObserver(KeyValueObserver observer, String forKey) {
		if (this.observers.containsKey(forKey))
			if (this.observers.get(forKey).contains(observer))
				this.observers.get(forKey).remove(observer);
	}

	/* (non-Javadoc)
	 * @see click.rmx.IRMXObject#removeObserver(click.rmx.messages.KeyValueObserver)
	 */
	@Override
	public void removeObserver(KeyValueObserver observer) {
		Set<String> keys = this.observers.keySet();
		if (keys.size() > 0)
			for (String key : keys) {
				this.removeObserver(observer, key);
			}
	}


	private final static String[] listenerMethods; 

	/**
	 * get the name of all EventListener methods. We will check whether these have been overridden later.
	 * If so, the object will be added to listeners on initialization
	 * TODO: Check that this is indeed efficient, or if "startListening()" should just be called instead.
	 */
	static {
		Method[] m = IEventListener.class.getDeclaredMethods();
		listenerMethods = new String[m.length];		
		for (int i=0; i<m.length ; ++i) {
			listenerMethods[i] = IEventListener.class.getMethods()[i].getName();
			//			System.out.println("ADDING: " + listenerMethods[i]);
		}

	}


	private HashMap<String,Boolean> _imps = new HashMap<String,Boolean>();

	static final int OVERRIDES = 1, METHOD_EXISTS = 0;
	//	private boolean check(int check, Method method) {
	//		return check == METHOD_EXISTS || check == OVERRIDES && method.getDeclaringClass() != RMXObject.class;
	//	}

	public static String StringValueOf(Class<?>... args) {
		String s = "(";
		if (args != null && args.length > 0) {
			s += args[0].getSimpleName();
			for (int i = 1; i<args.length; ++i)
				s += "," + args[i].getSimpleName();
		}
		return s +")";
	}
	boolean checkMethodWithName(String methodName, int type, Class<?>... args) {			
		Class<?> classType = this.getClass();
		String key = methodName + StringValueOf(args);
		try {
			switch (type) {
			case OVERRIDES:
				classType.getDeclaredMethod(methodName, args);
//				Bugger.log(this.getClass().getSimpleName() + "::" + key + " OVERRIDES SUPER");
				break;
			default:
				classType.getMethod(methodName, args);
//				Bugger.log(this.getClass().getSimpleName() + "::" + key + " EXISTS IN HIRACHY");
				break;		
			}
			_imps.put(key, true);
			return true;
		} catch (NoSuchMethodException e) {
			//			Bugger.log(this.getClass().getSimpleName() + "::" + key + " NOT Implemented");
			_imps.put(key, false);
		}
		return false;
	}
	/**
	 * Determines whether the RMXObject descendant should start listening for events.
	 * Decision is based on whether the derived class has overridden one of the methods used in Event Notification
	 * @return true if any of the listener methods have been overridden
	 * @throws SecurityException
	 */
	private boolean addToGlobalListeners() throws SecurityException { 

		boolean result = false;
		for (String method : listenerMethods) {
			if (checkMethodWithName(method, OVERRIDES, String.class, Object.class))
				result = true;
		}
		return result;
	}

	@Override
	public void sendMessage(String message, Object args) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if (args != null && this.implementsMethod(message,args.getClass())) {
			try {
				this.getClass().getMethod(message, args.getClass()).invoke(this,args);
			} catch (NoSuchMethodException e) {
				Bugger.logAndPrint(message, true);
				e.printStackTrace();
			}
		} else {
			this.sendMessage(message);
		}
	}

	/* (non-Javadoc)
	 * @see click.rmx.IRMXObject#sendMessage(java.lang.String)
	 */
	@Override
	public void sendMessage(String message) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if (this.implementsMethod(message)) {
			try {
				this.getClass().getMethod(message).invoke(this);
			} catch (NoSuchMethodException e) {
				Bugger.log(e);
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onEventDidStart(String theEvent, Object args){
		String arg = args != null ? args.toString() : "N/A";
		Bugger.log(this.uniqueName() + " => Event Started: " + theEvent + " ("+ NotificationCenter.getInstance().statusOf(theEvent) +"), with args: " + arg);
	}

	@Override
	public void onEventDidEnd(String theEvent, Object args){
		String arg = args != null ? args.toString() : "N/A";
		Bugger.log(this.uniqueName() + " => Event Ended: " + theEvent + " ("+ NotificationCenter.getInstance().statusOf(theEvent) +"), with args: " + arg);
	}



	public static boolean OneIn10() {
		return true;//TODO Random(0,10) == 1;
	}


	/* (non-Javadoc)
	 * @see click.rmx.IRMXObject#uniqueName()
	 */
	@Override
	public String uniqueName() {
		// TODO Auto-generated method stub
		return this.getName() + " (" + this.id + ")";
	}

	private String name = "";
	/* (non-Javadoc)
	 * @see click.rmx.IRMXObject#getName()
	 */
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;//(String) this.getValue("name");
	}



	/* (non-Javadoc)
	 * @see click.rmx.IRMXObject#setName(java.lang.String)
	 */
	@Override
	public void setName(String name) {
		//		this.willChangeValueForKey("name");
		this.name = name;
		//		this.values.put("name", name);
		//		this.didChangeValueForKey("name");
	}

	@Override
	public void onValueForKeyWillChange(String key, Object value, IRMXObject sender) {
		Bugger.log(this.uniqueName() + " >> " + sender.uniqueName() + " will change: " + key + ", from old value: " + value);
	}

	@Override
	public void onValueForKeyDidChange(String key, Object value, IRMXObject sender) {
		Bugger.log(this.uniqueName() + " >> " + sender.uniqueName() + " did change: " + key + ", to new value: " + value);
	}

	@Override
	public IEventListener clone() {
		RMX.rmxTodo();
		return null;
	}




	@Override
	public void broadcastMessage(String message) {
		try {
			this.sendMessage(message);
		} catch (SecurityException | IllegalAccessException 
				| InvocationTargetException e) {
			Bugger.log( e.getClass().getSimpleName() + ": " + message + "()");
			e.printStackTrace();
		} 

	}

	@Override
	public void broadcastMessage(String message, Object args) {
		try {
			this.sendMessage(message, args);
		} catch ( SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			Bugger.log(e.getClass().getSimpleName() + ": " + message + "("+args +")");
			e.printStackTrace();
		} 
	}


	@Override
	public boolean implementsMethod(String method, Class<?>... args) {
		String key = method + StringValueOf(args);
		Boolean result = _imps.get(key);
		if (result == null) {
			result = checkMethodWithName(method, METHOD_EXISTS, args);

			_imps.put(key, result);
		}
		return result;
	}

	@Override
	public String toString() {
		String s = this.uniqueName() + ":";
		try {
			for (Entry<String, Object> e : this.values.entrySet()) {
				s+= "\n" + e.getKey();
				if (e.getValue() == null)
					s += " == NULL";
				else if (e.getValue() instanceof RMXObject)
					s += " == " + e.getClass().getSimpleName() + ": " + ((IRMXObject) e.getValue()).uniqueName();
				else
					s += " == " + e;
			}
		} catch (StackOverflowError error) {
			int i = 0;
			for (Entry<String, Object> e : this.values.entrySet()) {
				System.err.print("\n" + i++ + ": ");
				System.err.print(e.getKey());
				System.err.print(" == ");
				System.err.print(e.getValue());
			}
			System.out.println(s+ "\nValue count: " + this.values.size());// + ", Message: " + e.toString());
//			e.printStackTrace();
		}
		return s;

	}
	
	
}
