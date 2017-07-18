package vehiclecounter.model;

import static vehiclecounter.utility.VehicleCounterUtility.getTime;

/**
 * Model class to present a vehicle profile, used to record an entry from the
 * VehicleSurveyData. <br/>
 * 
 * NOTE: there is one hose for facing direction but two hoses for opposite
 * directions
 * 
 * @author Jianwei Lu
 *
 */
public class VehicleProfile {

	/** the average wheelbase cars axles length in meters */
	private static final double VEHICLE_LENGTH = 2.5;

	/** the flag to indicate the vehicle is in the straight direction */
	private Boolean isStraightDirection;

	/** the time when vehicle front wheel hits the rubber hose */
	private Long frontWheelTime;

	/**
	 * the time when vehicle rear wheel hits the 2nd rubber host <br/>
	 * NOTE: this is applicable if opposite direction
	 */
	private Long frontWheelTime2;

	/** the time when vehicle front wheel hits the rubber hose */
	private Long rearWheelTime;

	/**
	 * the time when vehicle rear wheel hits the 2nd rubber host <br/>
	 * NOTE: this is applicable if opposite direction
	 */
	private Long rearWheelTime2;

	public Boolean getIsStraightDirection() {
		return isStraightDirection;
	}

	public void setIsStraightDirection(Boolean isStraightDirection) {
		this.isStraightDirection = isStraightDirection;
	}

	public Long getFrontWheelTime() {
		return frontWheelTime;
	}

	public void setFrontWheelTime(Long frontWheelTime) {
		this.frontWheelTime = frontWheelTime;
	}

	public Long getFrontWheelTime2() {
		return frontWheelTime2;
	}

	public void setFrontWheelTime2(Long frontWheelTime2) {
		this.frontWheelTime2 = frontWheelTime2;
	}

	public Long getRearWheelTime() {
		return rearWheelTime;
	}

	public void setRearWheelTime(Long rearWheelTime) {
		this.rearWheelTime = rearWheelTime;
	}

	public Long getRearWheelTime2() {
		return rearWheelTime2;
	}

	public void setRearWheelTime2(Long rearWheelTime2) {
		this.rearWheelTime2 = rearWheelTime2;
	}

	/**
	 * speed = distance / time <br/>
	 * 
	 * @return the speed in km/h
	 */
	public double getSpeed() {
		// 1000m = 1km
		// 1hr = 3600 seconds = 3600000 ms
		return VEHICLE_LENGTH / (rearWheelTime - frontWheelTime) * 3600;
	}

	@Override
	public String toString() {
		return "Straight: " + isStraightDirection + " Time: " + getTime(frontWheelTime) + "|" + getTime(frontWheelTime2)
				+ "|" + getTime(rearWheelTime) + "|" + getTime(rearWheelTime2) + " Speed: " + getSpeed() + "km/h";
	}
}
