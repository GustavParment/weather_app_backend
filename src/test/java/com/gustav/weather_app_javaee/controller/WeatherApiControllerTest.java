package com.gustav.weather_app_javaee.controller;

import com.gustav.weather_app_javaee.dto.WeatherDTO;
import com.gustav.weather_app_javaee.dto.WeatherDataList;
import com.gustav.weather_app_javaee.model.WeatherEntity;
import com.gustav.weather_app_javaee.repo.WeatherRepository;
import com.gustav.weather_app_javaee.service.WeatherApiService;
import com.gustav.weather_app_javaee.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class WeatherApiControllerTest {

    @Mock
    private WeatherRepository weatherRepository;

    @Mock
    private WeatherApiService weatherApiService;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherApiController weatherApiController;

    private WeatherDTO mockWeatherDTO;

    private WeatherDataList mockWeatherData;


    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        mockWeatherDTO = mockValueDto();

    }
    private static WeatherDTO mockValueDto() {
        WeatherDTO mockWeatherDTO = new WeatherDTO();
        mockWeatherDTO.setCityName("Stockholm");
        mockWeatherDTO.setCountryCode("SE");
        mockWeatherDTO.setLat(59.3293);
        mockWeatherDTO.setLon(18.0686);
        mockWeatherDTO.setStateCode("ST");
        mockWeatherDTO.setTimezone("Europe/Stockholm");

        WeatherDataList mockWeatherDataList = new WeatherDataList();
        mockWeatherDataList.setTemp(15.0);
        mockWeatherDataList.setClouds(50);
        mockWeatherDataList.setDatetime("2024-10-31 10:00:00");
        mockWeatherDataList.setMaxTemp(1337);
        mockWeatherDataList.setMinTemp(1337);
        mockWeatherDTO.setData(Collections.singletonList(mockWeatherDataList));


        WeatherDataList.WeatherDescription mockWeatherDescription =
                new WeatherDataList.WeatherDescription();

        mockWeatherDescription.setDescription("Some description");
        mockWeatherDescription.setIcon("icon.jpg");
        mockWeatherDescription.setCode(123455678);
        mockWeatherDataList.setWeather(mockWeatherDescription);

        return mockWeatherDTO;
    }


    /* Testa att metoden fetchWeatherByName i WeatherApiController
       korrekt hämtar väderinformation för en stad och returnerar
       den i form av en ResponseEntity med status OK.*/
    @Test
    void fetchWeatherByName_ShouldReturnWeatherDTO_WhenCityExists() {
        Mockito.when(weatherApiService.getWeatherFromExternalApi(anyString()))
                .thenReturn(Mono.just(mockValueDto()));

        Mono<ResponseEntity<WeatherDTO>> response =
                weatherApiController.fetchWeatherByName("Stockholm");

        response.subscribe(res -> {
            assertEquals(HttpStatus.OK, res.getStatusCode());
            assertNotNull(res.getBody());
            assertEquals("Stockholm", res.getBody().getCityName());
        }, error -> {
            fail("Testet misslyckades på grund av ett oväntat fel: " + error.getMessage());
        });
    }

    // Testa att DTO konverteras till entitet och att den skrivs till databasen

    @Test
    void addWeather_ShouldConvertDtoToEntity_And_Save_To_DB() {
        // Arrange
        WeatherEntity savedEntity = new WeatherEntity();
        savedEntity.setId(1337L);
        savedEntity.setCity_name(mockValueDto().getCityName());
        savedEntity.setCountry_code(mockValueDto().getCountryCode());
        savedEntity.setLat(mockValueDto().getLat());
        savedEntity.setLon(mockValueDto().getLon());
        savedEntity.setStateCode(mockValueDto().getStateCode());
        savedEntity.setTimezone(mockValueDto().getTimezone());
        savedEntity.setTemp(mockValueDto().getData().getFirst().getTemp());
        savedEntity.setClouds(mockValueDto().getData().getFirst().getClouds());
        savedEntity.setDatetime(mockValueDto().getData().getFirst().getDatetime());
        savedEntity.setMaxTemp(mockValueDto().getData().getFirst().getMaxTemp());
        savedEntity.setMinTemp(mockValueDto().getData().getFirst().getMinTemp());


        // Mocka repo-metoden
        when(weatherRepository.save(any(WeatherEntity.class))).thenReturn(savedEntity);

        WeatherEntity result = weatherService.addWeather(
                savedEntity.getCity_name(),mockWeatherDTO
        );

        assertNotNull(result);
        assertEquals(savedEntity.getCity_name(), result.getCity_name());
        assertEquals(savedEntity.getCountry_code(), result.getCountry_code());

    }
}






