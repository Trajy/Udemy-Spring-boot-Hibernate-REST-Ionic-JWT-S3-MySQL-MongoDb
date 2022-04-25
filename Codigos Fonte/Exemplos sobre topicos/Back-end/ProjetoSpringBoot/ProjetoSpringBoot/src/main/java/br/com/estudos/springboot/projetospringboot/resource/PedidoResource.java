package br.com.estudos.springboot.projetospringboot.resource;

import br.com.estudos.springboot.projetospringboot.domain.Pedido;
import br.com.estudos.springboot.projetospringboot.service.GenericoService;
import br.com.estudos.springboot.projetospringboot.service.PedidoService;
import br.com.estudos.springboot.projetospringboot.service.configuracao.ConfiguracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {


    @Autowired private PedidoService service;

    // private GenericoService<Pedido> service;

    public PedidoResource() {
        /*this.service = new AnnotationConfigApplicationContext(ConfiguracaoService.class).
            getBean(GenericoService.class, Pedido.class);*/
    }

    @GetMapping(value = "buscar/{id}")
    public ResponseEntity<?> buscar(@PathVariable Integer id){
        Pedido pedido = service.buscar(id);
        return ResponseEntity.ok().body(pedido);
    }

}
