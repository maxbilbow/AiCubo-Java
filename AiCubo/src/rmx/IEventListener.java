package rmx;

public interface IEventListener {

	void onEventDidStart(String theEvent, Object args);
	void onEventDidEnd(String theEvent, Object args);
	void sendMessage(String message, Object sendMessageOptions);
	void doesOverrideMethod(String methodName);
}
