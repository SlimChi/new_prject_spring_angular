package com.slim.authentification.controllers;

import com.slim.authentification.dto.AuthenticationRequest;
import com.slim.authentification.dto.AuthenticationResponse;
import com.slim.authentification.dto.UserDto;
import com.slim.authentification.services.UserService;
import com.slim.authentification.services.auth.RegistrationServiceImp;
import com.slim.authentification.services.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "authentication")
public class AuthenticationController {

  private final RegistrationServiceImp userService;

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
      @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(userService.authenticate(request));
  }

  @GetMapping(path = "confirm")
  public String confirm(@RequestParam("token") String token) {
    return userService.confirmToken(token);
  }

}
