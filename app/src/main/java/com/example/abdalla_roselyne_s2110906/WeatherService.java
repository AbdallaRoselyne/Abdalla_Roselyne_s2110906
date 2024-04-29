package com.example.abdalla_roselyne_s2110906;

//
// Name                Roselyne Abdalla Osundwa
// Student ID           s2110906
// Programme of Study   Computing
//

import android.util.Log;
import android.util.Xml;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class WeatherService {
    public static final String BASE_URL = "https://weather-broker-cdn.api.bbci.co.uk/en/observation/rss/";

    public static HashMap<String, String> locationNames = new HashMap<>();

    static {
        // Initialize location names map
        locationNames.put("2648579", "Glasgow");
        locationNames.put("2643743", "London");
        locationNames.put("5128581", "New York");
        locationNames.put("287286", "Oman");
        locationNames.put("934154", "Mauritius");
        locationNames.put("1185241", "Bangladesh");
    }

    public static String getLocationId(String locationName) {
        for (String locationId : locationNames.keySet()) {
            if (locationNames.get(locationId).equals(locationName)) {
                return locationId;
            }
        }
        return null; // Return null if location name not found
    }

public interface WeatherDataCallback {
        void onSuccess(List<WeatherData> weatherDataList);

        void onFailure(Exception e);
    }

    public void fetchWeatherData(String locationId, final WeatherDataCallback callback) {
        OkHttpClient client = new OkHttpClient();
        String url = BASE_URL + locationId;
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    InputStream inputStream = response.body().byteStream();
                    List<WeatherData> weatherDataList = parseWeatherData(inputStream);
                    callback.onSuccess(weatherDataList);
                } else {
                    callback.onFailure(new IOException("Unexpected code " + response));
                }
            }
        });
    }

    private List<WeatherData> parseWeatherData(InputStream inputStream) {
        List<WeatherData> weatherDataList = new ArrayList<>();
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            WeatherData weatherData = null;
            String locationId = ""; // Initialize location ID variable
            String date = ""; // Initialize date variable
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    String tagName = parser.getName();
                    if (tagName.equals("item")) {
                        weatherData = new WeatherData();
                    } else if (tagName.equals("title")) {
                        String title = parser.nextText();
                        if (weatherData != null) {
                            parseTitle(title, weatherData);
                        }
                    } else if (tagName.equals("description")) {
                        String description = parser.nextText();
                        if (weatherData != null) {
                            parseDescription(description, weatherData);
                            String locationName = locationNames.get(locationId);
                            Log.d("WeatherService", "Location ID: " + locationId);
                            Log.d("WeatherService", "Location Name: " + locationName);
                            if (locationName != null) {
                                weatherData.setLocation(locationName);
                            } else {
                                weatherData.setLocation("Unknown");
                            }
                            weatherData.setDate(date);
                            weatherDataList.add(weatherData);
                            weatherData = null; // Reset WeatherData for the next item
                        }
                    } else if (tagName.equals("location")) {
                        locationId = parser.nextText(); // Set locationId
                    } else if (tagName.equals("pubDate")) {
                        date = parser.nextText(); // Set date
                    }
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return weatherDataList;
    }

    private void parseTitle(String title, WeatherData weatherData) {
        // Example title: "Monday - 18:00 MUT: Light Cloud, 27Â°C (80Â°F)"
        // Split the title by comma to separate day information and temperature
        String[] parts = title.split(",");
        if (parts.length > 1) {
            String dayOfWeek = parts[0].trim();
            weatherData.setDayOfWeek(dayOfWeek);
            // Extract temperature from the second part of the title
            String temperatureStr = parts[1].trim();
            // Use regex to find the temperature part, which should be a number followed by a degree symbol and optionally a unit
            Pattern pattern = Pattern.compile("(\\d+\\.?\\d*)\\s*°\\s*[CF]?");
            Matcher matcher = pattern.matcher(temperatureStr);
            if (matcher.find()) {
                // Extract the temperature value
                String tempValue = matcher.group(1);
                try {
                    double temperature = Double.parseDouble(tempValue);
                    weatherData.setTemperature(temperature);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    weatherData.setTemperature(0.0); // Default value
                }
            } else {
                // Temperature not found or not in expected format
                weatherData.setTemperature(0.0); // Default value
            }
        }
    }

    private void parseDescription(String description, WeatherData weatherData) {
        // Example description: "Temperature: 17Â°C (62Â°F), Wind Direction: South Westerly, Wind Speed: 9mph, Humidity: 68%, Pressure: 1027mb, Steady, Visibility: --"
        // Extract wind speed, humidity, weather condition, and pressure from the description
        weatherData.setWindSpeed(extractValue(description, "Wind Speed"));
        weatherData.setHumidity(extractValue(description, "Humidity"));
        weatherData.setWeatherCondition(extractValue(description, "Weather Condition"));
        weatherData.setPressure(extractValue(description, "Pressure")); // Extract pressure
    }

    private String extractValue(String description, String key) {
        int startIndex = description.indexOf(key);
        if (startIndex != -1) {
            startIndex += key.length() + 1; // Move startIndex to the beginning of the value
            int endIndex = description.indexOf(",", startIndex);
            if (endIndex == -1) {
                endIndex = description.length(); // Use the end of the string if comma not found
            }
            return description.substring(startIndex, endIndex).trim();
        }
        return ""; // Return an empty string if key not found
    }
}
