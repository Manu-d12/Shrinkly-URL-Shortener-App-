package com.url.shortener.controllers;

import com.url.shortener.dtos.UserDto;
import com.url.shortener.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @Valid @RequestBody UserDto userDto
    ) {
        logger.info("USER_DTO: {}", userDto);
        UserDto createdUser = this.userService.createUser(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> findUserById(
            @PathVariable String userId
    ) {
        logger.info("USER_ID: {}", userId);
        UserDto user = this.userService.findUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
