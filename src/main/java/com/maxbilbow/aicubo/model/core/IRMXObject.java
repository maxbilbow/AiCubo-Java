package com.maxbilbow.aicubo.model.core;

import com.maxbilbow.aicubo.messages.IEventListener;
import com.maxbilbow.aicubo.messages.KeyValueObserver;

import java.lang.reflect.InvocationTargetException;

public interface IRMXObject extends IEventListener, KeyValueObserver, Categorizable
{

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