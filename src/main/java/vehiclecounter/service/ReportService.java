package vehiclecounter.service;

import static java.math.RoundingMode.HALF_UP;
import static vehiclecounter.utility.VehicleCounterUtility.getZonedTime;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import vehiclecounter.model.VehicleProfile;
import vehiclecounter.utility.VehicleCounterUtility;

/**
 * Provides service for generating report
 * 
 * @author Jianwei Lu
 *
 */
public class ReportService {

	private static final int DAY_TIME_START = 6;

	private static final int DAY_TIME_END = 18;

	private static final double AVERAGE_SPEED = 60.0;

	/**
	 * @param startTime
	 *            - if startTime is null, then there is no limit for the lower
	 *            bound
	 * @param endTime
	 *            - if endTime is null, there there is no limit for the upper
	 *            bound
	 * @return number of vehicle given period
	 */
	public Stream<VehicleProfile> filterVehicleTime(Stream<VehicleProfile> profiles, Long startTime, Long endTime) {
		return profiles.filter(profile -> (startTime == null || startTime <= profile.getFrontWheelTime()))
				.filter(profile -> (endTime == null || profile.getRearWheelTime() <= endTime));
	}

	/**
	 * @param profiles
	 * @param isStraightDirection
	 * @return filtered profiles with given direction
	 */
	public Stream<VehicleProfile> filterDirection(Stream<VehicleProfile> profiles, boolean isStraightDirection) {
		return profiles.filter(profile -> isStraightDirection == Boolean.valueOf(profile.getIsStraightDirection()));
	}

	/**
	 * @param profiles
	 * @param isDayTime
	 * @return filtered profiles to include daytime only if isDayTime is true,
	 *         filtered profiles to include night time only if isDayTime is
	 *         false
	 */
	public Stream<VehicleProfile> filterDayNight(Stream<VehicleProfile> profiles, boolean isDayTime) {
		return profiles.filter(profile -> isDayTime ? isWithinDayTime(profile.getFrontWheelTime())
				: !isWithinDayTime(profile.getFrontWheelTime()));
	}

	/**
	 * @param profiles
	 * @param isPeakTime
	 * @return filter profiles to include peak time only if isPeakTime is true,
	 *         include off peak only if isPeakTime is false
	 */
	public Stream<VehicleProfile> filterPeak(Stream<VehicleProfile> profiles, boolean isPeakTime) {
		return profiles.filter(profile -> isPeakTime ? isWithInPeak(profile.getFrontWheelTime())
				: !isWithInPeak(profile.getFrontWheelTime()));
	}

	/**
	 * @param time
	 * @return true if hour of the time is between day time (eg: 6am:6pm)
	 */
	private boolean isWithinDayTime(long time) {
		return VehicleCounterUtility.getZonedTime(time).getHour() >= DAY_TIME_START
				&& VehicleCounterUtility.getZonedTime(time).getHour() < DAY_TIME_END;
	}

	/**
	 * @param time
	 * @return true if hour of the time is between peak time (eg: 7am-9am and
	 *         17:00-19.00)
	 */
	private boolean isWithInPeak(long time) {
		// TODO move this to config
		// TODO minutes needed to be included?
		return (getZonedTime(time).getHour() >= 7 && getZonedTime(time).getHour() < 9)
				|| (getZonedTime(time).getHour() >= 17 && getZonedTime(time).getHour() < 19);
	}

	/**
	 * @param profiles
	 * @return the average speed by given profiles
	 */
	public double getAverageSpeed(List<VehicleProfile> profiles) {
		return profiles.stream().mapToDouble(VehicleProfile::getSpeed).average().getAsDouble();
	}

	/**
	 * @param profiles
	 * @return the min speed by given profiles
	 */
	public double getMinSpeed(List<VehicleProfile> profiles) {
		return profiles.stream().mapToDouble(VehicleProfile::getSpeed).min().getAsDouble();
	}

	/**
	 * @param profiles
	 * @return the average speed by given profiles
	 */
	public double getMaxSpeed(List<VehicleProfile> profiles) {
		return profiles.stream().mapToDouble(VehicleProfile::getSpeed).max().getAsDouble();
	}

	/**
	 * @param profiles
	 * @return the speed standard deviation by given profiles
	 */
	public double getSpeedStandardDeviation(List<VehicleProfile> profiles) {
		double sum = 0.0;
		double mean = getAverageSpeed(profiles);
		for (VehicleProfile profile : profiles) {
			sum += Math.pow((profile.getSpeed() - mean), 2.0);
		}
		return Math.sqrt(sum / profiles.size());
	}

	/**
	 * @param profiles
	 * @return the average distance in meters by given profiles
	 */
	public BigDecimal getAverageDistanceInMeter(List<VehicleProfile> profiles) {
		BigDecimal distance = BigDecimal.ZERO;
		if (profiles != null && profiles.size() > 1) {
			List<VehicleProfile> sortedProfiles = profiles.stream()
					.sorted(Comparator.comparing(VehicleProfile::getFrontWheelTime)).collect(Collectors.toList());

			Iterator<VehicleProfile> iterator = sortedProfiles.iterator();
			VehicleProfile profile = iterator.next();
			long currentTime = profile.getFrontWheelTime();
			while (iterator.hasNext()) {
				profile = iterator.next();
				// TODO move item to configuration files
				// 3600km/h = 1m/ms
				distance = distance.add(BigDecimal.valueOf(AVERAGE_SPEED / 3600)
						.multiply(BigDecimal.valueOf((profile.getFrontWheelTime() - currentTime))));
				currentTime = profile.getFrontWheelTime();
			}
			// if there are 3 cars, then the distance is calculated based on the
			// average between distance from car1 to car2 and car2 to car3
			distance = distance.divide(BigDecimal.valueOf(sortedProfiles.size() - 1L), HALF_UP);
			return distance.setScale(2, HALF_UP);
		}
		return distance;
	}
}
