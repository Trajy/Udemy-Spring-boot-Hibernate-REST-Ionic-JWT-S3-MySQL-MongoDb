package br.com.estudos.springboot.projetospringboot.config;

import br.com.estudos.springboot.projetospringboot.service.DataBaseService;
import br.com.estudos.springboot.projetospringboot.service.email.EmailService;
import br.com.estudos.springboot.projetospringboot.service.email.MockEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Optional;

@Configuration
@Profile("test") // annotation para identificar que essa classe so deve ser utilizada ao setar o profile test no arquivo de configuracoes.
public class TestConfig {

    @Autowired
    DataBaseService dataBaseService;

    @Bean
    public Optional<?> instanciateDataBase(){

        dataBaseService.instantiateTestDataBase();

        // apenas para retornar algo, os beans nao podem retornar o primitivo void
        Optional<?> voyd = Optional.ofNullable(Void.TYPE);
        return voyd;
    }

    @Bean
    public EmailService emailService(){
        return new MockEmailService();
    }
}


