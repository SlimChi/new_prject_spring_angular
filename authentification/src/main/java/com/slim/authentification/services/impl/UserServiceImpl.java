package com.slim.authentification.services.impl;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import com.slim.authentification.dto.UserDto;
import com.slim.authentification.models.Role;
import com.slim.authentification.models.User;
import com.slim.authentification.repositories.RoleRepository;
import com.slim.authentification.repositories.UserRepository;
import com.slim.authentification.services.UserService;
import com.slim.authentification.validators.ObjectsValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository repository;
    private final ObjectsValidator<UserDto> validator;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public Integer save(UserDto dto) {
//        validator.validate(dto);
        User user = UserDto.toEntity(dto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user).getId();
    }


    @Override
    @Transactional
    public List<UserDto> findAll() {
        return repository.findAll()
                .stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Integer id) {
        return repository.findById(id)
                .map(UserDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("No user was found with the provided ID : " + id));
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
    }



    @Override
    public Integer update(UserDto userDto) {
        User user = UserDto.toEntity(userDto);
        return repository.save(user).getId();
    }

    @Override
    @Transactional
    public void updateUser(Integer id, String firstName, String lastName, String email, String password){

        User updateUser = userRepository.findById(id).orElseThrow();

        updateUser.setId(id);
        updateUser.setEmail(email);
        updateUser.setFirstName(firstName);
        updateUser.setLastName(lastName);
        updateUser.setPassword(passwordEncoder.encode(password));

    }


    public int enableAppUser(String email) {
        return userRepository.enableAppUser(email);

    }



}
