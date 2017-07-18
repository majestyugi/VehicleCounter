package vehiclecounter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import vehiclecounter.model.VehicleProfile;
import vehiclecounter.service.ReportService;
import vehiclecounter.service.VehicleSurveyDataProcessor;

public class VehicleCounterMain {

	public static void main(String[] args) {

		VehicleSurveyDataProcessor processor = new VehicleSurveyDataProcessor();
		List<VehicleProfile> profiles = processor.processFile("data.txt");

		ReportService service = new ReportService();

		System.out.println(
				"Vehicle from Thu Jan 01 1970 05:57:31 GMT+0000 to Thu Jan 01 1970 06:57:31 GMT+0000, Northbound only");
		Stream<VehicleProfile> result1 = service.filterVehicleTime(service.filterDirection(profiles.stream(), true),
				21451000L, 25051000L);
		Supplier<Stream<VehicleProfile>> supplier = () -> result1;
		List<VehicleProfile> filtedResults = new ArrayList<>(supplier.get().collect(Collectors.toList()));
		filtedResults.stream().sorted(Comparator.comparing(VehicleProfile::getFrontWheelTime))
				.forEach(profile -> System.out.println(profile));
		System.out.println("Speed(km/h) in this period is between: " + service.getMinSpeed(profiles) + " to "
				+ service.getMaxSpeed(profiles) + " with average " + service.getAverageSpeed(filtedResults)
				+ " and standard deviation " + service.getSpeedStandardDeviation(profiles));
		System.out.println("Average distance in this period is: " + service.getAverageDistanceInMeter(filtedResults));

		System.out.println("================================================================");
		System.out.println(
				"Vehicle from Thu Jan 01 1970 05:57:31 GMT+0000 to Thu Jan 01 1970 06:57:31 GMT+0000, Northbound only, Night time only");
		Stream<VehicleProfile> result2 = service.filterDayNight(filtedResults.stream(), false);
		supplier = () -> result2;
		filtedResults = new ArrayList<>(supplier.get().collect(Collectors.toList()));
		filtedResults.stream().forEach(profile -> System.out.println(profile));
		System.out.println("Speed(km/h) in this period is between: " + service.getMinSpeed(profiles) + " to "
				+ service.getMaxSpeed(profiles) + " with average " + service.getAverageSpeed(filtedResults)
				+ " and standard deviation " + service.getSpeedStandardDeviation(profiles));
		System.out.println("Average distance in this period is: " + service.getAverageDistanceInMeter(filtedResults));

	}
}
