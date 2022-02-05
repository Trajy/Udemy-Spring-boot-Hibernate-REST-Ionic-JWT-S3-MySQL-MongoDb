package br.com.estudos.springboot.projetospringboot.resource;

import br.com.estudos.springboot.projetospringboot.domain.Categoria;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

    @RequestMapping(method = RequestMethod.GET, value = "/listar")
    public List<Categoria> listar(){

        List<Categoria> categorias = new ArrayList<>();

        Categoria categoriaA = new Categoria(1, "informatica");
        Categoria categoriaB = new Categoria(2, "Escritorio");

        categorias.add(categoriaA);
        categorias.add(categoriaB);

        return categorias;
    }

}
