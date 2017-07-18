package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.Verifications;
import vehiclecounter.model.VehicleProfile;
import vehiclecounter.utility.VehicleCounterUtility;

public class VehicleProfileTest {

	@Mocked
	VehicleCounterUtility mockedVehicleCounterUtility;

	@Test
	public void testVehicleProfile() {
		long time1 = 0L, time2 = 100L, time3 = 50L, time4 = 150L;
		VehicleProfile profile = createVehicleProfile(false, time1, time2, time3, time4);

		assertEquals(profile.getFrontWheelTime().longValue(), 0L);
		assertEquals(profile.getFrontWheelTime2().longValue(), 100L);
		assertEquals(profile.getRearWheelTime().longValue(), 50L);
		assertEquals(profile.getRearWheelTime2().longValue(), 150L);
		assertFalse(profile.getIsStraightDirection());

		new NonStrictExpectations() {
			{
				mockedVehicleCounterUtility.getTime(time1);
				result = "test";

				mockedVehicleCounterUtility.getTime(time2);
				result = "abc";
			}
		};

		new NonStrictExpectations(profile) {
			{
				profile.getSpeed();
				result = 10.0;
			}
		};
		assertEquals(profile.toString(), "Straight: false Time: test|abc|null|null Speed: 10.0km/h");

		new Verifications() {
			{
				// should invoke 4 times as there are 4 wheels to getTime
				mockedVehicleCounterUtility.getTime(anyLong);
				times = 4;
			}
		};
	}

	@Test
	public void testVehicleProfileSpeed() {
		VehicleProfile profile = createVehicleProfile(null, 0L, null, 150L, null);
		assertEquals("It takes for 150 milliseconds for a car to drive at speed 60km/h for distance 2.5m", "60.0",
				String.valueOf(profile.getSpeed()));

		profile = createVehicleProfile(null, 0L, null, 200L, null);
		assertEquals("It takes for 150 milliseconds for a car to drive at speed 60km/h for distance 2.5m", "45.0",
				String.valueOf(profile.getSpeed()));

		profile = createVehicleProfile(null, 0L, null, 100L, null);
		assertEquals("It takes for 150 milliseconds for a car to drive at speed 60km/h for distance 2.5m", "90.0",
				String.valueOf(profile.getSpeed()));
	}

	private VehicleProfile createVehicleProfile(Boolean direction, Long frontWheelTime, Long frontWheelTime2,
			Long rearWheelTime, Long rearWheelTime2) {
		VehicleProfile profile = new VehicleProfile();
		profile.setIsStraightDirection(direction);
		profile.setFrontWheelTime(frontWheelTime);
		profile.setFrontWheelTime2(frontWheelTime2);
		profile.setRearWheelTime(rearWheelTime);
		profile.setRearWheelTime2(rearWheelTime2);
		return profile;
	}
}
