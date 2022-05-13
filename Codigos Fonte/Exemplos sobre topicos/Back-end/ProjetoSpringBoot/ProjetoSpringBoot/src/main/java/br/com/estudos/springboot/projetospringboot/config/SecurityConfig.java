package br.com.estudos.springboot.projetospringboot.config;

import br.com.estudos.springboot.projetospringboot.security.JWTAuthenticationFilter;
import br.com.estudos.springboot.projetospringboot.security.JWTAuthorizationFilter;
import br.com.estudos.springboot.projetospringboot.security.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // habilita as annotarion @PreAuthorize("hasAnyRole('ADMIN')") nos end-points
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    JWTUtil jwtUtil;

    // Instancia da classe UserDatailsService utilizada pelo @Bean configure
    @Autowired
    private UserDetailsService userDetailsService;

    // Instancia que contem dados sobre o ambiente no qual a applicacao esta sendo executada.
    @Autowired
    private Environment env;

    // Array para guardar os endpoints que nao necessitao de autenticacao
    private static final String[] PUBLIC_MATCHERS = {
        "/h2/**"
    };

    /*
        para os casos de categoria e cliente por exemplo, nao queremos deixar o acesso liberado
        para todos os end-points, pois um usuario que nao e administrador do sistema nao pode,
        inserir, alterar ou deletar nada na base de dados, ele pode apenas recuperar os dados,
        para isto e feito uma limitacao para os metodos get.
     */
    private static final String[] PUBLIC_MATCHERS_GET = {
        "/produtos/**",
        "/categorias/**"
    };

    private static final String[] PUBLIC_MATCHERS_POST = {
        "/clientes/**",
        "/auth/forgot/**"
    };

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        /*
            como o banco de dados H2 gera varios frames na tela em sua construcao e necessario liberar pelo framework
         */
        if(Arrays.asList(env.getActiveProfiles()).contains("test")){
            httpSecurity.headers().frameOptions().disable();
        }

        /*
          o metodo cors() procura se existe um bean do tipo CorsConfigurationSource e applica automaticamente ao HttpSecurity

         .and().crsf().disable() desabilira protecao contra ataques do tipo csrf (nao e necessario para applicacoes que nao armazenao autenticacao por secao)
        */
        httpSecurity.cors().and().csrf().disable();

        /*
            permite apenas acesso aos endpoints declarados no array PUBLIC_MATCHER
            o restante dos end-points solicita autenticacao.

         */
        httpSecurity.authorizeRequests().antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
            .antMatchers(PUBLIC_MATCHERS_POST).permitAll()
            .antMatchers(PUBLIC_MATCHERS).permitAll()
            .anyRequest().authenticated();

        // para garantir que a politica de acessos sera stateless, ou seja nao ira armazenar a secao do usuario.
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //recebe o filtro de autenticacao
        httpSecurity.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil));
        httpSecurity.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService));
    }


    /*
        E necessario sobrecarregar esse metodo pois o framework precisa saber
        quem e o UserDatailsService e o Algoritimo de codificacao, neste caso
        implementado pela classe BCryptPasswordEncoder
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    // bean responsavel por forncecer uma instancia da classe para encriptar a senha
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
