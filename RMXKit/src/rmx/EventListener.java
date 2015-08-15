package rmx;


/**
 * 
 *   @author Max Bilbow, 15-08-04 16:08:32
 *
 *   @brief  abstract class for EventListeners
 *   @see NotificationCenter
 *   @since 0.1
 */
public abstract class EventListener {
    
   
       
    /**
     *  @author Max Bilbow, 15-08-04 16:08:30
     *
     *  Notify's all active listeners that an event is about to start
     *  @param theEvent as string
     *  @param args     anything
     *  @since 0.1
     */
    public abstract void OnEventDidStart(String theEvent, Object args);
    
    /**
     *  @author Max Bilbow, 15-08-04 16:08:53
     *
     *  Notify's all active listeners that an event did start
     *  @param theEvent string identifier
     *  @param args     anything
     *  @since 0.1
     */
    public abstract void OnEventDidEnd(String theEvent, Object args); 

    
    /**
     *   @author Max Bilbow, 15-08-04 16:08:55
     *
     *   Receives a message
     *   Has to be overridden for to add specific method handing
     *   as it is currently not automatic to call a method this way
     *   @param message Name of selector or any other message
     *   @param args    any object.
     *   @since 0.1
     */
    public abstract void SendMessage(String message, Object args);

    /**
     *   @author Max Bilbow, 15-08-04 16:08:22
     *
     *   @brief  Inserts the object into Notification::listeners, if it isn't already inserted.
     *
     *   @see NotificationCenter::addListener(listener)
     *   @since 0.1
     */
    public void StartListening(){
    	NotificationCenter.addListener(this);
    }
    
    /**
     *   @author Max Bilbow, 15-08-04 16:08:22
     *
     *   @brief  Removes the object from Notification::listeners, if it exists in the list.
     *   @see NotificationCenter::removeListener(listener)
     *   @since <#0.1#>
     */
    public void StopListening() {
    	NotificationCenter.removeListener(this);
	}

    
    ///Extends the Object::clone() method so that the listening status of the object is also copied.
    ///@see NotificationCenter::addListener(listener);
    public abstract EventListener clone();

};