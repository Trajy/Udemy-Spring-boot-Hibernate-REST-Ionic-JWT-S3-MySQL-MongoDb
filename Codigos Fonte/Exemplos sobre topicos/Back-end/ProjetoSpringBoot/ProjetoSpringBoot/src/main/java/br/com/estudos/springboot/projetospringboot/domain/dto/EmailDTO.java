package br.com.estudos.springboot.projetospringboot.domain.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class EmailDTO {

    @NotEmpty(message = "preenchimento obrigatorio")
    @Email(message = "e-mail invalido")
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
