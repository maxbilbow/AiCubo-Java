package com.maxbilbow.aicubo.messages;


import com.maxbilbow.aicubo.model.core.IRMXObject;

public interface KeyValueObserver
{
  void onValueForKeyWillChange(String key, Object value, IRMXObject sender);

  void onValueForKeyDidChange(String key, Object value, IRMXObject sender);
}
