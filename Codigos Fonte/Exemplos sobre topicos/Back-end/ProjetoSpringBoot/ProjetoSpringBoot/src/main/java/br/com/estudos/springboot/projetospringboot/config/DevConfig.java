package br.com.estudos.springboot.projetospringboot.config;

import br.com.estudos.springboot.projetospringboot.service.DataBaseService;
import br.com.estudos.springboot.projetospringboot.service.email.EmailService;
import br.com.estudos.springboot.projetospringboot.service.email.SmtpEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Optional;

@Configuration
@Profile("dev") // annotation para identificar que essa classe so deve ser utilizada ao setar o profile test no arquivo de configuracoes.
public class DevConfig {

    @Autowired
    DataBaseService dataBaseService;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String dataBaseInstanceStrategy;

    @Bean
    public boolean instanciateDataBase(){

        if(!dataBaseInstanceStrategy.equals("create")) return false;

        dataBaseService.instantiateTestDataBase();

        // apenas para retornar algo, os beans nao podem retornar o primitivo void
        return true;
    }

    @Bean
    public EmailService emailService(){
        return new SmtpEmailService();
    }
}


