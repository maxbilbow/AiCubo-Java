package rmx;

import java.util.HashMap;
import java.util.LinkedList;

public class NotificationCenter {
	
	private static LinkedList<EventListener> listeners = new LinkedList<EventListener> ();

	private static HashMap<String, EventStatus> events = new HashMap<String, EventStatus> ();

	//private static NotificationCenter singleton = new NotificationCenter();
	
//	protected NotificationCenter() {
//		this.events = new Dictionary<String, EventStatus> ();
//	}
//	public static NotificationCenter defaultCenter() {
//		return singleton;
//	}

	public static boolean hasListener(EventListener listener) {
		return listeners.contains(listener);
	}
	
	public static void Reset(String theEvent) {
		events.put(theEvent, EventStatus.Idle);
	}

	public static void addListener(EventListener listener) {
		if (!hasListener(listener)) {
			listeners.add(listener);
		}
//		if (Bugger.WillLog (RMXTests.EventCenter, listener.GetType () + " was added to Listeners ("+ Listeners.Count + ")"))
//			Debug.Log (Bugger.Last);
//		if (Bugger.WillLog(Testing.EventCenter, "Listeners: " + Listeners.Count))
//			Debug.Log (Bugger.Last);

	}

	public static boolean removeListener(EventListener listener) {
		return listeners.remove(listener);
	}

	public static EventStatus statusOf(String theEvent) {
		try {
			return events.get(theEvent);
		} catch (NullPointerException e) {
			events.put(theEvent, EventStatus.Idle);
			return EventStatus.Idle;
		}
	}

	public static boolean isIdle(String theEvent) {
		return statusOf (theEvent) == EventStatus.Idle;
	}

	public static boolean isActive(String theEvent) {
		return statusOf (theEvent) == EventStatus.Active;
	}

	public static void EventDidOccur(String e) {
		EventDidOccur (e, null);
	}

	public static void EventDidOccur(String theEvent, Object o) {
		
		EventWillStart(theEvent, o);
		EventDidEnd(theEvent, o);
	}


	public static boolean WasCompleted(String theEvent) {
		return statusOf (theEvent) == EventStatus.Completed;
	}

	public static void EventWillStart(String theEvent) {
		EventWillStart (theEvent, null);
	}

	public static void EventWillStart(String theEvent, Object o) {
		if (!isActive (theEvent)) {
			events.put(theEvent, o.getClass() == EventStatus.class ? (EventStatus) o : EventStatus.Active);
			for (EventListener listener : listeners) {
				listener.OnEventDidStart(theEvent, o);
			}
		}
	}
	public static void EventDidEnd(String theEvent) {
		
		EventDidEnd (theEvent, null);
	}
	public static void EventDidEnd(String theEvent, Object o) {
		events.put(theEvent, o.getClass() == EventStatus.class ? (EventStatus) o : EventStatus.Completed);
		for (EventListener listener : listeners) {
			listener.OnEventDidEnd(theEvent, o);
		}
	}

	public static void NotifyListeners(String message, Object args) {
		for (EventListener listener : listeners) {
			listener.SendMessage(message, args);
		}
	}

}
