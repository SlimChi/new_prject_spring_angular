package com.slim.authentification.services;

import com.slim.authentification.dto.AuthenticationRequest;
import com.slim.authentification.dto.AuthenticationResponse;
import com.slim.authentification.dto.UserDto;

import javax.transaction.Transactional;

/**
 * @author slimane
 * @Project
 */
public interface RegistrationService {
    @Transactional
    AuthenticationResponse registerAdmin(UserDto request);

    @Transactional
    AuthenticationResponse registerUser(UserDto request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
