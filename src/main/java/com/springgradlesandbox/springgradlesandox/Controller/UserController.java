package com.springgradlesandbox.springgradlesandox.Controller;

import com.springgradlesandbox.springgradlesandox.DTO.UserDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {

    @PostMapping(value = "/testValidation", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> testValidation(@Valid @RequestBody UserDTO request) {
        return new ResponseEntity<>(request, HttpStatus.OK);
    }
}