package com.maxbilbow.aicubo.messages;


import com.maxbilbow.aicubo.core.IRMXObject;

public interface KeyValueObserver
{
  void onValueForKeyWillChange(String key, Object value, IRMXObject sender);

  void onValueForKeyDidChange(String key, Object value, IRMXObject sender);
}
