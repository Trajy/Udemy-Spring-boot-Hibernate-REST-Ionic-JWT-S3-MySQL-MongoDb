package br.com.estudos.springboot.projetospringboot.domain.dto;

import br.com.estudos.springboot.projetospringboot.domain.Cliente;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class ClienteDTO {

    private Integer id;

    @NotEmpty(message = "preenchimento obrigatorio")
    @Length(min = 3, max = 120, message = "o nome deve conter entre {min} e {max} caracteres")
    private String nome;

    @NotEmpty(message = "preenchimento obrigatorio")
    @Email(message = "e-mail invalido")
    private String email;

    public ClienteDTO() {
    }

    public ClienteDTO(Cliente cliente){
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
