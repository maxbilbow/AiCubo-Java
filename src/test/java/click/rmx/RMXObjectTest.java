/**
 * 
 */
package click.rmx;

import static click.rmx.Tests.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import click.rmx.testsuite.ExcludeCat;
import click.rmx.testsuite.FoundationTest;

/**
 * @author bilbowm
 *
 */
public class RMXObjectTest  {

	RMXObject object;
	final String name = "Fred";
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		System.out.println("setUp");
		object = new RMXObject();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		System.out.println("tearDown");
		object = null;
	}

	@Test
	@Category({FoundationTest.class, ExcludeCat.class})
	public void test() {
//		messageWasNotReceived();
		todo();
		fail();
	}
	
	@Test
	@Category(FoundationTest.class)
	public void messageWasNotReceived() {
		todo();
	}

	
}
