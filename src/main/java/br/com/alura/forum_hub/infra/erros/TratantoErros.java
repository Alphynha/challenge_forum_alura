package br.com.alura.forum_hub.infra.erros;

import jakarta.persistence.EntityNotFoundException;
import jakarta.xml.bind.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class TratantoErros {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<List<?>> error400Handler(MethodArgumentNotValidException exception){
        var erro = exception.getFieldErrors().stream().map(validacionErroresDatos::new).toList();
        return ResponseEntity.badRequest().body(erro);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> error404Handler(){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IntegrityValidation.class)
    public ResponseEntity<String> errorHandlerIntegrityValidation(Exception exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> errorHandlerBusinessValidation(Exception exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> invalidBodyHandler(Exception exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }

    private record validacionErroresDatos(String field, String message){
        public validacionErroresDatos(FieldError error){
            this(error.getField(), error.getDefaultMessage());
        }
    }
}
