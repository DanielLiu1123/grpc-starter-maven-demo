package com.example.server;

import com.example.api.UserApi;
import com.example.api.generated.UserApiBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@SpringBootApplication
@RestController
public class App extends UserApiBase {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public UserApi.UserDTO getById(String id) {
        return new UserApi.UserDTO(id, "Freeman", List.of("Coding", "Reading"));
    }

    @Bean
    ApplicationRunner runner(UserApi userApi) {
        return args -> log.info("{}", userApi.getById("1"));
    }

}
