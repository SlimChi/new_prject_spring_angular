package com.slim.authentification;

import com.slim.authentification.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class AuthentificationApplication {

  @Autowired
  UserService userService;

  public static void main(String[] args) {
    SpringApplication.run(AuthentificationApplication.class, args);
  }

  void init_users(){
    //ajouter les rôles

  }

}
