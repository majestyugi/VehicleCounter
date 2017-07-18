package utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static vehiclecounter.utility.VehicleCounterUtility.getTime;

import org.junit.Test;

public class VehicleCounterUtilityTest {

	@Test
	public void testGetTime() {
		assertNull(getTime(null));
		assertEquals("1970-01-01 00:04:28.981", getTime(268981L));
		assertEquals("1970-01-01 00:10:04.957", getTime(604957L));
	}
}
