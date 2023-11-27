package com.wedogift.backend.exceptions;


public class UserNotFoundException extends  RuntimeException{
    public UserNotFoundException(String message){
        super(message)
        ;
    }
}