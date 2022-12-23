package ru.hogwarts.hogwarts.exception_handling;

public class NoSuchStudentException extends RuntimeException{
    public NoSuchStudentException(String s) {
        super(s);
    }
}
