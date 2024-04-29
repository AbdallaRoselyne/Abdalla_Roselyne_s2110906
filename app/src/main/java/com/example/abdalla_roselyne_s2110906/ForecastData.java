package com.example.abdalla_roselyne_s2110906;

//
// Name                Roselyne Abdalla Osundwa
// Student ID           s2110906
// Programme of Study   Computing
//

import android.os.Parcel;
import android.os.Parcelable;

public class ForecastData implements Parcelable {
    private String maxTemperature;
    private String minTemperature;
    private String windDirection;
    private String windSpeed;
    private String forecastDate;
    private String forecastDescription; // New field for the forecast description

    // Constructor that takes Parcel as a parameter
    protected ForecastData(Parcel in) {
        maxTemperature = in.readString();
        minTemperature = in.readString();
        windDirection = in.readString();
        windSpeed = in.readString();
        forecastDate = in.readString();
        forecastDescription = in.readString(); // Read the new field from the Parcel
    }

    // Updated constructor to include the new field
    public ForecastData(String maxTemperature, String minTemperature, String windDirection, String windSpeed, String forecastDate, String forecastDescription) {
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.forecastDate = forecastDate;
        this.forecastDescription = forecastDescription; // Initialize the new field
    }

    // Implement the required methods for Parcelable
    public static final Creator<ForecastData> CREATOR = new Creator<ForecastData>() {
        @Override
        public ForecastData createFromParcel(Parcel in) {
            return new ForecastData(in);
        }

        @Override
        public ForecastData[] newArray(int size) {
            return new ForecastData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(maxTemperature);
        dest.writeString(minTemperature);
        dest.writeString(windDirection);
        dest.writeString(windSpeed);
        dest.writeString(forecastDate);
        dest.writeString(forecastDescription); // Write the new field to the Parcel
    }

    // Getter and setter for the new field
    public String getForecastDescription() {
        return forecastDescription;
    }

    public void setForecastDescription(String forecastDescription) {
        this.forecastDescription = forecastDescription;
    }

    public String getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(String maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public String getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(String minTemperature) {
        this.minTemperature = minTemperature;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getForecastDate() {
        return forecastDate;
    }

    public void setForecastDate(String forecastDate) {
        this.forecastDate = forecastDate;
    }

}
