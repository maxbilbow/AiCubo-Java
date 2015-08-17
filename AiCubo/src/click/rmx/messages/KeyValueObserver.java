package click.rmx.messages;

import click.rmx.RMXObject;

public interface KeyValueObserver {
	void onValueForKeyWillChange(String key, Object value, RMXObject sender);
	void onValueForKeyDidChange(String key, Object value, RMXObject sender);
}