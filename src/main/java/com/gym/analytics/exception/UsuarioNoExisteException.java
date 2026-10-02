package com.gym.analytics.exception;

public class UsuarioNoExisteException extends RuntimeException {

    public UsuarioNoExisteException(String message){
        super(message);
    }

}