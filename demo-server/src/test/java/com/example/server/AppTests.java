package com.example.server;

import com.example.api.UserApi;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.example.api.UserApi.UserDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@SpringBootTest(webEnvironment = DEFINED_PORT)
class AppTests {

    @Autowired
    UserApi userApi;

    @Test
    void contextLoads() {
        assertThat(userApi).isNotInstanceOf(App.class);

        assertThat(userApi.getById("1"))
                .isEqualTo(new UserDTO("1", "Freeman", List.of("Coding", "Reading")));
    }

}
