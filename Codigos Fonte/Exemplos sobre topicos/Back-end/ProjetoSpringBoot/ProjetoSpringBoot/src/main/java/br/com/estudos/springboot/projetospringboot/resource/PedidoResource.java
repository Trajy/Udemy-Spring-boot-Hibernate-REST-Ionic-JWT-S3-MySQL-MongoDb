package br.com.estudos.springboot.projetospringboot.resource;

import br.com.estudos.springboot.projetospringboot.domain.Pedido;
import br.com.estudos.springboot.projetospringboot.domain.Pedido;
import br.com.estudos.springboot.projetospringboot.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
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

}
