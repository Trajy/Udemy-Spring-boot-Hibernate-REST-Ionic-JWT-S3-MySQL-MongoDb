package br.com.estudos.springboot.projetospringboot;

import br.com.estudos.springboot.projetospringboot.domain.*;
import br.com.estudos.springboot.projetospringboot.domain.enums.EstadoPagamento;
import br.com.estudos.springboot.projetospringboot.domain.enums.TipoCliente;
import br.com.estudos.springboot.projetospringboot.ropository.*;
import br.com.estudos.springboot.projetospringboot.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Arrays;

@SpringBootApplication
public class ProjetoSpringBootApplication implements CommandLineRunner {

	@Autowired
	private S3Service s3Service;

	public static void main(String[] args) {
		SpringApplication.run(ProjetoSpringBootApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		s3Service.uploadFile("/home/trajy/Imagens/3ds.jpg");
	}
}
