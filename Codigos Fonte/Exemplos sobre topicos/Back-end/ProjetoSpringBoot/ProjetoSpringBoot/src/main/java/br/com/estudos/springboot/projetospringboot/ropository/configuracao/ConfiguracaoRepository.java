package br.com.estudos.springboot.projetospringboot.ropository.configuracao;

import br.com.estudos.springboot.projetospringboot.domain.comum.EntidadeComum;
import br.com.estudos.springboot.projetospringboot.ropository.GenericoRepository;
import br.com.estudos.springboot.projetospringboot.ropository.GenericoRepository.GenericoRepositoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Configuration
public class ConfiguracaoRepository {

    @Bean
    public EntityManagerFactory entityManagerFactory(){
        return Persistence.createEntityManagerFactory("jpa-unit");
    }

    @Bean
    public EntityManager entityManagerBeanBuilder(EntityManagerFactory entityManagerFactoryBeanBuilder){
        return entityManagerFactoryBeanBuilder.createEntityManager();
    }

    @Bean
    @Scope(value = "prototype")
    public <E extends EntidadeComum, I> GenericoRepository<E, I> clienteRepositoryBeanBuilder(Class<E> entityClazz, Class<I> identificadorClazz){
        return new GenericoRepositoryBuilder<>(entityClazz, identificadorClazz, entityManagerBeanBuilder(entityManagerFactory())).build();
    }
}
