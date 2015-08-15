package rmx;

public interface KeyValueObserver {
	void OnValueForKeyWillChange(String key, Object value, RMXObject sender);
	void OnValueForKeyDidChange(String key, Object value, RMXObject sender);
}
