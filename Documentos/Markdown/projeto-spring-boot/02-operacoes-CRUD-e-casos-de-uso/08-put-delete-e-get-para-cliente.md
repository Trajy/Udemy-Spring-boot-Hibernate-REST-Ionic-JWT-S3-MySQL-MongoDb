# __PUT, DELETE E GET PARA CLIENTE__

Para implementar as operacoes de forma correta e importante observar os relacionamentos no [diagrama UML de classes](../../ConteudoDoCurso/Secao2-ImplementacaoDoModeloConceitual/Diagrama/diagrama-de-classes.png).

Note que a classe `Cliente` necessita de pelo menos 1 Objeto da classe `Endereco` e de forma similar a classe `Endereco` necessida de um Objeto da classe `Cidade` que tambem necessita de um `Estado`. Alem destes relacionamentos a classe `Cliente` tambem necessita conhecer ao menos um objeto do tipo `Telefone`.

inicialmente vamos implementar as operacoes de listagem (GET), Alteracao (PUT) e Delecao (DELETE). O primeiro passo e elaborar os end-points na camada de `controller` para expor o servico.

implementacao dos novos end-points na classe `ClienteResource`

```java
package br.com.estudos.springboot.projetospringboot.resource;

import br.com.estudos.springboot.projetospringboot.domain.Cliente
        ;
import br.com.estudos.springboot.projetospringboot.domain.dto.ClienteDTO;
import br.com.estudos.springboot.projetospringboot.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

    @Autowired
    private ClienteService service;

    @RequestMapping(method = RequestMethod.GET, value = "/buscar/{id}")
    public ResponseEntity<?> buscar(@PathVariable Integer id){

        Cliente cliente = service.buscar(id);

        return ResponseEntity.ok().body(cliente);
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
        List<ClienteDTO> clientes = service.buscarTodas();
        return ResponseEntity.ok().body(clientes);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/buscar")
    public ResponseEntity<Page<ClienteDTO>> buscarPaginado(
            @RequestParam(value = "pg", defaultValue = "0") Integer numeroPagina,
            @RequestParam(value = "qtditenspg", defaultValue = "2") Integer linhasPorPagina,
            @RequestParam(value = "ordenar", defaultValue = "nome") String ordenarPor,
            @RequestParam(value = "dir", defaultValue = "ASC") String direcaoOrdencao
    ){
        Page<ClienteDTO> paginaClienteDto = service.buscarPaginado(numeroPagina, linhasPorPagina, ordenarPor, direcaoOrdencao);
        return ResponseEntity.ok().body(paginaClienteDto);
    }

}
```

alem da implementacao acima e necessario criar uma classe DTO para cliente, contudo, deve conter apenas os atributos que podem ser alterados (neste caso `CfpOuCnpj` e `tipo` devem permanecer inalterados apos sua insercao no bando de dados).

criacao da classe DTO `ClienteDTO`

```java
package br.com.estudos.springboot.projetospringboot.domain.dto;

import br.com.estudos.springboot.projetospringboot.domain.Cliente;

public class ClienteDTO {

    private Integer id;

    private String nome;

    private String email;

    public ClienteDTO() {
    }

    public ClienteDTO(Cliente cliente){
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.email = cliente.getEmail();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
```

de acordo com a arquitetura do projeto, metodos seram implementados na camada de `services` utilizando um objeto da camada de `repository` como dependencia.

implementacao dos novos metodos na classe `ClienteService`

```java

```


