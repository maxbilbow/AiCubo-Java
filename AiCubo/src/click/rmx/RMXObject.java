package click.rmx;

import java.util.LinkedList;
import java.util.Set;

import click.rmx.messages.EventListener;
import click.rmx.messages.KeyValueObserver;
import click.rmx.messages.NotificationCenter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class RMXObject  implements EventListener, KeyValueObserver {
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
	/**
	 * Determines whether the RMXObject descendant should start listening for events.
	 * Decision is based on whether the derived class has overridden one of the methods used in Event Notification
	 * @return true if any of the listener methods have been overridden
	 * @throws SecurityException
	 */
	private boolean addToGlobalListeners() throws SecurityException { 
		Class<?> classType = this.getClass();
		for (String vMethod : ListenerMethods) {
			Method method;
			try {
				method = classType.getDeclaredMethod(vMethod, String.class, Object.class);
				if (method.getDeclaringClass() != RMXObject.class) {
					_imps.put(method.getName(), true);
					return true;
				} else {
					_imps.put(method.getName(), false);
				}
			} catch (NoSuchMethodException e) {
				Bugger.log(classType.getName() + " Does not override " + vMethod);
			}
		}
		return false;
	}
	
	public void sendMessage(String message, Object args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class<?> classType = this.getClass();
		Method method;
		if (args != null) {
			method = classType.getDeclaredMethod(message, args.getClass());
			method.invoke(this,args);
		} else {
			method = classType.getDeclaredMethod(message);
			method.invoke(this);
		}
	}
	
	public void sendMessage(String message) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class<?> classType = this.getClass();
			Method method = classType.getDeclaredMethod(message);
			method.invoke(this);
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
	public EventListener clone() {
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
		} catch (NoSuchMethodException e) {
//			Bugger.log(e.getClass().getSimpleName() + ": " + message + "()");
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
			} catch (NoSuchMethodException e) {
				//Bugger.log(e.getClass().getSimpleName() + ": " + message + "("+args +")");
			}
		
	}

	
	@Override
	public boolean doesImplementMethod(String name) {
		// TODO Auto-generated method stub
//		Bugger.logAndPrint(name + " implements " + name + ": " +  _imps.getOrDefault(name, false), false);
		return _imps.getOrDefault(name, false);
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