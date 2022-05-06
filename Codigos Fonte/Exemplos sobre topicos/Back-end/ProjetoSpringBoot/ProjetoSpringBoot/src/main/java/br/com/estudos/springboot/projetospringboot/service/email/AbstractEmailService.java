package br.com.estudos.springboot.projetospringboot.service.email;

import br.com.estudos.springboot.projetospringboot.domain.Pedido;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import java.util.Date;

public abstract class AbstractEmailService implements EmailService {

    @Value("${default.sender}")
    private String sender;

    @Override
    public void sendOrderConfirmationEmail(Pedido pedido) {
        SimpleMailMessage simpleMailMessage = prepareSimpleMainMessageFromPedido(pedido);
        sendEmail(simpleMailMessage);
    }

    protected SimpleMailMessage prepareSimpleMainMessageFromPedido(Pedido pedido){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(pedido.getCliente().getEmail());
        simpleMailMessage.setFrom(sender);
        simpleMailMessage.setSubject("Pedido confirmado: " + pedido.getId());
        simpleMailMessage.setSentDate(new Date());
        simpleMailMessage.setText(pedido.toString());

        return simpleMailMessage;
    }
}
