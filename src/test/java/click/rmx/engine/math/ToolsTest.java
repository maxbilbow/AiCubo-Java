/**
 * 
 */
package click.rmx.engine.math;
import static click.rmx.engine.math.Tools.*;
import static org.junit.Assert.*;

import org.junit.Test;


/**
 * @author Max
 *
 */
public class ToolsTest {

	
	@Test
	public void test() {
		int min = 4; int max = 5;
		for (int i = 0; i<10000000;++i) {
			double result = Tools.rBounds(min,max);
			assertTrue(result + " out of bounds: " + min + " ≤ x < " + max, result >= min && result < max || min == max && max == result);
//			System.out.println(d);
		}
		
		min = -4; max = 5;
		for (int i = 0; i<10000000;++i) {
			double result = Tools.rBounds(min,max);
			assertTrue(result + " out of bounds: " + min + " ≤ x < " + max, result >= min && result < max || min == max && max == result);
//			System.out.println(d);
		}
		
		min = -4; max = -3;
		for (int i = 0; i<10000000;++i) {
			double result = Tools.rBounds(min,max);
			assertTrue(result + " out of bounds: " + min + " ≤ x < " + max, result >= min && result < max || min == max && max == result);
//			System.out.println(d);
		}
//		fail("Not yet implemented");
	}

}
