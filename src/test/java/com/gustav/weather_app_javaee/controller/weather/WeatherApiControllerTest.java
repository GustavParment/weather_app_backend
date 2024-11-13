package com.gustav.weather_app_javaee.controller.weather;

import com.gustav.weather_app_javaee.model.dto.weather.WeatherDTO;
import com.gustav.weather_app_javaee.model.dto.weather.WeatherDataList;
import com.gustav.weather_app_javaee.service.weather.WeatherService;
import com.gustav.weather_app_javaee.controller.weather.WeatherApiController;
import com.gustav.weather_app_javaee.authorities.jwt.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.Mockito.mock;



@SpringBootTest
@AutoConfigureMockMvc
class WeatherApiControllerTest {
    @Mock
    private WebClient webClient;

    @Mock
    private JwtService jwtService;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private WeatherApiController weatherApiController;

    private MockMvc mockMvc;

    @Value("${weather.api.key}")
    private String apiKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void test_Fetch_External_API() throws Exception {
        String city = "Stockholm";
        String jwtToken = "fake-jwt-token";

        WeatherDataList dataList = new WeatherDataList();
        dataList.setTemp(30.3);
        WeatherDTO weatherDTO = new WeatherDTO();
        weatherDTO.setCityName("Stockholm");
        weatherDTO.setData(Collections.singletonList(dataList));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/fetch/{city}", city)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cityName").value("Stockholm"))
                .andExpect(jsonPath("$.countryCode").value("SE"));







    }
    @Test
    @WithMockUser(roles = {"USER"})
    void testFetchWeatherByCity_Unauthorized() throws Exception {
        when(jwtService.extractJwtFromRequest(any())).thenReturn(null);


        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/fetch/{city}", "Stockholm"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }





}