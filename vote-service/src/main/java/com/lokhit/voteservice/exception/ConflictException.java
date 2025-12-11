package com.lokhit.voteservice.exception;

public class ConflictException extends RuntimeException{

    public ConflictException(String message){
        super(message);
    }
}
