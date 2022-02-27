package br.com.estudos.springboot.projetospringboot.domain.dto;

import br.com.estudos.springboot.projetospringboot.domain.Categoria;

public class CategoriaDTO {

    Integer id;

    String nome;

    CategoriaDTO(){

    }

    public CategoriaDTO(Categoria categoria) {
        this.id = categoria.getId();
        this.nome = categoria.getNome();
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
}
