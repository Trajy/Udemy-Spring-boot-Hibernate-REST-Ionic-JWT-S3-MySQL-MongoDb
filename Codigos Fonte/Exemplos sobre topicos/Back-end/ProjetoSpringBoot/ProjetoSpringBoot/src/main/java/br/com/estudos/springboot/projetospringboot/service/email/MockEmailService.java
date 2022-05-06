package br.com.estudos.springboot.projetospringboot.service.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

public class MockEmailService extends AbstractEmailService {

    private static final Logger log = LoggerFactory.getLogger(MockEmailService.class);

    @Override
    public void sendEmail(SimpleMailMessage simpleMailMessage) {
        log.info("Simulando Envio de E-mail");
        log.info(simpleMailMessage.toString());
        log.info("e-mail Enviado com Sucesso");
    }
}
