package com.slim.authentification.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

/**
 * @author
 * @Project
 */


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "adresse")
public class Adresse  {

    @Id
    @GeneratedValue
    private Integer id;
    private String rue;

    private String complement;

    private String codePostal;

    private String ville;

        @ManyToOne
        @JoinColumn(name = "id_type_adresse")
        private TypeAdresse typeAdresse;


    @Basic
    @Column(name = "id_user")
    private int user;

}
