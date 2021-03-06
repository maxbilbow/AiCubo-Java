package click.rmx.messages;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import click.rmx.Bugger;



public final class NotificationCenter {
	
	private NotificationCenter() {	}
	public static NotificationCenter getInstance() {
		if(singleton == null) {
			  synchronized(NotificationCenter.class) {
		    	 if(singleton == null) {
		    	   singleton = new NotificationCenter();
		       }
		     }
		  }
		  return singleton;
	}
	private static NotificationCenter singleton;
	
	private ArrayList<IEventListener> listeners = new ArrayList<IEventListener> ();

	private HashMap<String, EventStatus> events = new HashMap<String, EventStatus> ();

	public boolean hasListener(IEventListener listener) {
		return listeners.contains(listener);
	}
	
	public void Reset(String theEvent) {
		events.put(theEvent, EventStatus.Idle);
	}

	public void addListener(IEventListener listener) {
		if (!hasListener(listener)) 
			listeners.add(listener);
	}

	public boolean removeListener(IEventListener listener) {
		return listeners.remove(listener);
	}

	public EventStatus statusOf(String theEvent) {
		try {
			return events.get(theEvent);
		} catch (NullPointerException e) {
			events.put(theEvent, EventStatus.Idle);
			return EventStatus.Idle;
		}
	}

	public boolean isIdle(String theEvent) {
		return statusOf (theEvent) == EventStatus.Idle;
	}

	public boolean isActive(String theEvent) {
		return statusOf (theEvent) == EventStatus.Active;
	}

	public void EventDidOccur(String e) {
		EventDidOccur (e, null);
	}

	public void EventDidOccur(String theEvent, Object o) {
		EventWillStart(theEvent, o);
		EventDidEnd(theEvent, o);
	}

	public boolean WasCompleted(String theEvent) {
		return statusOf (theEvent) == EventStatus.Completed;
	}

	public void EventWillStart(String theEvent) {
		EventWillStart (theEvent, null);
	}

	public void EventWillStart(String theEvent, Object o) {
		if (!isActive (theEvent)) {
			events.put(theEvent, o!=null && o.getClass() == EventStatus.class ? (EventStatus) o : EventStatus.Active);
			for (IEventListener listener : listeners) {
				if (listener.implementsMethod("onEventDidStart", String.class, Object.class))
					listener.onEventDidStart(theEvent, o);
			}
		}
	}
	public void EventDidEnd(String theEvent) {
		
		EventDidEnd (theEvent, null);
	}
	public void EventDidEnd(String theEvent, Object o) {
		events.put(theEvent, o!=null&& o.getClass() == EventStatus.class ? (EventStatus) o : EventStatus.Completed);
		for (IEventListener listener : listeners) {
			if (listener.implementsMethod("onEventDidEnd", String.class, Object.class))
				listener.onEventDidEnd(theEvent, o);
			
		}
	}

	public void BroadcastMessage(String message, Object[] args) {
		for (IEventListener listener : listeners) {
			try {
				listener.sendMessage(message, args);
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				Bugger.log(this.getClass().getName() + " " + e.getMessage() + ": " + message + " with args: " + args);
//				e.printStackTrace();
			}
		}
	}

}
