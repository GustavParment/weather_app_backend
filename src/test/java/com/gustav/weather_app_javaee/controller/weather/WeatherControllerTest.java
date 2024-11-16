package com.gustav.weather_app_javaee.controller.weather;

import com.gustav.weather_app_javaee.model.WeatherEntity;
import com.gustav.weather_app_javaee.service.weather.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class WeatherControllerTest {

    @MockBean
    private WeatherService weatherService;

    @Autowired
    private WeatherController  weatherController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(weatherController)
                .build();
    }

    @Test
    void test_Get_All_Weather_From_DB_And_Expect_Not_Found() throws Exception {
        when(weatherService.getAllWeather())
                .thenReturn(
                        Collections.emptyList()
                );

        mockMvc.perform(get("/api/v1/weather/all"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("No weather found"));


        verify(weatherService, times(1)).getAllWeather();

    }

    @Test
    void test_Get_Weather_From_DB_And_Expect_Found() throws Exception {
        WeatherEntity weatherEntity = WeatherEntity
                .builder()
                .Id(1L)
                .city_name("Stockholm")
                .build();

        List<WeatherEntity> weatherList = Collections.singletonList(weatherEntity);

        when(weatherService.getAllWeather()).thenReturn(weatherList);

        mockMvc.perform(get("/api/v1/weather/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$[0].city_name").value("Stockholm"));

    }

    @Test
    void test_Get_Weather_By_Id() throws Exception {
        WeatherEntity mockWeatherEntity = WeatherEntity
                .builder()
                .Id(1L)
                .city_name("Stockholm")
                .build();

        when(weatherService.getWeatherById(1L))
                .thenReturn(
                        Optional.of(mockWeatherEntity)
                );

        mockMvc.perform(get("/api/v1/weather/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city_name").value("Stockholm"));

        verify(weatherService, times(1)).getWeatherById(1L);
    }

    @Test
    void test_Get_Weather_By_Id_And_Expect_Not_Found() throws Exception {

        when(weatherService.getWeatherById(1L)).thenReturn(Optional.empty());

        // Perform the GET request and verify the response
        mockMvc.perform(get("/api/v1/weather/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));

        verify(weatherService, times(1)).getWeatherById(1L);
    }

}