package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import vehiclecounter.model.VehicleProfile;
import vehiclecounter.service.VehicleSurveyDataProcessor;

public class VehicleSurveyDataProcessorTest {

	VehicleSurveyDataProcessor processor;

	@Before
	public void init() {
		processor = new VehicleSurveyDataProcessor();
	}

	@Test
	public void testVehicleDataProcessor() {

		/**
		 * A98186 A98333 A499718 A499886 A638379 B638382 A638520 B638523
		 */
		List<VehicleProfile> profiles = processor.processFile("testdata.txt");

		assertEquals(3, profiles.size());

		VehicleProfile profile = profiles.get(0);
		assertTrue(profile.getIsStraightDirection());
		assertEquals(
				"Straight: true Time: 1970-01-01 00:01:38.186|null|1970-01-01 00:01:38.333|null Speed: 61.22448979591837km/h",
				profile.toString());

		profile = profiles.get(1);
		assertTrue(profile.getIsStraightDirection());
		assertEquals(
				"Straight: true Time: 1970-01-01 00:08:19.718|null|1970-01-01 00:08:19.886|null Speed: 53.57142857142857km/h",
				profile.toString());

		profile = profiles.get(2);
		assertFalse(profile.getIsStraightDirection());
		assertEquals(
				"Straight: false Time: 1970-01-01 00:10:38.379|1970-01-01 00:10:38.382|1970-01-01 00:10:38.520|1970-01-01 00:10:38.523 Speed: 63.829787234042556km/h",
				profile.toString());
	}

	@Test(expected = NullPointerException.class)
	public void testProccessorWithInvalidFile() {
		processor.processFile("testtesttest");
	}
}
