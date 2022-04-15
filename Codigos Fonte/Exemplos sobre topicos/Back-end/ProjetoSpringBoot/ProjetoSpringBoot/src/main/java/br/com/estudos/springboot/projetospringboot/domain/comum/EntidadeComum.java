package br.com.estudos.springboot.projetospringboot.domain.comum;

import javax.persistence.*;

@MappedSuperclass
public abstract class EntidadeComum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
