package com.springgradlesandbox.springgradlesandox.Controller;

import com.springgradlesandbox.springgradlesandox.DTO.UserDTO;
import com.springgradlesandbox.springgradlesandox.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO request) {
        return new ResponseEntity<>(userService.createUser(request), HttpStatus.OK);
    }
}