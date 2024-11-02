package com.gustav.weather_app_javaee.service.weather;

import com.gustav.weather_app_javaee.model.dto.WeatherDTO;
import com.gustav.weather_app_javaee.model.dto.WeatherDataList;
import com.gustav.weather_app_javaee.model.WeatherEntity;
import com.gustav.weather_app_javaee.repo.WeatherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
@AllArgsConstructor
@Service
public class WeatherServiceImpl implements WeatherService {
    private final WeatherRepository weatherRepository;
    private final WeatherApiService weatherApiService;

    @Override
    public List<WeatherEntity> getAllWeather() {
        Optional <WeatherEntity> response = weatherRepository
                .findAll()
                .stream()
                .findAny();

        if(response.isEmpty()){
            System.out.println("WeatherRepository is empty");
        }
        return weatherRepository.findAll();
    }

    @Override
    public WeatherEntity getWeatherByCity(String city) {
        return weatherRepository.getWeatherFromDb(city);
    }

    @Override
    public Optional <WeatherEntity> getWeatherById(Long id) {
      Optional <WeatherEntity> entityByIdOpt = weatherRepository.findById(id);

      entityByIdOpt.ifPresentOrElse(
                weatherEntity -> System.out.println(
                        "Found weather data for id: " + id),
                () -> System.out.println("No weather found with id: " + id)
        );

        return entityByIdOpt;
    }

    @Override
    public boolean deleteWeatherById(Long id) {
        if(weatherRepository.existsById(id)) {
            weatherRepository.deleteById(id);
            return true;
        }

        return false;
    }

    @Override
    public WeatherEntity updateWeather(Long id, WeatherDTO weatherDTO) {
        Optional <WeatherEntity> weatherForUpdateOpt = weatherRepository.findById(id);

        if(weatherForUpdateOpt.isEmpty()) {
            System.out.println("""
            WeatherRepository is empty or no such weather found with the given id
            """);

            return null;
        }

        WeatherEntity weatherForUpdate = weatherForUpdateOpt.get();
        WeatherDataList dataForUpdate = weatherDTO.getData().getFirst();

        if (dataForUpdate == null) {
            System.out.println(" WeatherDataList is empty");
            return null;
        }

        weatherForUpdate.setTemp(dataForUpdate.getTemp());
        weatherForUpdate.setClouds(dataForUpdate.getClouds());
        weatherForUpdate.setDatetime(dataForUpdate.getDatetime());

        return weatherRepository.save(weatherForUpdate);

    }

    @Override
    public WeatherEntity convertToEntity(String cityName, WeatherDTO dto) {
        WeatherDataList data = dto.getData()
                .isEmpty() ? null : dto.getData().getFirst();

        if (data == null) {
            throw new IllegalArgumentException("Weather data is null");
        }

        return WeatherEntity.builder()
                .city_name(cityName.toUpperCase())
                .country_code(dto.getCountryCode())
                .lat(dto.getLat())
                .lon(dto.getLon())
                .stateCode(dto.getStateCode())
                .timezone(dto.getTimezone())
                .temp(data.getTemp())
                .clouds(data.getClouds())
                .maxTemp(data.getMaxTemp())
                .minTemp(data.getMinTemp())
                .weatherDescription(data.getWeather().getDescription())
                .datetime(data.getDatetime())
                .build();
    }


    @Override
    public Mono<WeatherEntity> fetchAndSaveWeatherData(String cityName) {
        return weatherApiService.getWeatherFromExternalApi(cityName)
                .map(weatherDTO -> addWeather(cityName, weatherDTO));
    }

    @Override
    public WeatherEntity addWeather(String cityName, WeatherDTO weather) {
        WeatherEntity convertedData = convertToEntity(cityName,weather);

        return weatherRepository.save(convertedData);
    }

    @Override
    public Optional <Double> getAverageTemperature(String cityName) {
        return weatherRepository.getAverageTemperatureForCity(cityName.toUpperCase());
    }
}
