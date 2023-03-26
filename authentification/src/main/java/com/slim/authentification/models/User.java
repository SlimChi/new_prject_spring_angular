package com.slim.authentification.models;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

/**
 * @author  ${SLIMANE}
 * @Project ${AUTHENTIFICATION}
 */

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "_user")
public class User extends AbstractEntity implements UserDetails {

  private String firstName;

  private String lastName;

  @Column(unique = true)
  private String email;

  private String password;

  private boolean active = false;


  @OneToOne
  private Role role;
  @OneToMany(fetch =FetchType.LAZY)
  @JoinColumn(name = "id_user")
  private List<Adresse> adresse;


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority(role.getName()));
  }

  @Override
  public String getUsername() {
    return email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return active;
  }
}
