package com.slim.authentification.repositories;

import java.util.Optional;

import com.slim.authentification.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;


public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);

  @Transactional
  @Modifying
  @Query("UPDATE User a " +
          "SET a.active = TRUE WHERE a.email = ?1")
  int enableAppUser(String email);
}
