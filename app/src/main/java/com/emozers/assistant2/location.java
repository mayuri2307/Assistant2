package com.emozers.assistant2;

public class location {
    private String Latitude;
    private String Longitude;
    location()
    {

    }

    public location(String latitude, String longitude) {
        Latitude = latitude;
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }
}
