package com.slim.authentification.controllers;

import com.slim.authentification.models.EmailMessage;
import com.slim.authentification.repositories.EmailSenderRepository;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Slimane
 * @Project ApiMAil
 */

@RestController
public class MailController {

    private final EmailSenderRepository emailSenderRepository;

    public MailController(EmailSenderRepository emailSenderRepository) {
        this.emailSenderRepository = emailSenderRepository;
    }


    @PostMapping("/mail")
    @ApiResponse(responseCode = "400", description = "La syntaxe ou le contenu est invalide.")
    @ApiResponse(responseCode = "401", description = "Vous n'avez pas l'autorisation.")
    @ApiResponse(responseCode = "500", description = "Erreur serveur, veuillez réessayer plus tard.")
    public ResponseEntity emailSending(@RequestBody EmailMessage emailMessage) {

        this.emailSenderRepository.sendEmail(emailMessage.getTo(), emailMessage.getEmail());
        return ResponseEntity.ok("Vous avez envoyez un email à : " + emailMessage.getTo());

    }

}
