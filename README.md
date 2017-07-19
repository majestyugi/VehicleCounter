# VehicleCounter
A system that collects vehicle survey data and provide different methods and utility for generating reports

------------
## Current design and main classes
- **VehicleProfile** - represents each survey record for a vehicle
- **VehicleSurveyDataProcessor** - read survey data from resource file and covert data into list of VehicleProfile
- **ReportService** - provide service to filtering VehicleProfile data, and services for providing report data
- **VehicleCounterMain** - main class for the application


------------
## TODO or to improve
- Add configuration module that allows data such as AVERAGE vehicle length to be configurable
- Assuming VehicleSurveyData will be provided say every month and we need to store all data into database, then need to add service to persist data and DataProcessor needs to invoke service for bulk update.
- Add search module (eg: vehicle data should be indexed by DataProcessor and ReportService provides searching and filtering)
- Convert project to springboot application, and search module may use spring-data-solr
- Add restful service for reports

------------
## Note
- To run, execute the VehicleCounterMain main method. Change the parameters for the ReportService to get/filter different types of data as needed. 
- To execute the test, make sure the jmockit jar is initialised, you would add the following to the java VM arguments
`-javaagent:"{path to the project}\VehicleCounter\lib\jmockit-1.8.jar" `

