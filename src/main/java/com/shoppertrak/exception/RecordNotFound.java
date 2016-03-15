package com.shoppertrak.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value=HttpStatus.NOT_FOUND)
public class RecordNotFound extends RuntimeException {
	
	private static final long serialVersionUID = 1L;	
	private static final String MESSAGE = "Record is not found for ID %d";
	
	public RecordNotFound(int id) {
        super(String.format(MESSAGE, id));
    }
}
