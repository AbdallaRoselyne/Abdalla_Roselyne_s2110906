package com.example.abdalla_roselyne_s2110906;

//
// Name                Roselyne Abdalla Osundwa
// Student ID           s2110906
// Programme of Study   Computing
//

import android.os.Parcel;
import android.os.Parcelable;

public class WeatherData implements Parcelable {

    private String dayOfWeek;
    private String location; // Location field
    private String locationId; // Location ID field
    private String date; // Date field
    private double temperature;
    private String windSpeed; // Wind speed
    private String humidity; // Humidity
    private String pressure; // Pressure field
    private String weatherCondition; // Weather condition description
    private int weatherIcon; // Resource ID for the weather icon

    // No-argument constructor required for Parcelable
    public WeatherData() {
    }

    // Adjusted constructor to include locationId
    public WeatherData(String dayOfWeek, String location, String locationId, String date, double temperature, String windSpeed, String humidity, String weatherCondition) {
        this.dayOfWeek = dayOfWeek;
        this.location = location; // Ensure this is set correctly
        this.locationId = locationId;// Initialize locationId
        this.date = date; // Initialize date
        this.temperature = temperature;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.pressure = pressure;
        this.weatherCondition = weatherCondition;
        this.weatherIcon = getWeatherIcon(weatherCondition); // Set the weather icon based on the condition
    }

    protected WeatherData(Parcel in) {
        dayOfWeek = in.readString();
        location = in.readString(); // Read location from Parcel
        locationId = in.readString(); // Read locationId from Parcel
        date = in.readString(); // Read date from Parcel
        temperature = in.readDouble();
        windSpeed = in.readString();
        humidity = in.readString();
        pressure = in.readString();
        weatherCondition = in.readString();
        weatherIcon = in.readInt();
    }

    public static final Creator<WeatherData> CREATOR = new Creator<WeatherData>() {
        @Override
        public WeatherData createFromParcel(Parcel in) {
            return new WeatherData(in);
        }

        @Override
        public WeatherData[] newArray(int size) {
            return new WeatherData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dayOfWeek);
        dest.writeString(location); // Write location to Parcel
        dest.writeString(locationId); // Write locationId to Parcel
        dest.writeString(date); // Write date to Parcel
        dest.writeDouble(temperature);
        dest.writeString(windSpeed);
        dest.writeString(humidity);
        dest.writeString(pressure);
        dest.writeString(weatherCondition);
        dest.writeInt(weatherIcon);
    }

    // Getters and setters for your fields
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }
    public String getPressure() {
        return pressure;
    }
    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }

    public void setWeatherCondition(String weatherCondition) {
        this.weatherCondition = weatherCondition;
        this.weatherIcon = getWeatherIcon(weatherCondition); // Update the weather icon when the condition changes
    }

    public int getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(int weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    // Method to map weather condition to a specific icon
    private int getWeatherIcon(String weatherCondition) {
        switch (weatherCondition) {
            case "Sun":
                return R.drawable.sunny;
            case "Light cloud":
                return R.drawable.cloudy;
            case "Rain":
                return R.drawable.rainy;
            case "Snow":
                return R.drawable.snowy;
            // Add more cases as needed
            default:
                return R.drawable.cloudy_sunny; // Default icon
        }
    }
}
