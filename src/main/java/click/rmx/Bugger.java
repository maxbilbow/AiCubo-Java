package click.rmx;

import java.io.FileWriter;
import java.io.IOException;
import static java.nio.file.Files.*;

import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;

import click.rmx.engine.GameController;


public class Bugger {
	public static  boolean logging = true;
	public static  boolean debug = true;
	
	private final LinkedList<String> logs = new LinkedList<>();

	private static Bugger singleton;
	private Bugger(){ 
		Runtime.getRuntime().addShutdownHook(new Thread() {
                    @Override
		    public void run() {
		        if (singleton != null)
		        	singleton.printAll(debug);
		    }
		});
	}
	
	public static Bugger getInstance() {
		if (singleton != null)
			return singleton;
		else {
			singleton = new Bugger();
		}
		return singleton;
	}
	
	public static void logAndPrint(Object o, boolean keep) {
		if (logging) {
			Bugger b = getInstance();
			String log = b.logMessage(o);
			if (!keep) {
				b.logs.removeLast();
			}
			System.out.println(log);
		}
	}
	
//	private void newLog() {
////		this.logs.addLast("");
//	}
	
	public static void log(Object o) {
		if (logging) 
			getInstance().logMessage(o);
	}
	int count = 1;
//	private String previousLog = "";
	
	
	public static void PrintTrace() {
		StackTraceElement[] trace = Thread.currentThread().getStackTrace();//[2];
		System.out.println("TRACE:");
		int i =0;
		for (StackTraceElement e : trace) {
			if (i++ > 0)
			System.out.println(e);
		}
	}
	
	private String logMessage(Object o) {
		
		StackTraceElement trace = Thread.currentThread().getStackTrace()[3];
		String theClass = trace.getClassName();
		String method = trace.getMethodName();
		int line = trace.getLineNumber();
		String time = java.time.LocalTime.now().toString();
		
		String newLog =  theClass + "::" + method + " on line " + line + ": " + String.valueOf(o);
		if (!this.logs.isEmpty() && logs.getLast().endsWith(newLog)) {
			logs.removeLast();
			count++;
		} else {
			count = 1;
		}
		String timestamp = "[" + time + "x" + count + "] ";
		newLog = timestamp + newLog;
		
		
		this.logs.addLast(newLog);	
		return newLog;
	}
	

	public void printAll(boolean toConsole) {
		String systemLog = "====== BEGIN LOG ======\n";
		Iterator<String> i = getInstance().logs.iterator();
//		String systemLog = "";
		while (i.hasNext()) { 
			systemLog +=  i.next() + "\n";
		}
		systemLog += "====== END OF LOG ======\n\n"; //= systemLog.substring(0, systemLog.length()) +
		if (toConsole)
			System.out.println(systemLog);
		if (logging) {
			FileWriter writer = null;
			try {
				createDirectories(Paths.get("bugger"));
				writer = new FileWriter("game_log_" + scope() + ".log", true);
				systemLog = systemLog.replace("\n", "\r\n");
				writer.write(systemLog);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (writer != null)
					try {
						writer.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
		}
	}
	
	String scope() {
		GameController gc = GameController.getInstance(); 
		if (gc != null)
			return gc.getClass().getSimpleName();
		else
			return "bugger-generic";
	}
	
	public static void test (String[] args) {
//		Bugger b = Bugger.getInstance();
		log("Hello!");
//		Print(true);
		
		log("Hello again!");
		logAndPrint("My Friends!", true); logAndPrint("My Friends!", true);

	
	}
}
