package com.example.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

/**
 * @author Freeman
 */
@Validated
@HttpExchange("/user")
public interface UserApi {
    record UserDTO(String id, String name, List<String> hobbies) {
    }

    @GetExchange("/getById/{id}")
    UserDTO getById(@PathVariable("id") @NotBlank @Size(max = 5) String id);

    @GetExchange("/getByName/{name}")
    UserDTO getByName(@PathVariable("name") @NotBlank String name);
}
