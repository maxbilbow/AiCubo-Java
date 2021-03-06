package click.rmx;

import static click.rmx.Tests.todo;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;

import click.rmx.messages.KeyValueObserver;

public class WeakObject<E extends IRMXObject> extends WeakReference<E> implements IRMXObject {

	
	public WeakObject(E referent) {
		super(referent);
	}

	public WeakObject(E referent, ReferenceQueue<? super E> q) {
		super(referent, q);
	}

	@Override
	public int uniqueID() {
		return this.get().uniqueID();
	}

	@Override
	public Object setValue(String forKey, Object value) {
		return this.get().setValue(forKey, value);
	}

	@Override
	public Object getValue(String forKey) {
		return this.get().getValue(forKey);
	}

	@Override
	public Object getValueOrSetDefault(String forKey, Object value) {
		return this.get().getValueOrSetDefault(forKey, value);
	}

	@Override
	public void AddObserver(KeyValueObserver observer, String forKey) {
		this.get().AddObserver(observer, forKey);
	}

	@Override
	public void removeObserver(KeyValueObserver observer, String forKey) {
		this.get().removeObserver(observer, forKey);
	}

	@Override
	public void removeObserver(KeyValueObserver observer) {
		this.get().removeObserver(observer);

	}

	@Override
	public void sendMessage(String message)
			throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		this.get().sendMessage(message);

	}

	@Override
	public String uniqueName() {
		return this.get().uniqueName();
	}

	@Override
	public String getName() {
		return this.get().getName();
	}

	@Override
	public void setName(String name) {
		this.get().setName(name);
	}

	@Override
	public void onEventDidStart(String theEvent, Object args) {
		// TODO Auto-generated method stub
		todo();
	}

	@Override
	public void onEventDidEnd(String theEvent, Object args) {
		// TODO Auto-generated method stub
		todo();
	}

	@Override
	public void startListening() {
		// TODO Auto-generated method stub
		todo();
	}

	@Override
	public void stopListening() {
		// TODO Auto-generated method stub
		todo();
	}

	@Override
	public void sendMessage(String message, Object args) throws NoSuchMethodException, SecurityException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// TODO Auto-generated method stub
		todo();
	}

	@Override
	public void broadcastMessage(String message) {
		// TODO Auto-generated method stub
		todo();
	}

	@Override
	public void broadcastMessage(String message, Object args) {
		// TODO Auto-generated method stub
		todo();
	}

	@Override
	public boolean implementsMethod(String method, Class<?>... args) {
		// TODO Auto-generated method stub
		todo();
		return false;
	}

	@Override
	public void onValueForKeyWillChange(String key, Object value, IRMXObject sender) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onValueForKeyDidChange(String key, Object value, IRMXObject sender) {
		// TODO Auto-generated method stub
		todo();
		
	}



}
