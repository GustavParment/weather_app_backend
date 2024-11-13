package com.gustav.weather_app_javaee.model.dto.user;


import lombok.Data;

/*TODO
   -Sätta Annotationer sätta begränsnings annotationer
   -Skriva tester?
* */
@Data
public class RegisterUserDto {
    private String email;
    private String password;
    private String fullName;


}