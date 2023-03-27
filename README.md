Delivery api to calculate delivery cost based on weather conditions, location and vehicle type.
Api accepts POST requests with json body containing city, vehicle, and optionally you can customize the timestamp, base fee and extra fees based on weather conditions.
By default, the timestamp is set to current time.

JSON body structure:
```
{
    "city": "tallinn",          #(required)
    "vehicle": "scooter",       #(required)
    "RBF": "5",                 #(optional) base fee
    "ATEF": "2",                #(optional) air temperature extra fee
    "WSEF": "1",                #(optional) wind speed extra fee
    "WPEF": "3",                #(optional) weather phenomenon extra fee
    "timestamp": "1679776185"   #(optional) timestamp
}
```

to configure the weather importing cron job go to application.properties and set the cron expression for the property: weather.import.cron 
example: 
```weather.import.cron=0 15 * * * * ``` (ex: every hour at 15 minutes past the hour)

By default the cron job is set to the example above.
