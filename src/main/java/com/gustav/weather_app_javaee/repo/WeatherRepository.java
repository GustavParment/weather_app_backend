package com.gustav.weather_app_javaee.repo;

import com.gustav.weather_app_javaee.model.WeatherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntity, Long> {

    @Query(value = "SELECT AVG(temp) AS average_temp " +
            "FROM weather w WHERE w.city_name = :city_name",
            nativeQuery = true)
    Optional<Double> getAverageTemperatureForCity(@Param("city_name") String cityName);

    @Query(value = "SELECT * FROM weather w " +
            "WHERE w.city_name = :city_name",
            nativeQuery = true)
    WeatherEntity getWeatherByCityName(@Param("city_name") String cityName);

    @Query(value = "SELECT * FROM weather w " +
            "ORDER BY w.city_name ASC, datetime DESC ",
            nativeQuery = true)
    List<WeatherEntity> getWeatherByNameAsc();

    //TODO - Fixa getWeatherByNameAsc() just nu kraschar appen när jag försöker använda den

}