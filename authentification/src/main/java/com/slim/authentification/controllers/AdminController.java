package com.slim.authentification.controllers;


import com.slim.authentification.dto.AdresseDto;
import com.slim.authentification.dto.AuthenticationResponse;
import com.slim.authentification.dto.UserDto;
import com.slim.authentification.models.Role;
import com.slim.authentification.services.AdresseService;
import com.slim.authentification.services.auth.RegistrationServiceImp;
import com.slim.authentification.services.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "admin")
public class AdminController {

    private final UserServiceImpl service;
    private final RegistrationServiceImp userService;


    @PutMapping("/")
    public ResponseEntity<Integer> save(
            @RequestBody UserDto userDto
    ) {
        return ResponseEntity.ok(service.update(userDto));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerAdmin(
            @RequestBody UserDto user) {

        return ResponseEntity.ok(userService.registerAdmin(user));
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


    @PutMapping("/UpdateAdminById")
    public ResponseEntity updateUser(@PathParam("id")Integer id,
                                     @RequestParam String firstName,
                                     @RequestParam String lastName,
                                     @RequestParam String email,
                                     @RequestParam String password)
    {

        service.updateUser(id,firstName,lastName,email,password);

        return ResponseEntity.ok().build();

    }

}
