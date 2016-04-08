package com.maxbilbow.aicubo.messages;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

@Component
public final class NotificationCenter
{

  private Logger mLogger = LoggerFactory.getLogger(NotificationCenter.class);

  public NotificationCenter()
  {
    singleton = this;
  }

  public static NotificationCenter getInstance()
  {
    if (singleton == null)
    {
      LoggerFactory.getLogger(NotificationCenter.class).warn("Notification Center not active but singleton called.");
      return new NotificationCenter();
    }
    return singleton;
  }

  private static NotificationCenter singleton;

  private ArrayList<IEventListener> listeners = new ArrayList<IEventListener>();

  private HashMap<String, EventStatus> events = new HashMap<String, EventStatus>();

  public boolean hasListener(IEventListener listener)
  {
    return listeners.contains(listener);
  }

  public void Reset(String theEvent)
  {
    events.put(theEvent, EventStatus.Idle);
  }

  public void addListener(IEventListener listener)
  {
    if (!hasListener(listener))
    {
      listeners.add(listener);
    }
  }

  public boolean removeListener(IEventListener listener)
  {
    return listeners.remove(listener);
  }

  public EventStatus statusOf(String theEvent)
  {
    try
    {
      return events.get(theEvent);
    }
    catch (NullPointerException e)
    {
      events.put(theEvent, EventStatus.Idle);
      return EventStatus.Idle;
    }
  }

  public boolean isIdle(String theEvent)
  {
    return statusOf(theEvent) == EventStatus.Idle;
  }

  public boolean isActive(String theEvent)
  {
    return statusOf(theEvent) == EventStatus.Active;
  }

  public void EventDidOccur(String e)
  {
    EventDidOccur(e, null);
  }

  public void EventDidOccur(String theEvent, Object o)
  {
    EventWillStart(theEvent, o);
    EventDidEnd(theEvent, o);
  }

  public boolean WasCompleted(String theEvent)
  {
    return statusOf(theEvent) == EventStatus.Completed;
  }

  public void EventWillStart(String theEvent)
  {
    EventWillStart(theEvent, null);
  }

  public void EventWillStart(String theEvent, Object o)
  {
    if (!isActive(theEvent))
    {
      events.put(theEvent, o != null && o.getClass() == EventStatus.class ? (EventStatus) o : EventStatus.Active);
      for (IEventListener listener : listeners)
      {
        if (listener.implementsMethod("onEventDidStart", String.class, Object.class))
        {
          listener.onEventDidStart(theEvent, o);
        }
      }
    }
  }

  public void EventDidEnd(String theEvent)
  {

    EventDidEnd(theEvent, null);
  }

  public void EventDidEnd(String theEvent, Object o)
  {
    events.put(theEvent, o != null && o.getClass() == EventStatus.class ? (EventStatus) o : EventStatus.Completed);
    for (IEventListener listener : listeners)
    {
      if (listener.implementsMethod("onEventDidEnd", String.class, Object.class))
      {
        listener.onEventDidEnd(theEvent, o);
      }

    }
  }

  public void BroadcastMessage(String message, Object[] args)
  {
    for (IEventListener listener : listeners)
    {
      try
      {
        listener.sendMessage(message, args);
      }
      catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
              | InvocationTargetException e)
      {
        mLogger.debug(this.getClass().getName() + " " + e.getMessage() + ": " + message + " with args: " + args);
//				e.printStackTrace();
      }
    }
  }

}
