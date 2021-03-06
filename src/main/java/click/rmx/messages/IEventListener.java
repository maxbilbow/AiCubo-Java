package click.rmx.messages;


/**
 * 
 *   @author Max Bilbow, 15-08-04 16:08:32
 *
 *   @brief  abstract class for EventListeners
 *   @see NotificationCenter
 *   @since 0.1
 */
public interface IEventListener extends IMessageable {
    
   
       
    /**
     *  @author Max Bilbow, 15-08-04 16:08:30
     *
     *  Notify's all active listeners that an event is about to start
     *  @param theEvent as string
     *  @param args     anything
     *  @since 0.1
     */
    public abstract void onEventDidStart(String theEvent, Object args);
    
    /**
     *  @author Max Bilbow, 15-08-04 16:08:53
     *
     *  Notify's all active listeners that an event did start
     *  @param theEvent string identifier
     *  @param args     anything
     *  @since 0.1
     */
    public abstract void onEventDidEnd(String theEvent, Object args); 

    
    
   

    /**
     *   @author Max Bilbow, 15-08-04 16:08:22
     *
     *   @brief  Inserts the object into Notification::listeners, if it isn't already inserted.
     *
     *   @see NotificationCenter::addListener(listener)
     *   @since 0.1
     */
    public void startListening();
    
    /**
     *   @author Max Bilbow, 15-08-04 16:08:22
     *
     *   @brief  Removes the object from Notification::listeners, if it exists in the list.
     *   @see NotificationCenter::removeListener(listener)
     *   @since <#0.1#>
     */
    public void stopListening();

    
};