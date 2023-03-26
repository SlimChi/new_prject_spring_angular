package com.slim.authentification.dto;

import com.slim.authentification.models.Adresse;
import com.slim.authentification.models.TypeAdresse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AdresseDto {


  private Integer id;

  private String rue;

  private String complement;

  private String codePostal;

  private String ville;

  private TypeAdresse typeAdresse;
  private Integer userId;

  public static AdresseDto fromEntity(Adresse adresse) {
    return AdresseDto.builder()
            .id(adresse.getId())
            .rue(adresse.getRue())
            .complement(adresse.getComplement())
            .codePostal(adresse.getCodePostal())
            .ville(adresse.getVille())
            .userId(adresse.getUser())
            .typeAdresse(adresse.getTypeAdresse())
            .build();
  }


  public static Adresse toEntity(AdresseDto adresse) {
    return Adresse.builder()
            .id(adresse.getId())
            .rue(adresse.getRue())
            .complement(adresse.getComplement())
            .codePostal(adresse.getCodePostal())
           .user(adresse.getUserId())
            .typeAdresse(adresse.getTypeAdresse())
            .ville(adresse.getVille())
        .build();
  }

}
