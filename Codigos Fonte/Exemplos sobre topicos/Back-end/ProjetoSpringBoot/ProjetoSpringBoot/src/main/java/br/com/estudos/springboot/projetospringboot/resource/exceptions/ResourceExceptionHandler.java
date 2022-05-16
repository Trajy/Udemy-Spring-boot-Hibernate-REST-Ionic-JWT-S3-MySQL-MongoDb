package br.com.estudos.springboot.projetospringboot.resource.exceptions;

import br.com.estudos.springboot.projetospringboot.service.exceptions.AuthorizationException;
import br.com.estudos.springboot.projetospringboot.service.exceptions.DataIntegrityException;
import br.com.estudos.springboot.projetospringboot.service.exceptions.FileException;
import br.com.estudos.springboot.projetospringboot.service.exceptions.ObjectNotFoundException;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {
        StandardError erro = new StandardError(HttpStatus.NOT_FOUND.value(), e.getMessage(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<StandardError> dataIntegity(DataIntegrityException e, HttpServletRequest request){
        StandardError erro = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> validationError(MethodArgumentNotValidException e, HttpServletRequest request){
        ValidationError erro = new ValidationError(HttpStatus.BAD_REQUEST.value(), "Erro de validacao", System.currentTimeMillis());
        for(FieldError x : e.getBindingResult().getFieldErrors()){
            erro.addFieldError(x.getField(), x.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<StandardError> validationUser(FileException e, HttpServletRequest request){
        StandardError erro = new StandardError(HttpStatus.FORBIDDEN.value(), e.getMessage(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(erro);
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<StandardError> fileException(FileException e, HttpServletRequest request){
        StandardError erro = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(AmazonServiceException.class)
    public ResponseEntity<StandardError> amazonServiceException(AmazonServiceException e, HttpServletRequest request){
        HttpStatus code = HttpStatus.valueOf(e.getStatusCode());
        StandardError erro = new StandardError(code.value(), e.getMessage(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(AmazonClientException.class)
    public ResponseEntity<StandardError> amazonServiceException(AmazonClientException e, HttpServletRequest request){
        StandardError erro = new StandardError(HttpStatus.BAD_REQUEST.value(), e.getMessage(), System.currentTimeMillis());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
}
