package br.com.estudos.springboot.projetospringboot.domain.dto;

import br.com.estudos.springboot.projetospringboot.service.validacao.ClienteNovoDTOInsertValidation;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@ClienteNovoDTOInsertValidation
public class ClienteNovoDTO {

    @NotEmpty(message = "preenchimento obrigatorio")
    @Length(min = 3, max = 120, message = "o nome deve conter entre {min} e {max} caracteres")
    private String nome;

    @NotEmpty(message = "preenchimento obrigatorio")
    @Email(message = "e-mail invalido")
    private String email;

    @NotEmpty(message = "preenchimento obrigatorio")
    private String cpfOuCnpj;

    private Integer tipo;

    // relacionamentos entidade Endereco
    @NotEmpty(message = "preenchimento obrigatorio")
    private String logradouro;

    @NotEmpty(message = "preenchimento obrigatorio")
    private String numero;

    private String complemento;

    private String bairro;

    @NotEmpty(message = "preenchimento obrigatorio")
    private String cep;

    // relacionamento entidade Telefone
    @NotEmpty(message = "preenchimento obrigatorio")
    private String telefone;

    // relacionamento Entidades Cidade e Endereco
    private Integer cidadeId;

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

    public String getCpfOuCnpj() {
        return cpfOuCnpj;
    }

    public void setCpfOuCnpj(String cpfOuCnpj) {
        this.cpfOuCnpj = cpfOuCnpj;
    }

    public Integer getTipo() {
        return tipo;
    }

    public void setTipo(Integer tipo) {
        this.tipo = tipo;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Integer getCidadeId() {
        return cidadeId;
    }

    public void setCidadeId(Integer cidadeId) {
        this.cidadeId = cidadeId;
    }
}
