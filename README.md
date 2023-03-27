Delivery api to calculate delivery cost based on weather conditions, location and vehicle type.
api accepts POST requests with json body containing city, vehicle, and optionally you can customize the timestamp, base fee and extra fees based on weather conditions.
By default, the timestamp is set to current time.
JSON body structure:
{
    "city": "tallinn",          #(required)
    "vehicle": "scooter",       #(required)
    "RBF": "5",                 #(optional) base fee
    "ATEF": "2",                #(optional) air temperature extra fee
    "WSEF": "1",                #(optional) wind speed extra fee
    "WPEF": "3",                #(optional) weather phenomenon extra fee
    "timestamp": "1679776185"   #(optional) timestamp
}

to configure the weather importing cron job go to application.properties and set the cron expression for the property: weather.import.cron 
example: weather.import.cron=0 15 * * * * (ex: every hour at 15 minutes past the hour)
by default the cron job is set to the example above.

example sql query to insert some test data into the database:
INSERT INTO WEATHER_DATA ( WMO_CODE , STATION_NAME , AIR_TEMPERATURE , WIND_SPEED , PHENOMENON , TIMESTAMP ) VALUES ( 26038, 'Tallinn-Harku', -5.0, 15.0, 'Heavy snowfall', 1679776185);

example curl command to make a request to the delivery api:
curl -X POST -H "Content-Type: application/json" -d '{"city": "tallinn", "vehicle": "scooter"}' http://localhost:8080/api/delivery