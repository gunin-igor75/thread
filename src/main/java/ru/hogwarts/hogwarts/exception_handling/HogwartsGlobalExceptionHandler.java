package ru.hogwarts.hogwarts.exception_handling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HogwartsGlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(HogwartsGlobalExceptionHandler.class);

    @ExceptionHandler
    public ResponseEntity<HogwartsIncorrectData> handlerException(
            NoSuchFacultyException exception) {
        HogwartsIncorrectData data = new HogwartsIncorrectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<HogwartsIncorrectData> handlerException(
            NoSuchStudentException exception) {
        HogwartsIncorrectData data = new HogwartsIncorrectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<HogwartsIncorrectData> handlerException(
            NoSuchAvatarException exception) {
        HogwartsIncorrectData data = new HogwartsIncorrectData();
        data.setInfo(exception.getMessage());
        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler
    public ResponseEntity<HogwartsIncorrectData> handlerException(
            Exception exception) {
        HogwartsIncorrectData data = new HogwartsIncorrectData();
        data.setInfo(exception.getMessage());
        log.error("Error {}", exception.toString());
        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }
}
