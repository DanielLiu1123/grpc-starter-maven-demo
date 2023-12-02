package com.example.server;

import com.example.api.UserApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AppTests {

    @Autowired
    UserApi userApi;

    @Test
    void contextLoads() {
        assertThat(userApi.getById("1"))
                .isEqualTo(new UserApi.UserDTO("1", "Freeman", List.of("Coding", "Reading")));
    }

}
