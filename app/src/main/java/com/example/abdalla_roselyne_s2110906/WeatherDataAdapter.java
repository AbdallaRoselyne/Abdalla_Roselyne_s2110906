package com.example.abdalla_roselyne_s2110906;

//
// Name                Roselyne Abdalla Osundwa
// Student ID           s2110906
// Programme of Study   Computing
//

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class WeatherDataAdapter extends RecyclerView.Adapter<WeatherDataAdapter.WeatherDataViewHolder> {

    private List<WeatherData> weatherDataList;

    public WeatherDataAdapter(List<WeatherData> weatherDataList) {
        this.weatherDataList = weatherDataList;
    }

    @Override
    public WeatherDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_data_item, parent, false);
        return new WeatherDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeatherDataViewHolder holder, int position) {
        WeatherData weatherData = weatherDataList.get(position);
        // Bind the data to the views in the holder
        holder.locationTextView.setText(weatherData.getLocation());
        holder.dayOfWeekTextView.setText(weatherData.getDayOfWeek());
        holder.temperatureTextView.setText(String.valueOf(weatherData.getTemperature()));
        holder.windSpeedTextView.setText(weatherData.getWindSpeed());
        holder.humidityTextView.setText(weatherData.getHumidity());
        holder.weatherConditionTextView.setText(weatherData.getWeatherCondition());
        // Set the weather icon
        holder.weatherIconImageView.setImageResource(weatherData.getWeatherIcon());
    }

    @Override
    public int getItemCount() {
        return weatherDataList.size();
    }

    public static class WeatherDataViewHolder extends RecyclerView.ViewHolder {
        TextView locationTextView;
        TextView dayOfWeekTextView;
        TextView temperatureTextView;
        TextView windSpeedTextView;
        TextView humidityTextView;
        TextView pressureTextView;
        TextView weatherConditionTextView;
        ImageView weatherIconImageView; // Initialize the ImageView for the weather icon

        public WeatherDataViewHolder(View itemView) {
            super(itemView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            dayOfWeekTextView = itemView.findViewById(R.id.dayOfWeekTextView);
            temperatureTextView = itemView.findViewById(R.id.temperatureTextView);
            windSpeedTextView = itemView.findViewById(R.id.windSpeedTextView);
            humidityTextView = itemView.findViewById(R.id.humidityTextView);
            weatherConditionTextView = itemView.findViewById(R.id.weatherConditionTextView);
            weatherIconImageView = itemView.findViewById(R.id.weatherIconImageView); // Initialize the ImageView
        }
    }

    public void setWeatherDataList(List<WeatherData> weatherDataList) {
        this.weatherDataList = weatherDataList;
        notifyDataSetChanged();
    }
}
