package click.rmx;

public class Tests {
	public static void todo() {
		StackTraceElement trace = Thread.currentThread().getStackTrace()[2];
		String method = trace.getMethodName();
		System.out.println(method + ": not yet implemented");
	}
}
