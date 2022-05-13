package br.com.estudos.springboot.projetospringboot.resource;

import br.com.estudos.springboot.projetospringboot.domain.Pedido;
import br.com.estudos.springboot.projetospringboot.domain.Pedido;
import br.com.estudos.springboot.projetospringboot.domain.dto.CategoriaDTO;
import br.com.estudos.springboot.projetospringboot.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

    @Autowired private PedidoService service;

    public PedidoResource() {

    }

    @GetMapping(value = "buscar/{id}")
    public ResponseEntity<?> buscar(@PathVariable Integer id){
        Pedido pedido = service.buscar(id);
        return ResponseEntity.ok().body(pedido);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/novo")
    public ResponseEntity<Void> inserir(@Valid @RequestBody Pedido pedido){
        pedido = service.inserir(pedido);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().replacePath("pedidos/buscar/{id}").build(pedido.getId());
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/buscar")
    public ResponseEntity<Page<Pedido>> buscarPaginado(
        @RequestParam(value = "pg", defaultValue = "0") Integer numeroPagina,
        @RequestParam(value = "qtditenspg", defaultValue = "5") Integer linhasPorPagina,
        @RequestParam(value = "ordenar", defaultValue = "instante") String ordenarPor,
        @RequestParam(value = "dir", defaultValue = "DESC") String direcaoOrdencao
    ){
        Page<Pedido> paginaPedidos = service.buscarPaginado(numeroPagina, linhasPorPagina, ordenarPor, direcaoOrdencao);
        return ResponseEntity.ok().body(paginaPedidos);
    }

}
