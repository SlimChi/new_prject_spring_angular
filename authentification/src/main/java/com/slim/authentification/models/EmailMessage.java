package com.slim.authentification.models;

import lombok.Getter;
import lombok.Setter;

/**
 * @author slimane
 * @Project emailApi
 */
@Getter
@Setter
public class EmailMessage {

    private String to;
    private String email;

    public EmailMessage(){

    }

    public EmailMessage(String to, String email) {

        this.to = to;
        this.email = email;
    }
}
