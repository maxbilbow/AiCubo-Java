package click.rmx;

import java.lang.reflect.InvocationTargetException;

import click.rmx.messages.IEventListener;
import click.rmx.messages.KeyValueObserver;

public interface IRMXObject extends IEventListener, KeyValueObserver, Categorizable  {

	int uniqueID();

	Object setValue(String forKey, Object value);

	Object getValue(String forKey);

	Object getValueOrSetDefault(String forKey, Object value);

	void AddObserver(KeyValueObserver observer, String forKey);

	void removeObserver(KeyValueObserver observer, String forKey);

	void removeObserver(KeyValueObserver observer);

	void sendMessage(String message)
			throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;

	String uniqueName();

	String getName();

	void setName(String name);
	
}