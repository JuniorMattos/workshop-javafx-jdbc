/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.exceptions;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Junior
 */
public class ValidationException extends RuntimeException{
    
    private static final long serialVersionUID = 1L;
    
    private Map<String, String> errors = new HashMap<>();

    public ValidationException(String msg) {
        super(msg);
    }
    
    public Map<String, String> getErrors(){
        return errors;
    }

    public void addError(String fieldName, String errorMessage){
        errors.put(fieldName, errorMessage);
    } 
}
