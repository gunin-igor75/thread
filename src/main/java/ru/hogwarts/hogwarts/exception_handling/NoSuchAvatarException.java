package ru.hogwarts.hogwarts.exception_handling;

public class NoSuchAvatarException extends RuntimeException{
    public NoSuchAvatarException(String message) {
        super(message);
    }
}
