# __IMPLEMENTANDO EMD-POINT DE PEDIDOS__

A classe da camada `repository`, `PedidoRepository`, foi implementada anteriormente, para disponibilizar os dados e necessario implementar a camada de `service` (que contem as regras de negocio da aplicacao) e a camada `resource` que expoe o end-point.

criacao da classe `PedidoService`

```java
package br.com.estudos.springboot.projetospringboot.service;

import br.com.estudos.springboot.projetospringboot.domain.Pedido;
import br.com.estudos.springboot.projetospringboot.ropository.PedidoRepository;
import br.com.estudos.springboot.projetospringboot.service.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository repository;

    @GetMapping(value = "/pedidos/{id}")
    public Pedido buscar(@PathVariable Integer id){

        Optional<Pedido> pedido = repository.findById(id);

        return pedido.orElseThrow(() -> new ObjectNotFoundException(
                "objeto com id " + id + " nao encontrado em " + this.getClass().getSimpleName()
            )
        );
    }
}
```

criacao da classe `PedidoResource`

```java
package br.com.estudos.springboot.projetospringboot.resource;

import br.com.estudos.springboot.projetospringboot.domain.Pedido;
import br.com.estudos.springboot.projetospringboot.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

    @Autowired
    private PedidoService service;

    @GetMapping(value = "buscar/{id}")
    public ResponseEntity<?> buscar(@PathVariable Integer id){
        Pedido pedido = service.buscar(id);
        return ResponseEntity.ok().body(pedido);
    }
}
```

#
## Tratamento do erro de referencia ciclica

A implementacao da aplicacao feita de acordo com o para pesrsistir os objetos no banco de dados, vamos refatorar o metodo `run()` na classe principal do Spring, instanciando os objetos de acordo com o [diagrama UML de objetos](../../ConteudoDoCurso/Secao2-ImplementacaoDoModeloConceitual/Diagrama/diagrama-de-objetos.png). possui diversos relacionamentos bidirecionais, e necessario realizar o tratamento para que nao ocorra recursao infinita (referencia ciclica), ao acessar o end-point.

partindo das refatoracoes na classe de `domain` `Pedido`, vamos realizar as alteracoes.

```java
package br.com.estudos.springboot.projetospringboot.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.*;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Date instante;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "pedido")
    private Pagamento pagamento;

    @ManyToOne
    @JoinColumn(name = "endereco_de_entrega_id")
    private Endereco enderecoDeEntrega;

    // invertido para apenas o pedido mostrar os clientes
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "id.pedido")
    private Set<ItemPedido> itens = new HashSet<>();

    public Pedido() {
    }

    public Pedido(Integer id, Date instante, Endereco enderecoDeEntrega, Cliente cliente) {
        this.id = id;
        this.instante = instante;
        this.enderecoDeEntrega = enderecoDeEntrega;
        this.cliente = cliente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Produto> getProdutos(){
        List<Produto> lista = new ArrayList<>();
        for(ItemPedido itemPedido : itens){
            lista.add(itemPedido.getItem());
        }
        return lista;
    }

    public Date getInstante() {
        return instante;
    }

    public void setInstante(Date instante) {
        this.instante = instante;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public Endereco getEnderecoDeEntrega() {
        return enderecoDeEntrega;
    }

    public void setEnderecoDeEntrega(Endereco enderecoDeEntrega) {
        this.enderecoDeEntrega = enderecoDeEntrega;
    }

    @JsonIgnore
    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Set<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(Set<ItemPedido> itens) {
        this.itens = itens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(id, pedido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", instante=" + instante +
                ", pagamento=" + pagamento +
                ", enderecoDeEntrega=" + enderecoDeEntrega +
                ", cliente=" + cliente +
                '}';
    }
}
```

como o acesso sera feito atraves do end-point de pedido apenas o atributo cliente foi alterado, com a troca das _annotations_, na classe cliente a _annotation_ do atributo pedidos foi alterada para `@JsonBackReference`.

refatoracao da classe de `domain` `ItemPedido`

