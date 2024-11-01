package com.gustav.weather_app_javaee.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDataList {
    @JsonProperty("app_max_temp")
    private double appMaxTemp;

    @JsonProperty("app_min_temp")
    private double appMinTemp;

    @JsonProperty("clouds")
    private int clouds;

    @JsonProperty("datetime")
    private String datetime;

    @JsonProperty("dewpt")
    private double dewpt;

    @JsonProperty("high_temp")
    private double highTemp;

    @JsonProperty("low_temp")
    private double lowTemp;

    @JsonProperty("temp")
    private double temp;

    @JsonProperty("max_temp")
    private double maxTemp;

    @JsonProperty("min_temp")
    private double minTemp;

    @JsonProperty("weather")
    private WeatherDescription weather;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class WeatherDescription {

        @JsonProperty("icon")
        private String icon;

        @JsonProperty("description")
        private String description;

        @JsonProperty("code")
        private int code;
    }
}
