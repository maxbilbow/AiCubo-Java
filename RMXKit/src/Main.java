


import rmx.NotificationCenter;
import rmx.RMXObject;
import rmx.StringFormatter;
import rmx.db.Database;

public class Main {

	public static void main(final String[] args) {
//		NotificationCenter.init();
		RMXObject o = new RMXObject();
		RMXObject o2 = new RMXObject();
		o.AddObserver(o2, "name");
		
		o.setName("New Name");
		
		StringFormatter sf = new StringFormatter("One two three four five!");
		sf.newTextInput("One two three four five!");
		NotificationCenter.EventWillStart("ReverseString", sf);
		sf.reverse();
		NotificationCenter.EventDidEnd("ReverseString", sf);
		
		System.out.println("\nDid all that...");
		
		Database.test();
	}

}
