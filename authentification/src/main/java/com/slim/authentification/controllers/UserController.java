package com.slim.authentification.controllers;


import com.slim.authentification.dto.AdresseDto;
import com.slim.authentification.dto.AuthenticationResponse;
import com.slim.authentification.dto.UserDto;
import com.slim.authentification.services.AdresseService;
import com.slim.authentification.services.UserService;
import com.slim.authentification.services.auth.RegistrationServiceImp;
import com.slim.authentification.services.impl.UserServiceImpl;
import com.slim.authentification.services.token.ConfirmationTokenService;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "user")
public class UserController {

    private final UserServiceImpl service;
    private final RegistrationServiceImp userService;
    private final AdresseService adresseService;


    @PostMapping("/")
    public ResponseEntity<Integer> save(
            @RequestBody UserDto userDto
    ) {
        return ResponseEntity.ok(service.update(userDto));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(
            @RequestBody UserDto user) {

        return ResponseEntity.ok(userService.registerUser(user));
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{user-id}")
    public ResponseEntity<UserDto> findById(
            @PathVariable("user-id") Integer userId
    ) {
        return ResponseEntity.ok(service.findById(userId));
    }

    @DeleteMapping("/{user-id}")
    public ResponseEntity<Void> delete(
            @PathVariable("user-id") Integer userId
    ) {
        service.delete(userId);
        return ResponseEntity.accepted().build();
    }


    @PostMapping("/{id}/addAdresseToUser")
    public ResponseEntity addAdresseToUser(@RequestBody AdresseDto adresse) {

        adresseService.addAdresseToUser(adresse);
        return ResponseEntity.ok().build();

    }

    @PutMapping("/UpdateUserById")
    public ResponseEntity updateUser(@PathParam("id")Integer id,
                                     @RequestParam String firstName,
                                     @RequestParam String lastName,
                                     @RequestParam String email,
                                     @RequestParam String password){

        service.updateUser(id,firstName,lastName,email,password);

        return ResponseEntity.ok().build();

    }

}
