package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import mockit.Mocked;
import mockit.NonStrictExpectations;
import vehiclecounter.model.VehicleProfile;
import vehiclecounter.service.ReportService;

public class ReportServiceTest {

	List<VehicleProfile> profiles;

	ReportService reportService;

	@Mocked
	VehicleProfile profile1;

	@Mocked
	VehicleProfile profile2;

	@Mocked
	VehicleProfile profile3;

	@Before
	public void init() {

		profiles = new ArrayList<VehicleProfile>(3);
		profiles.add(profile1);
		profiles.add(profile2);
		profiles.add(profile3);

		new NonStrictExpectations() {
			{
				profile1.getFrontWheelTime();
				result = 1500181873000L; // Sun Jul 16 2017 05:11:13 UTC
				profile1.getRearWheelTime();
				result = 1500181873300L; // 300ms more than front wheel time
				profile1.getIsStraightDirection();
				result = false;
				profile1.toString();
				result = "profile 1";

				profile2.getFrontWheelTime();
				result = 1500192673000L; // Sun Jul 16 2017 08:11:13 UTC
				profile2.getRearWheelTime();
				result = 1500192673300L; // 300ms more than front wheel time
				profile2.getIsStraightDirection();
				result = false;
				profile2.toString();
				result = "profile 2";

				profile3.getFrontWheelTime();
				result = 1500228673000L; // Sun Jul 16 2017 18:11:13 UTC
				profile3.getRearWheelTime();
				result = 1500228673300L; // 300ms more than front wheel time
				profile3.getIsStraightDirection();
				result = true;
				profile3.toString();
				result = "profile 3";
			}
		};
		reportService = new ReportService();
	}

	@Test
	public void testFilterVehicleTime() {
		List<VehicleProfile> results = reportService.filterVehicleTime(profiles.stream(), null, 1500192673000L)
				.collect(Collectors.toList());
		assertEquals(1, results.size());
		assertTrue(results.contains(profile1));

		// Sun Jul 16 2017 08:11:13 UTC to Sun Jul 16 2017 18:11:13:300 UTC
		results = reportService.filterVehicleTime(profiles.stream(), 1500192673000L, 1500228673300L)
				.collect(Collectors.toList());
		assertEquals(2, results.size());
		assertTrue(results.contains(profile2));
		assertTrue(results.contains(profile3));

		// from Sun Jul 16 2017 07:11:13 UTC to Sun Jul 16 2017 17:11:13 UTC
		results = reportService.filterVehicleTime(profiles.stream(), 1500189073000L, 1500225073000L)
				.collect(Collectors.toList());
		assertEquals(1, results.size());
		assertTrue(results.contains(profile2));
	}

	@Test
	public void testFilterDirection() {
		List<VehicleProfile> results = reportService.filterDirection(profiles.stream(), true)
				.collect(Collectors.toList());
		assertEquals(1, results.size());
		assertEquals(results.get(0), profile3);

		results = reportService.filterDirection(profiles.stream(), false).collect(Collectors.toList());
		assertEquals(2, results.size());
		assertTrue(results.contains(profile1));
		assertTrue(results.contains(profile2));
	}

	@Test
	public void testFilterDayTime() {

		List<VehicleProfile> results = reportService.filterDayNight(profiles.stream(), true)
				.collect(Collectors.toList());
		assertEquals(1, results.size());
		assertEquals(results.get(0), profile2);

		results = reportService.filterDayNight(profiles.stream(), false).collect(Collectors.toList());
		assertEquals(2, results.size());
		assertTrue(results.contains(profile1));
		assertTrue(results.contains(profile3));
	}

	@Test
	public void testFilterPeakTime() {

		List<VehicleProfile> results = reportService.filterPeak(profiles.stream(), true).collect(Collectors.toList());
		assertEquals(2, results.size());
		assertTrue(results.contains(profile2));
		assertTrue(results.contains(profile3));

		results = reportService.filterPeak(profiles.stream(), false).collect(Collectors.toList());
		assertEquals(1, results.size());
		assertTrue(results.contains(profile1));
	}

	@Test
	public void testGetSpeedService() {

		new NonStrictExpectations() {
			{
				profile1.getSpeed();
				result = 1;

				profile2.getSpeed();
				result = 2;

				profile3.getSpeed();
				result = 3;
			}
		};
		assertEquals("2.0", String.valueOf(reportService.getAverageSpeed(profiles)));
		assertEquals("1.0", String.valueOf(reportService.getMinSpeed(profiles)));
		assertEquals("3.0", String.valueOf(reportService.getMaxSpeed(profiles)));

		// sqrt(2/3)
		assertEquals("0.816496580927726", String.valueOf(reportService.getSpeedStandardDeviation(profiles)));

		new NonStrictExpectations() {
			{
				profile1.getSpeed();
				result = 6;

				profile2.getSpeed();
				result = 6;

				profile3.getSpeed();
				result = 6;
			}
		};
		assertEquals("6.0", String.valueOf(reportService.getAverageSpeed(profiles)));
		assertEquals("6.0", String.valueOf(reportService.getMinSpeed(profiles)));
		assertEquals("6.0", String.valueOf(reportService.getMaxSpeed(profiles)));

		// sqrt(2/3)
		assertEquals("0.0", String.valueOf(reportService.getSpeedStandardDeviation(profiles)));
	}

	@Test
	public void testAverageDistance() {
		new NonStrictExpectations() {
			{
				profile1.getFrontWheelTime();
				result = 1500228000000L; // Sun Jul 16 2017 18:00:00 UTC

				profile2.getFrontWheelTime();
				result = 1500228060000L; // Sun Jul 16 2017 18:01:00 UTC

				profile3.getFrontWheelTime();
				result = 1500228090000L; // Sun Jul 16 2017 18:01:30 UTC
			}
		};
		profiles = new ArrayList<VehicleProfile>(1);
		profiles.add(profile1);
		assertEquals("0", String.valueOf(reportService.getAverageDistanceInMeter(profiles)));

		profiles.add(profile2);
		// distance = 1km when driving 60km/h on 1 minute
		assertEquals("1000.00", String.valueOf(reportService.getAverageDistanceInMeter(profiles)));

		profiles.add(profile3);
		// distance = 1km when driving 60km/h on 1 minute
		// distance between car1 and car2 should be 1000m and car2 and car3
		// should be 500m
		assertEquals("750.00", String.valueOf(reportService.getAverageDistanceInMeter(profiles)));

		// the distance between car1 and car3 should be 1500m
		profiles.remove(profile2);
		assertEquals("1500.00", String.valueOf(reportService.getAverageDistanceInMeter(profiles)));
	}
}
