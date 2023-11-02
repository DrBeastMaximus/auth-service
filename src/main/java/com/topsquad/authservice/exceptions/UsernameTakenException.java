package com.topsquad.authservice.exceptions;

public class UsernameTakenException extends Exception {
    public UsernameTakenException(String errorMsg){
        super(errorMsg);
    }
}