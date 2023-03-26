package com.slim.authentification.services;


import com.slim.authentification.dto.AdresseDto;
import com.slim.authentification.dto.AdresseResponse;

import javax.transaction.Transactional;

/**
 * @author slimane
 * @Project auth
 */
public interface AdresseService extends AbstractService<AdresseResponse>{


    Integer save(AdresseDto dto);

    @Transactional
    void addAdresseToUser(AdresseDto dto);

    @Transactional
    void updateAdress(Integer id, String rue, String complement, String codePostal, String ville);
}
