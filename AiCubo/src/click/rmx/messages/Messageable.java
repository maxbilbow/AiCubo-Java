package click.rmx.messages;

import java.lang.reflect.InvocationTargetException;

public interface Messageable {
	/**
     *   @author Max Bilbow, 15-08-04 16:08:55
     *
     *   Receives a message
     *   Has to be overridden for to add specific method handing
     *   as it is currently not automatic to call a method this way
     *   @param message Name of selector or any other message
     *   @param args    any object.
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
     *   @since 0.1
     */
    public void sendMessage(String message, Object args) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException;
    
    
    /**
     *   @author Max Bilbow, 15-08-04 16:08:55
     *
     *   Sends message to any children who are listenting
     *   @param message Name of selector or any other message
     *   @param args    any object.
     *   @since 0.1
     */
    public void broadcastMessage(String message);
    
    /**
     *   @author Max Bilbow, 15-08-04 16:08:55
     *
     *   Sends message to any children who are listenting
     *   @param message Name of selector or any other message
     *   @param args    any object.
     *   @since 0.1
     */
    public void broadcastMessage(String message, Object args);
    
    
}
