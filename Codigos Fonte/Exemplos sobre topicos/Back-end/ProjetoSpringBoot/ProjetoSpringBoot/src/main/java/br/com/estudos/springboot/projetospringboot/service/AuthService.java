package br.com.estudos.springboot.projetospringboot.service;

import br.com.estudos.springboot.projetospringboot.domain.Cliente;
import br.com.estudos.springboot.projetospringboot.ropository.ClienteRepository;
import br.com.estudos.springboot.projetospringboot.service.email.EmailService;
import br.com.estudos.springboot.projetospringboot.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private BCryptPasswordEncoder enconder;

    @Autowired
    private EmailService emailService;

    // recebe um email, pois precisa verificar se o cliente existe.
    public void sendNewPassword(String email){

        Cliente cliente = clienteRepository.findByEmail(email);

        if(cliente == null) {
            throw new ObjectNotFoundException("Email nao cadastrado no sistema");
        }

        String newPassword = newPassword();
        cliente.setSenha(enconder.encode(newPassword));

        clienteRepository.save(cliente);

        emailService.sendNewPasswordEmail(cliente, newPassword);
    }

    private String newPassword() {
        char[] chars = new char[10];
        for(int i=0; i < chars.length; i++){
            chars[i] = randomChar();
        }
        return new String(chars);
    }

    private char randomChar() {
        Random random = new Random();
        int option = random.nextInt(3);

        // gera um char de 0 a 10 segundo a tabela Unicode
        if(option == 0){
            return (char) (random.nextInt(10) + 48);
        }

        // gera letra maiuscula segundo a tabela unicode

        else if(option == 1){
            return (char) (random.nextInt(25) + 65);
        }

        else {
            return (char) (random.nextInt(25) + 97);
        }
    }
}
