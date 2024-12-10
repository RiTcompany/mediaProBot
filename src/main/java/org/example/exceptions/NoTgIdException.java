package org.example.exceptions;

public class NoTgIdException extends AbstractException {
    public NoTgIdException(String message) {
        super(message, "Что-то пошло не так, пожалуйста обратитесь в поддержку");
    }
}
