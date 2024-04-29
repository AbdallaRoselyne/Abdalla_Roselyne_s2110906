package com.example.abdalla_roselyne_s2110906;

//
// Name                Roselyne Abdalla Osundwa
// Student ID           s2110906
// Programme of Study   Computing
//

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForecastFetcher {

    private static final String TAG = "ForecastFetcher";
    private static final String RSS_FEED_URL = "https://weather-broker-cdn.api.bbci.co.uk/en/forecast/rss/3day/";

    public InputStream fetchForecastDataAsStream(String locationId) {
        InputStream inputStream = null;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(RSS_FEED_URL + locationId)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                inputStream = response.body().byteStream();
            } else {
                Log.e(TAG, "Failed to fetch forecast data: HTTP " + response.code());
                // Return a default or empty InputStream to prevent the IllegalArgumentException
                return new ByteArrayInputStream(new byte[0]);
            }
        } catch (IOException e) {
            Log.e(TAG, "Error fetching forecast data", e);
        }
        return inputStream;
    }

    public List<ForecastData> parseForecastData(InputStream inputStream) {
        if (inputStream == null) {
            Log.e(TAG, "InputStream is null, cannot parse forecast data");
            return new ArrayList<>();
        }

        List<ForecastData> forecastDataList = new ArrayList<>();
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            ForecastData forecastData = null;
            String forecastDay = "";
            String forecastDescription = "";

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tagName = parser.getName();
                    if (tagName.equals("item")) {
                        // Reset forecastData and forecastDay for each new item
                        forecastData = new ForecastData(null, null, null, null, null, null);
                        forecastDay = "";
                        forecastDescription = "";
                    } else if (forecastData != null) {
                        if (tagName.equals("title")) {
                            String title = parser.nextText();
                            // Correctly parse the day and forecast description from the title
                            String[] parts = title.split(":");
                            if (parts.length > 0) {
                                forecastDay = parts[0].trim(); // Ensure this is the day
                            }
                            if (parts.length > 1) {
                                // Split the description part by commas and take the first part as the description
                                String[] descriptionParts = parts[1].trim().split(",");
                                if (descriptionParts.length > 0) {
                                    forecastDescription = descriptionParts[0].trim();
                                }
                            }
                        } else if (tagName.equals("description")) {
                            String description = parser.nextText();
                            String[] parts = description.split(",");
                            for (String part : parts) {
                                if (part.contains("Maximum Temperature")) {
                                    forecastData.setMaxTemperature(extractValue(part));
                                } else if (part.contains("Minimum Temperature")) {
                                    forecastData.setMinTemperature(extractValue(part));
                                } else if (part.contains("Wind Direction")) {
                                    forecastData.setWindDirection(extractValue(part));
                                } else if (part.contains("Wind Speed")) {
                                    forecastData.setWindSpeed(extractValue(part));
                                }
                            }
                            forecastData.setForecastDescription(forecastDescription);
                            forecastData.setForecastDate(forecastDay);
                            forecastDataList.add(forecastData);
                            // Reset forecastData to null to prevent adding the same object multiple times
                            forecastData = null;
                        }
                    }
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "Error parsing forecast data", e);
        }
        // Do not close the stream here. It will be closed by the caller.
        return forecastDataList;
    }

    private String extractValue(String descriptionPart) {
        String[] keyValue = descriptionPart.split(":");
        if (keyValue.length == 2) {
            String value = keyValue[1].trim();
            // Check if the value contains both direction and speed, and if so, split them correctly
            if (value.contains("mph") || value.contains("km/h")) {
                // Split by the last occurrence of "mph" or "km/h" to separate direction and speed
                String[] parts = value.split("(?<=mph|km/h)");
                if (parts.length > 1) {
                    // Combine the direction and speed parts, removing any extra spaces
                    return parts[0].trim() + " " + parts[1].trim();
                }
            }
            return value;
        }
        return "";
    }
}
