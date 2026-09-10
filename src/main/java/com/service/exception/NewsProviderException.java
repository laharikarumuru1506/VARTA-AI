package com.service.exception;

@SuppressWarnings("serial")
public class NewsProviderException extends RuntimeException{
	
	public NewsProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
