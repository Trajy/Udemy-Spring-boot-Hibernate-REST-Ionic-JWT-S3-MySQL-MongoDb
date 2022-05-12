package br.com.estudos.springboot.projetospringboot.service;

import br.com.estudos.springboot.projetospringboot.security.UserSpringSecurity;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserService {

    public static UserSpringSecurity authenticated(){
        try {
            // retorna o usuario que esta autenticado
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        catch (Exception e){
            return null;
        }

    }
}
