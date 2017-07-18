package vehiclecounter.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import vehiclecounter.model.VehicleProfile;

/**
 * Provides service
 * 
 * @author Jianwei Lu
 *
 */
public class VehicleSurveyDataProcessor {

	/**
	 * @param fileNameO
	 * @return list of vehicle profile
	 */
	public List<VehicleProfile> processFile(String fileName) {
		List<VehicleProfile> profiles = new LinkedList<>();
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(
					new InputStreamReader(this.getClass().getResourceAsStream("/" + fileName)));
			String line = bufferedReader.readLine();
			while (line != null) {
				VehicleProfile profile = new VehicleProfile();
				profile.setFrontWheelTime(getTime(line));

				line = bufferedReader.readLine();
				if (isOppositeDirection(line)) {
					profile.setIsStraightDirection(false);
					profile.setFrontWheelTime2(getTime(line));
					line = bufferedReader.readLine();
					profile.setRearWheelTime(getTime(line));
					line = bufferedReader.readLine();
					profile.setRearWheelTime2(getTime(line));
				} else {
					profile.setIsStraightDirection(true);
					profile.setRearWheelTime(getTime(line));
				}

				profiles.add(profile);
				line = bufferedReader.readLine();
			}
		} catch (IOException e) {
			// TODO log error
			e.printStackTrace();
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return profiles;
	}

	/**
	 * @param line
	 * @return time section of the given line entry
	 */
	private Long getTime(String line) {
		if (line != null && line.length() > 1) {
			String time = line.substring(1);
			return Long.valueOf(time);
		}
		return null;
	}

	/**
	 * @param line
	 * @return true if the give line (the 2nd line entry) indicates it is an
	 *         opposite direction
	 */
	private boolean isOppositeDirection(String line) {
		// TODO: move 'B' to a configuration file
		return line != null && line.charAt(0) == 'B';
	}
}
