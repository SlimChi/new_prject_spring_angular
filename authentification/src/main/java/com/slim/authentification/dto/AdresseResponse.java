package com.slim.authentification.dto;

import com.slim.authentification.models.Adresse;
import com.slim.authentification.models.TypeAdresse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author slimane
 * @Project
 */

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AdresseResponse {

    private Integer id;

    private String rue;

    private String complement;

    private String codePostal;

    private String ville;

    private TypeAdresse typeAdresse;


    public static AdresseResponse fromEntity(Adresse adresse) {
        return AdresseResponse.builder()
                .id(adresse.getId())
                .rue(adresse.getRue())
                .complement(adresse.getComplement())
                .codePostal(adresse.getCodePostal())
                .ville(adresse.getVille())
                .typeAdresse(adresse.getTypeAdresse())
                .build();
    }
}
