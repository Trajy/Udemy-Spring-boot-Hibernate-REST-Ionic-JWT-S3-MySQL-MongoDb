package br.com.estudos.springboot.projetospringboot.security;

import br.com.estudos.springboot.projetospringboot.domain.dto.CredenciaisDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/*
    ao extender a classe UsernamePasswordAuthenticationFilter
    o spring ira interceptar automaticamente qualquer requisicao com o
    endereco /login (reservado para o spring security)
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // ESTA ETAPA E BOILER PLATE, ou seja o codigo segue o padrao que o framework necessita

    private AuthenticationManager authManager;

    private JWTUtil jwtUtil;

    // neste caso as dependencias serao injetadas atraves do construtor
    public JWTAuthenticationFilter(AuthenticationManager authManager, JWTUtil jwtUtil){
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    // metodo que ira realiizar a tentariva de autenticacao
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // Boiler Plate

        try {
            // pega o stream enviado e converte para o objeto desejado no caso CredenciaisDTO
            CredenciaisDTO credenciais = new ObjectMapper()
                .readValue(request.getInputStream(), CredenciaisDTO.class);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(credenciais.getEmail(), credenciais.getSenha(), new ArrayList<>());

            // verfica se a autenricacao e valida de acordo com as demais implementacoes relativas ao spring security
            Authentication authentication = authManager.authenticate(authToken);
            return authentication;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication auth){
        // caso attemptAuthentication consiga autenticar sera gerado o token JWT

        String username = ((UserSpringSecurity) auth.getPrincipal()).getUsername();
        String token = jwtUtil.generateToken(username);
        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("access-control-expose-headers", "Authorization");
    }
}
