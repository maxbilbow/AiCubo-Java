package click.rmx;

import java.util.LinkedList;
import java.util.Set;

import click.rmx.messages.IEventListener;
import click.rmx.messages.KeyValueObserver;
import click.rmx.messages.NotificationCenter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class RMXObject  implements IEventListener, KeyValueObserver {
	private HashMap<String, Object> values = new HashMap<String, Object> ();
	private HashMap<String, LinkedList<KeyValueObserver>> observers = new HashMap<String, LinkedList<KeyValueObserver>>  ();
//	protected String name = "Unnamed RMXObject";
	
	private static int _count = 0;
	private int id = _count++;
	
	public RMXObject() {
		this.setName("Unnamed RMXObject");
		try {
			if (this.addToGlobalListeners())
				NotificationCenter.getInstance().addListener(this);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.onAwake();
	}
	
	protected void onAwake(){}
	
	public void startListening(){
    	NotificationCenter.getInstance().addListener(this);
    }
	
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
//
//	public virtual void setValue(string forKey, object value) {
//		if (values[forKey] != value) {
////			DidChangeValueForKey(forKey); ??
//			values[forKey] = value;
//			DidChangeValueForKey(forKey);
//		}
//	}

	public Object getValue(String forKey) {
		return this.values.get(forKey);
	}

	public void AddObserver(KeyValueObserver observer, String forKey) {
		if (!this.observers.containsKey(forKey))
			this.observers.put(forKey, new LinkedList<KeyValueObserver>());
		if (!this.observers.get(forKey).contains(observer))
			this.observers.get(forKey).add(observer);
	}

	public void removeObserver(KeyValueObserver observer, String forKey) {
		if (this.observers.containsKey(forKey))
			if (this.observers.get(forKey).contains(observer))
				this.observers.get(forKey).remove(observer);
	}
	
	public void removeObserver(KeyValueObserver observer) {
		Set<String> keys = this.observers.keySet();
		if (keys.size() > 0)
			for (String key : keys) {
				this.removeObserver(observer, key);
		}
	}

	protected static String[] ListenerMethods = {
		"onEventDidStart",
		"onEventDidEnd"
	};

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
				Bugger.log(this.getClass().getSimpleName() + "::" + key + " OVERRIDES SUPER");
				break;
			default:
				classType.getMethod(methodName, args);
				Bugger.log(this.getClass().getSimpleName() + "::" + key + " EXISTS IN HIRACHY");
				break;		
			}
			_imps.put(key, true);
			return true;
		} catch (NoSuchMethodException e) {
			Bugger.log(this.getClass().getSimpleName() + "::" + key + " NOT Implemented");
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
		for (String method : ListenerMethods) {
			if (checkMethodWithName(method, OVERRIDES, String.class, Object.class))
				result = true;
		}
		return result;
	}
	
	public void sendMessage(String message, Object args) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if (args != null && this.implementsMethod(message,args.getClass())) {
			try {
				this.getClass().getDeclaredMethod(message, args.getClass()).invoke(this,args);
			} catch (NoSuchMethodException e) {
				Bugger.log(e);
				e.printStackTrace();
			}
		} else {
			this.sendMessage(message);
		}
	}
	
	public void sendMessage(String message) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if (this.implementsMethod(message)) {
			try {
				this.getClass().getDeclaredMethod(message).invoke(this);
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
	

	public String uniqueName() {
		// TODO Auto-generated method stub
		return this.getName() + " (" + this.id + ")";
	}
	public String getName() {
		// TODO Auto-generated method stub
		return (String) this.getValue("name");
	}

	public void setName(String name) {
		this.willChangeValueForKey("name");
//		this.name = name;
		this.values.put("name", name);
		this.didChangeValueForKey("name");
	}
	
	@Override
	public void onValueForKeyWillChange(String key, Object value, RMXObject sender) {
        Bugger.log(this.uniqueName() + " >> " + sender.uniqueName() + " will change: " + key + ", from old value: " + value);
	}

	@Override
	public void onValueForKeyDidChange(String key, Object value, RMXObject sender) {
		Bugger.log(this.uniqueName() + " >> " + sender.uniqueName() + " did change: " + key + ", to new value: " + value);
	}

	@Override
	public IEventListener clone() {
		// TODO Auto-generated method stub
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
}
class Derived extends RMXObject {
	@Override
	public void onEventDidEnd(String theEvent, Object args){
		String arg = args != null ? args.toString() : "N/A";
        Bugger.log(this.getClass().getName() + " => Event Ended: " + theEvent + " ("+ NotificationCenter.getInstance().statusOf(theEvent) +"), with args: " + arg);
    }
	
	public void printName() {
		Bugger.log("My Name is " + getName());
	}
	public void printName(String arg) {
		Bugger.log("My Name is NOT " + arg);
	}
	
	public static void TEST() {
		RMXObject o = new RMXObject();
		o.setName("Parent");
		RMXObject o2 = new Derived();
		o2.setName("Child");
		
		o.broadcastMessage("printName");
		o2.broadcastMessage("printName");
		o.broadcastMessage("printName","Balls");
		o2.broadcastMessage("printName","Balls");
		o.didCauseEvent("Hello!");
		
	}
}
