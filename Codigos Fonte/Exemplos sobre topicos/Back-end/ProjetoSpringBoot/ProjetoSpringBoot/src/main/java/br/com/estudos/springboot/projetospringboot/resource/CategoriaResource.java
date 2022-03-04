package br.com.estudos.springboot.projetospringboot.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

import br.com.estudos.springboot.projetospringboot.domain.Categoria;
import br.com.estudos.springboot.projetospringboot.domain.dto.CategoriaDTO;
import br.com.estudos.springboot.projetospringboot.service.CategoriaService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

    @Autowired
    private CategoriaService service;

    @RequestMapping(method = RequestMethod.GET, value = "/buscar/{id}")
    public ResponseEntity<?> buscar(@PathVariable Integer id){

        Categoria categoria = service.buscar(id);

        return ResponseEntity.ok().body(categoria);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/nova")
    public ResponseEntity<Void> inserir(@Valid @RequestBody Categoria categoria){
        categoria = service.inserir(categoria);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().replacePath("categorias/buscar/{id}").build(categoria.getId());
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/alterar/{id}")
    public ResponseEntity<Void> alterar(@Valid @RequestBody Categoria categoria, @PathVariable Integer id){
        categoria.setId(id);
        categoria = service.alterar(categoria);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deletar/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/buscar/todas")
    public ResponseEntity<List<CategoriaDTO>> buscarTodas(){
        List<CategoriaDTO> categorias = service.buscarTodas();
        return ResponseEntity.ok().body(categorias);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/buscar")
    public ResponseEntity<Page<CategoriaDTO>> buscarPaginado(
            @RequestParam(value = "pg", defaultValue = "0") Integer numeroPagina,
            @RequestParam(value = "qtditenspg", defaultValue = "2") Integer linhasPorPagina,
            @RequestParam(value = "ordenar", defaultValue = "nome") String ordenarPor,
            @RequestParam(value = "dir", defaultValue = "ASC") String direcaoOrdencao
    ){
        Page<CategoriaDTO> paginaCategoriaDto = service.buscarPaginado(numeroPagina, linhasPorPagina, ordenarPor, direcaoOrdencao);
        return ResponseEntity.ok().body(paginaCategoriaDto);
    }

}
