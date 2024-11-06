package com.gustav.weather_app_javaee.service.weather;

import com.gustav.weather_app_javaee.Dao.WeatherDao;
import com.gustav.weather_app_javaee.model.dto.weather.WeatherDTO;
import com.gustav.weather_app_javaee.model.WeatherEntity;
import com.gustav.weather_app_javaee.service.converter.GenericConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
@AllArgsConstructor
@Service
public class WeatherServiceImpl implements WeatherService {
    private final WeatherDao weatherDao;
    private final WeatherApiService weatherApiService;
    private final GenericConverter converter;



    @Override
    public List<WeatherEntity> getAllWeather() {
        return weatherDao.getAllWeather();
    }

    @Override
    public WeatherEntity getWeatherByCity(String city) {
        return weatherDao.getWeatherByCity(city).orElse(null);
    }

    @Override
    public Optional <WeatherEntity> getWeatherById(Long id) {
        return weatherDao.getWeatherById(id);
    }

    @Override
    public boolean deleteWeatherById(Long id) {
        return weatherDao.deleteWeatherById(id);
    }

    @Override
    public WeatherEntity updateWeather(Long id, WeatherDTO weatherDTO) {
        WeatherEntity updatedWeather = converter.convertToWeatherEntity(
                weatherDTO.getCityName(),weatherDTO
        );
      return weatherDao.updateWeather(id,updatedWeather);

    }

    @Override
    public Mono<WeatherEntity> fetchAndSaveWeatherData(String cityName) {
        return weatherApiService.getWeatherFromExternalApi(cityName)
                .map(weatherDTO -> addWeather(cityName, weatherDTO));
    }

    @Override
    public WeatherEntity addWeather(String cityName, WeatherDTO weather) {
        WeatherEntity convertedData = converter.convertToWeatherEntity(cityName,weather);

        return weatherDao.saveWeatherData(convertedData);
    }

    @Override
    public Optional <Double> getAverageTemperature(String cityName) {
        return weatherDao.getAverageTemperature(cityName.toUpperCase());
    }
}