```java
package br.com.estudos.springboot.projetospringboot.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class ItemPedido {

    // annotation que ignora a serializacao do Json
    @JsonIgnore
    @EmbeddedId
    private ItemPedidoPK id = new ItemPedidoPK();

    private Double desconto;

    private Integer quantidade;

    private Float preco;

    public ItemPedido() {
    }

    public ItemPedido(Pedido pedido, Produto produto, Double desconto, Integer quantidade, Float preco) {
        this.id.setPedido(pedido);
        this.id.setProduto(produto);
        this.desconto = desconto;
        this.quantidade = quantidade;
        this.preco = preco;
    }

    // annotation que ignora a serializacao do Json
    @JsonIgnore
    public Pedido getPedido(){
        return id.getPedido();
    }

    public Produto getItem(){
        return id.getProduto();
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Float getPreco() {
        return preco;
    }

    public void setPreco(Float preco) {
        this.preco = preco;
    }
}
```

note que foram adcionadas a _annotation_ `@JsonIgnore` no atributo id, para que nao seja necessario considerar o tratamento para o nivel da classe `ItemPedidoPK`, e no metodo getPedido, pois todos os metodos que iniciam com `get` tambem sao serializados.

por fim a classe de `domain` `Produto`

```java
package br.com.estudos.springboot.projetospringboot.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

@Entity
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private Double preco;

    @JsonBackReference
    @ManyToMany
    @JoinTable(
            name = "produto_categoria",
            joinColumns = @JoinColumn(name = "produto_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Categoria> categorias = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "id.produto")
    private Set<ItemPedido> itens =new HashSet<>();

    public Produto(){

    }

    public Produto(Integer id, String nome, Double preco) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
    }

    @JsonIgnore
    public List<Pedido> getPedidos(){
        List<Pedido> lista = new ArrayList<>();
        for(ItemPedido itemPedido : itens){
            lista.add(itemPedido.getPedido());
        }
        return lista;
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

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public Set<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(Set<ItemPedido> itens) {
        this.itens = itens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return Objects.equals(id, produto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", categorias=" + categorias +
                '}';
    }
}
```

semelhante a classe anterior o atributo e o metodo foram anotados com `@JsonIgnore`, pois nao queremos serializa-los.

#
## Testanto End-Point

utilizando o software postman obtemos o seguinte resultado ao acessar o end-point

Json serializado

```json
{
    "id": 2,
    "instante": "2017-10-10T22:35:00.000+00:00",
    "pagamento": {
        "id": 2,
        "estadoPagamento": "QUITADO",
        "dataVencimento": "2017-10-20T02:00:00.000+00:00",
        "dataPagamento": null
    },
    "enderecoDeEntrega": {
        "id": 2,
        "logradouro": "Avenida Matos",
        "numero": "105",
        "complemente": "Sala 800",
        "bairro": "Centro",
        "cep": "38777012",
        "cidade": {
            "id": 2,
            "nome": "Sao Paulo",
            "estado": {
                "id": 2,
                "nome": "Sao Paulo"
            }
        }
    },
    "cliente": {
        "id": 1,
        "nome": "Maria Silva",
        "email": "maria@gmail.com",
        "cpfOuCnpj": "36378912377",
        "tipo": "PESSOA_FISICA",
        "enderecos": [
            {
                "id": 1,
                "logradouro": "Rua Flores",
                "numero": "300",
                "complemente": "Apto 203",
                "bairro": "Jardim",
                "cep": "38220834",
                "cidade": {
                    "id": 1,
                    "nome": "Uberlandia",
                    "estado": {
                        "id": 1,
                        "nome": "Minas Gerais"
                    }
                }
            },
            {
                "id": 2,
                "logradouro": "Avenida Matos",
                "numero": "105",
                "complemente": "Sala 800",
                "bairro": "Centro",
                "cep": "38777012",
                "cidade": {
                    "id": 2,
                    "nome": "Sao Paulo",
                    "estado": {
                        "id": 2,
                        "nome": "Sao Paulo"
                    }
                }
            }
        ],
        "telefones": [
            "27363323",
            "93838393"
        ]
    },
    "itens": [
        {
            "desconto": 100,
            "quantidade": 1,
            "preco": 800,
            "item": {
                "id": 2,
                "nome": "Impressora",
                "preco": 800
            }
        }
    ],
    "produtos": [
        {
            "id": 2,
            "nome": "Impressora",
            "preco": 800
        }
    ]
}
```
