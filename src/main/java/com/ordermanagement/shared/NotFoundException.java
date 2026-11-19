// shared/NotFoundException.java
package com.ordermanagement.shared;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}