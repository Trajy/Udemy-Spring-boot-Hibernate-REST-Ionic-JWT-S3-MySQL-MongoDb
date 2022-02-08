package br.com.estudos.springboot.projetospringboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.ArrayList;
import java.util.List;

import br.com.estudos.springboot.projetospringboot.domain.Categoria;
import br.com.estudos.springboot.projetospringboot.ropository.CategoriaRepository;

@SpringBootApplication
public class ProjetoSpringBootApplication implements CommandLineRunner {

	@Autowired
	CategoriaRepository categoriaRepository;

	public static void main(String[] args) {
		SpringApplication.run(ProjetoSpringBootApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var cat1 = new Categoria(null, "Informatica");
		var cat2 = new Categoria(null, "Escritorio");

		List<Categoria> categorias = new ArrayList<>(){{
			add(cat1);
			add(cat2);
		}};

		categoriaRepository.saveAll(categorias);
	}
}
