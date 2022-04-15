package br.com.estudos.springboot.projetospringboot.service.configuracao;

import br.com.estudos.springboot.projetospringboot.domain.comum.EntidadeComum;
import br.com.estudos.springboot.projetospringboot.service.GenericoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class ConfiguracaoService {

    @Bean
    @Scope(value = "prototype")
    public <E extends EntidadeComum> GenericoService<E> genericoServiceBeanBuilder(Class<E> clazz){
        return new GenericoService.GenericoServiceBuilder<>(clazz).build();
    }

}
