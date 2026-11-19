// shared/BusinessException.java
package com.ordermanagement.shared;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) { super(message); }
}