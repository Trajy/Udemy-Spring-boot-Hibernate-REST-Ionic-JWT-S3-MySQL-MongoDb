package br.com.estudos.springboot.projetospringboot.service.email;

import br.com.estudos.springboot.projetospringboot.domain.Pedido;
import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    void sendOrderConfirmationEmail(Pedido pedido);

    void sendEmail(SimpleMailMessage simpleMailMessage);
}
