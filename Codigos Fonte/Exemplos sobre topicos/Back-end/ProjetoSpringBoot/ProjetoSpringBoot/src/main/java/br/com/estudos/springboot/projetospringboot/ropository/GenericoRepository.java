package br.com.estudos.springboot.projetospringboot.ropository;

import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;

public class GenericoRepository<E, I> extends SimpleJpaRepository<E, I> {

    private GenericoRepository(JpaEntityInformation<E, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
    }

    private GenericoRepository(Class<E> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
    }

    public static class GenericoRepositoryBuilder<E, I> {

        Class<E> clazz;
        Class<I> identificador;
        EntityManager entityManager;

        public GenericoRepositoryBuilder(Class<E> clazz, Class<I> identificador, EntityManager entityManager) {
            this.clazz = clazz;
            this.identificador = identificador;
            this.entityManager = entityManager;
        }

        public GenericoRepository<E, I> build(){
            return new GenericoRepository<>(clazz, entityManager);
        }
    }

}
