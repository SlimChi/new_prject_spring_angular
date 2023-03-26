package com.slim.authentification.controllers;


import com.slim.authentification.dto.AdresseResponse;
import com.slim.authentification.services.AdresseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;


@RestController
@RequestMapping("/adresses")
@RequiredArgsConstructor
@Tag(name = "address")
public class AdresseController {

    private final AdresseService service;
//
//    @PostMapping("/")
//    public ResponseEntity<Integer> save(
//            @RequestBody AdresseResponse addressDto
//    ) {
//        return ResponseEntity.ok(service.save(addressDto));
//    }

    @GetMapping("/findAll")
    public ResponseEntity<List<AdresseResponse>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/findById")
    public ResponseEntity<AdresseResponse> findById(
            @PathVariable("address-id") Integer addressId
    ) {
        return ResponseEntity.ok(service.findById(addressId));
    }

    @DeleteMapping("/{address-id}")
    public ResponseEntity<Void> delete(
            @PathVariable("address-id") Integer addressId
    ) {
        service.delete(addressId);
        return ResponseEntity.accepted().build();
    }

    @PutMapping("/update")
    public ResponseEntity updateAdress(@PathParam("id")Integer id,
                                     @RequestParam String rue,
                                     @RequestParam String complement,
                                     @RequestParam String codePostal,
                                     @RequestParam String ville

                                     ){

        service.updateAdress(id,rue,complement,codePostal,ville);

        return ResponseEntity.ok().build();

    }

}
