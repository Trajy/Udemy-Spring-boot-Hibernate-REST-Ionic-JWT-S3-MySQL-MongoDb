package br.com.estudos.springboot.projetospringboot.resource;

import br.com.estudos.springboot.projetospringboot.domain.dto.ClienteDTO;
import br.com.estudos.springboot.projetospringboot.domain.dto.ClienteNovoDTO;
import br.com.estudos.springboot.projetospringboot.ropository.EnderecoRepository;
import br.com.estudos.springboot.projetospringboot.service.ClienteService;
import br.com.estudos.springboot.projetospringboot.service.GenericoService;
import br.com.estudos.springboot.projetospringboot.service.configuracao.ConfiguracaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

import br.com.estudos.springboot.projetospringboot.domain.Cliente;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
@RequestMapping(value = "/clientes")
@Lazy
public class ClienteResource {

     @Autowired ClienteService service;

     @Autowired
    EnderecoRepository enderecoRepository;

    //private GenericoService<Cliente> service;

    public ClienteResource() {
        /*this.service = new AnnotationConfigApplicationContext(ConfiguracaoService.class).
            getBean(GenericoService.class, Cliente.class);*/
    }

    @RequestMapping(method = RequestMethod.GET, value = "/buscar/{id}")
    public ResponseEntity<?> buscar(@PathVariable Integer id){

        Cliente cliente = service.buscar(id);

        return ResponseEntity.ok().body(cliente);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/novo")
    public ResponseEntity<Void> inserir(@Valid @RequestBody ClienteNovoDTO clienteNovoDTO) {
        Cliente cliente = service.toEntidade(clienteNovoDTO);
        cliente = service.inserir(cliente);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().replacePath("clientes/buscar/{id}").build(cliente.getId());
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/alterar/{id}")
    public ResponseEntity<Void> alterar(@Valid @RequestBody Cliente cliente, @PathVariable Integer id){
        cliente.setId(id);
        cliente = service.alterar(cliente);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/deletar/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/buscar/todas")
    public ResponseEntity<List<ClienteDTO>> buscarTodas(){
        List<ClienteDTO> clientes = service.buscarTodas(/*ClienteDTO.class*/);
        return ResponseEntity.ok().body(clientes);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/buscar")
    public ResponseEntity<Page<ClienteDTO>> buscarPaginado(
            @RequestParam(value = "pg", defaultValue = "0") Integer numeroPagina,
            @RequestParam(value = "qtditenspg", defaultValue = "2") Integer linhasPorPagina,
            @RequestParam(value = "ordenar", defaultValue = "nome") String ordenarPor,
            @RequestParam(value = "dir", defaultValue = "ASC") String direcaoOrdencao
    ){
        Page<ClienteDTO> paginaClienteDto = service.buscarPaginado(numeroPagina, linhasPorPagina, ordenarPor, direcaoOrdencao/*, ClienteDTO.class*/);
        return ResponseEntity.ok().body(paginaClienteDto);
    }

}
