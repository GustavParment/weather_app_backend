package com.gustav.weather_app_javaee.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "weather_data")
public class WeatherEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column(name = "city_name", nullable = false)
    private String city_name;

    @Column(name = "cloud", nullable = false)
    private int clouds;

    @Column(name = "country_code", nullable = false)
    private String country_code;

    @Column(name = "datetime", nullable = false)
    private String datetime;

    @Column(name = "lat", nullable = false)
    private double lat;

    @Column(name = "lon", nullable = false)
    private double lon;

    @Column(name = "state_code")
    private String stateCode;

    @Column(name = "temp", nullable = false)
    private double temp;

    @Column(name = "timezone", nullable = false)
    private String timezone;

    @Column(name = "max_temp")
    private double maxTemp;

    @Column(name = "min_temp")
    private double minTemp;

    @Column(name = "weather_description")
    private String weatherDescription;

}
