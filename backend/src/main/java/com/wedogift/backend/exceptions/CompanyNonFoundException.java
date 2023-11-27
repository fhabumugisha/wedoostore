package com.wedogift.backend.exceptions;

public class CompanyNonFoundException extends  RuntimeException{
    public CompanyNonFoundException(String message){
        super(message)
        ;
    }
}
