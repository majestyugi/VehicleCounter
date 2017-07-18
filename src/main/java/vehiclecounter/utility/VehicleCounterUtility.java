package vehiclecounter.utility;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Provides utility functions
 * 
 * @author Jianwei Lu
 *
 */
public class VehicleCounterUtility {

	/** TODO pattern move to configuration item */
	private static final DateTimeFormatter DEFAULT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

	private static final ZoneId DEFAULT_ZONE = ZoneId.of("UTC");

	/**
	 * @param milliSeconds
	 * @return the zoned time in milliseconds
	 */
	public static ZonedDateTime getZonedTime(Long milliSeconds) {
		if (milliSeconds != null) {
			return Instant.ofEpochMilli(milliSeconds).atZone(DEFAULT_ZONE);
		}
		return null;
	}

	/**
	 * @param milliSeconds
	 * @return the string format for the time in milliseconds
	 */
	public static String getTime(Long milliSeconds) {
		if (milliSeconds != null) {
			return getZonedTime(milliSeconds).format(DEFAULT_FORMAT);
		}
		return null;
	}
}
