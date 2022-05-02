package br.com.estudos.springboot.projetospringboot.resource;

import br.com.estudos.springboot.projetospringboot.domain.Pedido;
import br.com.estudos.springboot.projetospringboot.domain.Produto;
import br.com.estudos.springboot.projetospringboot.domain.dto.ProdutoDTO;
import br.com.estudos.springboot.projetospringboot.resource.utils.URL;
import br.com.estudos.springboot.projetospringboot.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

    @Autowired private ProdutoService service;

    public ProdutoResource() {

    }

    @GetMapping(value = "buscar/{id}")
    public ResponseEntity<?> buscar(@PathVariable Integer id){
        Produto pedido = service.buscar(id);
        return ResponseEntity.ok().body(pedido);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/buscar")
    public ResponseEntity<Page<ProdutoDTO>> buscarPaginado(
        @RequestParam(value = "nome", defaultValue = "") String nome,
        @RequestParam(value = "categorias", defaultValue = "") String categorias,
        @RequestParam(value = "pg", defaultValue = "0") Integer numeroPagina,
        @RequestParam(value = "qtditenspg", defaultValue = "10") Integer linhasPorPagina,
        @RequestParam(value = "ordenar", defaultValue = "nome") String ordenarPor,
        @RequestParam(value = "dir", defaultValue = "ASC") String direcaoOrdencao
    ) {

        Page<Produto> paginaProdutos = service.search(URL.decodeParam(nome), URL.toIntList(categorias), numeroPagina, linhasPorPagina, ordenarPor, direcaoOrdencao);
        Page<ProdutoDTO> paginaProdutoDTOs = paginaProdutos.map(produto -> new ProdutoDTO(produto));
        return ResponseEntity.ok().body(paginaProdutoDTOs);
    }
}
