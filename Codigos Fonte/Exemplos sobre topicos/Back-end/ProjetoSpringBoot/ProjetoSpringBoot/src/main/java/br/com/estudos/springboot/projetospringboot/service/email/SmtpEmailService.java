package br.com.estudos.springboot.projetospringboot.service.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.internet.MimeMessage;

public class SmtpEmailService extends AbstractEmailService {

    private static final Logger log = LoggerFactory.getLogger(SmtpEmailService.class);

    @Autowired
    private MailSender mailSender;

    @Autowired
    JavaMailSender javaMailSender;

    @Override
    public void sendEmail(SimpleMailMessage simpleMailMessage) {
        log.info("Enviando de E-mail");
        mailSender.send(simpleMailMessage);
        log.info("e-mail Enviado com Sucesso");

    }

    @Override
    public void sendHtmlEmail(MimeMessage mimeMessage) {
        log.info("Enviando de E-mail");
        javaMailSender.send(mimeMessage);
        log.info("e-mail Enviado com Sucesso");
    }
}
