package com.slim.authentification.repositories;

/**
 * @author Slimane
 * @Project ApiEmail
 */
public interface EmailSenderRepository {


    void sendEmail(String to, String email);
}
