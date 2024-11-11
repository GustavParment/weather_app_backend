package com.gustav.weather_app_javaee.service.weather;

import com.gustav.weather_app_javaee.Dao.WeatherDao;
import com.gustav.weather_app_javaee.model.dto.weather.WeatherDTO;
import com.gustav.weather_app_javaee.model.WeatherEntity;
import com.gustav.weather_app_javaee.service.converter.GenericConverter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
@AllArgsConstructor
@Service
public class WeatherServiceImpl implements WeatherService {
    private final WeatherDao weatherDao;

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
    public WeatherEntity saveWeatherData(String cityName, WeatherDTO weatherDTO) {
        WeatherEntity weatherEntity = converter.convertToWeatherEntity(cityName, weatherDTO);


        return weatherDao.saveWeatherData(weatherEntity);
    }


    @Override
    public Optional <Double> getAverageTemperature(String cityName) {
        return weatherDao.getAverageTemperature(cityName.toUpperCase());
    }
}
