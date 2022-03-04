package br.com.estudos.springboot.projetospringboot.resource.exceptions;

import java.util.ArrayList;
import java.util.List;

public class ValidationError extends StandardError {

    List<FieldMensage> erros = new ArrayList<>();

    public ValidationError(Integer status, String msg, Long timeStamp) {
        super(status, msg, timeStamp);
    }

    public List<FieldMensage> getErros() {
        return erros;
    }

    public void addFieldError(String fieldName, String mensage) {
        erros.add(new FieldMensage(fieldName, mensage));
    }
}
