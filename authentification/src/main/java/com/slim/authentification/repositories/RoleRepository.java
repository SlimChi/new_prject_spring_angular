package com.slim.authentification.repositories;


import java.util.Optional;

import com.slim.authentification.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Integer> {

  Optional<Role> findByName(String roleName);
}
