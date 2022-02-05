package br.com.estudos.springboot.projetospringboot.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

    @RequestMapping(method = RequestMethod.GET, value = "/listar")
    public String listar(){
        return "metodo listar() esta funcionando";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/mostrar")
    public String mostrar(){
        return "metodo mostrar() esta funcionando";
    }

}
