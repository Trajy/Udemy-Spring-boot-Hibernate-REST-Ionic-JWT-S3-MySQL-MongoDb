package br.com.estudos.springboot.projetospringboot.ropository.configuracao;

import br.com.estudos.springboot.projetospringboot.domain.comum.EntidadeComum;
import br.com.estudos.springboot.projetospringboot.ropository.GenericoRepository;
import br.com.estudos.springboot.projetospringboot.ropository.GenericoRepository.GenericoRepositoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;

import javax.persistence.EntityManagerFactory;

@Configuration
public class ConfiguracaoRepository {

    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Bean
    @Scope(value = "prototype")
    public <E extends EntidadeComum, I> GenericoRepository<E, I> genericoRepositoryBeanBuilder(Class<E> entityClazz, Class<I> identificadorClazz){
        return new GenericoRepositoryBuilder<>(entityClazz, identificadorClazz, entityManagerFactory.createEntityManager()).build();
    }
}
