package rmx;

import java.util.LinkedList;
import java.util.Set;
import java.util.HashMap;

public class RMXObject extends EventListener implements KeyValueObserver {
	private HashMap<String, Object> values = new HashMap<String, Object> ();
	private HashMap<String, LinkedList<KeyValueObserver>> observers = new HashMap<String, LinkedList<KeyValueObserver>>  ();
//	protected String name = "Unnamed RMXObject";
	
	private static int _count = 0;
	private int id = _count++;
	/// <summary>
	/// Gets a value indicating whether this <see cref="RMX.ASingleton`1"/> add to global listeners.
	/// </summary>
	/// <value><c>true</c> if add to global listeners; otherwise, <c>false</c>.</value>
	private boolean addToGlobalListeners() { 
//			System.Type classType = this.GetType();
//			foreach (string vMethod in ListenerMethods) {
//				MethodInfo method = classType.GetMethod (vMethod);
//				if (method.DeclaringType != typeof(RMXObject)) 
//					return true;
//			}
			return true;
	}

	public RMXObject() {
		this.setName("Unnamed RMXObject");
		this.awake();
	}
	
	protected void awake() {
		if (this.addToGlobalListeners())
			NotificationCenter.addListener(this);
	}

	protected void willBeginEvent(String theEvent){
		NotificationCenter.EventWillStart (theEvent);
	}

	protected void didFinishEvent(String theEvent){
		NotificationCenter.EventDidEnd (theEvent);
	}

	protected void didCauseEvent(String theEvent){
		NotificationCenter.EventDidOccur (theEvent);
	}

	protected void willBeginEvent(String theEvent, Object args){
		NotificationCenter.EventWillStart (theEvent, args);
	}
	
	
	protected void DidFinishEvent(String theEvent, Object args){
		NotificationCenter.EventDidEnd (theEvent, args);
	}

	protected void DidCauseEvent(String theEvent, Object args){
		NotificationCenter.EventDidOccur (theEvent, args);
	}

	protected void WillChangeValueForKey(String key){
		if (this.observers.size() > 0)
			for (KeyValueObserver observer : this.observers.get(key)) {
				observer.OnValueForKeyWillChange(key, this.values.get(key), this);
			}
	}
//	
	protected void DidChangeValueForKey(String key) {
		if (this.observers.size() > 0)
			for (KeyValueObserver observer : this.observers.get(key)) {
				observer.OnValueForKeyDidChange(key, this.values.get(key), this);
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

	public void RemoveObserver(KeyValueObserver observer, String forKey) {
		if (this.observers.containsKey(forKey))
			if (this.observers.get(forKey).contains(observer))
				this.observers.get(forKey).remove(observer);
	}
	
	public void RemoveObserver(KeyValueObserver observer) {
		Set<String> keys = this.observers.keySet();
		if (keys.size() > 0)
			for (String key : keys) {
				this.RemoveObserver(observer, key);
		}
	}

	protected static String[] ListenerMethods = {
		"OnEvent",
		"OnEventDidStart",
		"OnEventDidEnd"
	};


	@Override
	public void OnEventDidStart(String theEvent, Object args){
		String arg = args != null ? args.toString() : "N/A";
        System.out.println(this.uniqueName() + " => Event Started: " + theEvent + " ("+ NotificationCenter.statusOf(theEvent) +"), with args: " + arg);
    }
	
	@Override
	public void OnEventDidEnd(String theEvent, Object args){
		String arg = args != null ? args.toString() : "N/A";
        System.out.println(this.uniqueName() + " => Event Ended: " + theEvent + " ("+ NotificationCenter.statusOf(theEvent) +"), with args: " + arg);
    }
	
	@Override
	public void SendMessage(String message, Object args){
		String arg = args != null ? args.toString() : "N/A";
        System.out.println(this.uniqueName() + " => Message Received: " + message + ", with args: " + arg);
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
		this.WillChangeValueForKey("name");
//		this.name = name;
		this.values.put("name", name);
		this.DidChangeValueForKey("name");
	}
	
	@Override
	public void OnValueForKeyWillChange(String key, Object value, RMXObject sender) {
        System.out.println(this.uniqueName() + " >> " + sender.uniqueName() + " will change: " + key + ", from old value: " + value);
	}

	@Override
	public void OnValueForKeyDidChange(String key, Object value, RMXObject sender) {
		System.out.println(this.uniqueName() + " >> " + sender.uniqueName() + " did change: " + key + ", to new value: " + value);
	}

	@Override
	public EventListener clone() {
		// TODO Auto-generated method stub
		return null;
	}
}
