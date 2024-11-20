package com.gustav.weather_app_javaee.Dao.weather;

import com.gustav.weather_app_javaee.model.WeatherEntity;
import com.gustav.weather_app_javaee.repo.WeatherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
@AllArgsConstructor
public class WeatherDaoImpl implements WeatherDao {
    private WeatherRepository weatherRepository;

    @Override
    public List<WeatherEntity> getAllWeather() {
        return weatherRepository.findAll();
    }

    @Override
    public Optional<WeatherEntity> getWeatherByCity(String city) {
        return Optional
                .ofNullable(
                        weatherRepository.getWeatherByCityName(city)
                );
    }

    @Override
    public Optional<WeatherEntity> getWeatherById(Long id) {
        return weatherRepository.findById(id);
    }

    @Override
    public boolean deleteWeatherById(Long id) {
        if (weatherRepository.existsById(id)){
            weatherRepository.deleteById(id);

            return true;
        }
        return false;
    }

    @Override
    public WeatherEntity updateWeather(Long id, WeatherEntity updatedWeather) {
        return weatherRepository.findById(id)
                .map(existingWeather -> {
                    existingWeather.setTemp(updatedWeather.getTemp());
                    existingWeather.setClouds(updatedWeather.getClouds());
                    existingWeather.setDatetime(updatedWeather.getDatetime());
                    return weatherRepository.save(existingWeather);
                })
                .orElse(null);
    }

    @Override
    public Optional<Double> getAverageTemperature(String cityName) {
        return weatherRepository.getAverageTemperatureForCity(cityName);
    }

    @Override
    public WeatherEntity saveWeatherData(WeatherEntity weatherEntity) {
        return weatherRepository.save(weatherEntity);
    }
}
