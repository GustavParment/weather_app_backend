package com.gustav.weather_app_javaee.controller;

import com.gustav.weather_app_javaee.Dao.WeatherDao;
import com.gustav.weather_app_javaee.controller.weather.WeatherApiController;
import com.gustav.weather_app_javaee.model.dto.weather.WeatherDTO;
import com.gustav.weather_app_javaee.model.dto.weather.WeatherDataList;
import com.gustav.weather_app_javaee.model.WeatherEntity;
import com.gustav.weather_app_javaee.service.converter.GenericConverter;
import com.gustav.weather_app_javaee.service.weather.WeatherApiService;
import com.gustav.weather_app_javaee.service.weather.WeatherService;
import com.gustav.weather_app_javaee.service.weather.WeatherServiceImpl;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


class WeatherApiControllerTest {

    @Mock
    private GenericConverter converter;

    @Mock
    private WeatherDao weatherDao;

    @Mock
    private WeatherApiService weatherApiService;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherApiController weatherApiController;

    private WeatherDTO mockWeatherDTO;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        weatherService = new WeatherServiceImpl(weatherDao, weatherApiService, converter);
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
    void fetch_Weather_By_Name_Should_Return_Weather_DTO_WhenCityExists() {
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
    void addWeather_Should_Convert_Dto_To_Entity_And_Save_To_DB() {
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

        when(weatherDao.saveWeatherData(any(WeatherEntity.class))).thenReturn(savedEntity);

        WeatherEntity result = weatherDao.saveWeatherData(savedEntity);

        assertNotNull(result);
        assertEquals(savedEntity.getCity_name(), result.getCity_name());
        assertEquals(savedEntity.getCountry_code(), result.getCountry_code());

    }

    @Test
    void update_Weather_Should_Update_Entity_And_Save_Update_To_DB() {
        Long mockId = 1337L;
        WeatherEntity existingEntity = new WeatherEntity();

        existingEntity.setId(mockId);
        existingEntity.setCity_name("Stockholm");
        existingEntity.setCountry_code("SE");
        existingEntity.setTemp(10);
        existingEntity.setClouds(50);
        existingEntity.setDatetime("2024-10-31");

        when(weatherDao.getWeatherById(mockId)).thenReturn(Optional.of(existingEntity));

        WeatherDataList mockDataForUpdate = new WeatherDataList();

        mockDataForUpdate.setTemp(15.0);
        mockDataForUpdate.setClouds(69);
        mockDataForUpdate.setDatetime("2024-11-01");

        mockValueDto().setData(Collections.singletonList(mockDataForUpdate));

        when(weatherApiService.getWeatherFromExternalApi("Stockholm"))
                .thenReturn(Mono.just(mockValueDto()));

        Mono<ResponseEntity<WeatherEntity>> response = weatherApiController
                .updateWeather(mockId, "Stockholm");

        response.subscribe(res ->{
            assertEquals(HttpStatus.OK, res.getStatusCode());
            assertNotNull(res.getBody());
            assertEquals(15.0, res.getBody().getTemp());
            assertEquals(69, res.getBody().getClouds());
            assertEquals("2024-11-01", res.getBody().getDatetime());
        }, error -> {
            fail("Testat misslyckades på grund av att du e sämst på att skriva tester"
                    + error.getMessage());
        });
    }
}






