package com.slim.authentification.services;


import com.slim.authentification.dto.UserDto;
import com.slim.authentification.models.Role;

import javax.transaction.Transactional;

public interface UserService extends AbstractService<UserDto> {


  Integer update(UserDto userDto);


  @Transactional
  void updateUser(Integer id, String firstName, String lastName, String email, String password);

}
