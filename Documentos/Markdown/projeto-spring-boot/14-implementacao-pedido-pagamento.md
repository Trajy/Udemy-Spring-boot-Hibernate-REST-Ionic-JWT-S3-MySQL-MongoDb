# __IMPLEMENTACAO DAS CLASSES PEDIDO, PAGAMENTO E ENUM ESTADOPAGAMENTO__

seguindo o [diagrama UML de classes](../../ConteudoDoCurso/Secao2-ImplementacaoDoModeloConceitual/Diagrama/diagrama-de-classes.png) vamos implementar as demais classes do projeto, `Pedido`, `Pagamento`.

criacao da classe `Pagamento`, note que a classe possui heranca com as classes, `PagamentoComBoleto` e `PagametoComCartao`.

criacao da classe `Pagamento`.

```java
package br.com.estudos.springboot.projetospringboot.domain;

import br.com.estudos.springboot.projetospringboot.domain.enums.EstadoPagameno;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pagamento {

    @Id
    private Integer id;

    private Integer estadoPagamento;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "pedido_id")
    @MapsId
    private Pedido pedido;

    public Pagamento(){
    }

    public Pagamento(Integer id, EstadoPagameno estadoPagamento, Pedido pedido) {
        this.id = id;
        this.estadoPagamento = estadoPagamento.getId();
        this.pedido = pedido;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EstadoPagameno getEstadoPagamento() {
        return EstadoPagameno.toEnum(id);
    }

    public void setEstadoPagamento(EstadoPagameno estadoPagamento) {
        this.estadoPagamento = estadoPagamento.getId();
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pagamento pagamento = (Pagamento) o;
        return Objects.equals(id, pagamento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "id=" + id +
                ", estadoPagamento=" + estadoPagamento +
                ", pedido=" + pedido +
                '}';
    }
}
```

Algumas observacoes sao importantes, note que a classe possui o modificador de acesso `abstract`, para que nao seja instanciavel, pois os pagamentos serao instancias das subclasses. Como a relacao entre a classe pedido e pagamento e de um para um (_annotation_ `@OneToOne`), utilizamos a _annotation_ `@MapsId` que ira tornar o id da entidade pagamento o mesmo da entidade pedido, e por fim `@JoinColum` que contem o nome da coluna que ira receber a chave estrangeira. para os mapeamentos relacionados as classes que possuem herancas a _annotation_ `@Inheritance` ira estabelescer os relacionamentos, recebe como argumento o atributo `strategy` que contem a forma de geracao das tabelas, duas principais sao mais usadas, `InheritanceType.SINGLE_TABLE` ira criar uma unica tabela que contem todos os atribudos das classes heradas, ao instanciar uma classe filha (subclasse) os atributos das demais classes seram setados como null, deste modo as consultas tornam-se mais performaticas, outra forma e `InheritanceType.JOINED` que ira gerar tabelas separadas para cada subclasse, desta forma os dados ficam mais organizados e evitamos a geracao de campos null sem necessidade no banco de dados, porem e menos performatico, pois necessita realizar join entre as tabelas para retornar os resultados.

criacao das subclasses.

subclasse `PagamentoComBoleto`

```java
package br.com.estudos.springboot.projetospringboot.domain;

import br.com.estudos.springboot.projetospringboot.domain.enums.EstadoPagameno;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class PagamentoComBoleto extends Pagamento {

    private Date dataVencimento;
    private Date dataPagamento;

    public PagamentoComBoleto(){
    }

    public PagamentoComBoleto(Integer id, EstadoPagameno estadoPagamento, Pedido pedido, Date dataVencimento, Date dataPagamento) {
        super(id, estadoPagamento, pedido);
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }
}
```

subclasse `PagametoComCartao`

```java
package br.com.estudos.springboot.projetospringboot.domain;

import br.com.estudos.springboot.projetospringboot.domain.enums.EstadoPagameno;

import javax.persistence.Entity;

@Entity
public class PagamentoComCartao extends Pagamento {

    private Integer numeroDeParcelas;

    PagamentoComCartao(){
    }

    public PagamentoComCartao(Integer id, EstadoPagameno estadoPagamento, Pedido pedido, Integer numeroDeParcelas) {
        super(id, estadoPagamento, pedido);
        this.numeroDeParcelas = numeroDeParcelas;
    }

    public Integer getNumeroDeParcelas() {
        return numeroDeParcelas;
    }

    public void setNumeroDeParcelas(Integer numeroDeParcelas) {
        this.numeroDeParcelas = numeroDeParcelas;
    }
}
```

note que nao e necessario implementar os metodos `equals()` e `hashCode()`, pois o id e herdado da super classe (classe pai).

criacao da classe `Pedido`

```java
package br.com.estudos.springboot.projetospringboot.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

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

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    Pedido(){

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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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

na _annotation_ `@OneToOne`, e necessario informar como argumento o atributo `cacade = cascadeType.ALL`.

as demais relacios foram implementadas de acordo com o diagrama de classes.

refatoracao da classe cliente `Cliente`, para adicionar o relacionamento com `Peidido`

```java
package br.com.estudos.springboot.projetospringboot.domain;

import br.com.estudos.springboot.projetospringboot.domain.enums.TipoCliente;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.*;

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    private String email;

    private String cpfOuCnpj;

    private Integer tipo;

    @JsonManagedReference
    @OneToMany(mappedBy = "cliente")
    private List<Endereco> enderecos = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "telefone")
    private Set<String> telefones = new HashSet<>();

    // relacionamento com pedidos
    @JsonManagedReference
    @OneToMany(mappedBy = "cliente")
    private List<Pedido> pedidos = new ArrayList<>();

    public Cliente(){

    }

    public Cliente(Integer id, String nome, String email, String cpfOuCnpj, TipoCliente tipo) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpfOuCnpj = cpfOuCnpj;
        this.tipo = tipo.getId();
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

    public String getCpfOuCnpj() {
        return cpfOuCnpj;
    }

    public void setCpfOuCnpj(String cpfOuCnpj) {
        this.cpfOuCnpj = cpfOuCnpj;
    }

    public TipoCliente getTipo() {
        return TipoCliente.toEnum(tipo);
    }

    public void setTipo(TipoCliente tipo) {
        this.tipo = tipo.getId();
    }

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public Set<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(Set<String> telefones) {
        this.telefones = telefones;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", cpfOuCnpj='" + cpfOuCnpj + '\'' +
                ", tipo=" + tipo +
                ", enderecos=" + enderecos +
                ", telefones=" + telefones +
                '}';
    }
}
```

#
## REFATORACOES ADJACENTES

cricao das interfaces na camada de `repository`

interface `PedidoRepository`

```java
package br.com.estudos.springboot.projetospringboot.ropository;

import br.com.estudos.springboot.projetospringboot.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
}
```

interface `PagamentoRepository`

```java
package br.com.estudos.springboot.projetospringboot.ropository;

import br.com.estudos.springboot.projetospringboot.domain.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
}
```

refatoracao do metodo `run()` para instanciar os objetos a partir da novas classes de `domain` e persistir no banco de dados.

```java
package br.com.estudos.springboot.projetospringboot;

import br.com.estudos.springboot.projetospringboot.domain.*;
import br.com.estudos.springboot.projetospringboot.domain.enums.EstadoPagameno;
import br.com.estudos.springboot.projetospringboot.domain.enums.TipoCliente;
import br.com.estudos.springboot.projetospringboot.ropository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.Arrays;

@SpringBootApplication
public class ProjetoSpringBootApplication implements CommandLineRunner {

	@Autowired
	CategoriaRepository categoriaRepository;

	@Autowired
	ProdutoRepository produtoRepository;

	@Autowired
	EstadoRepository estadoRepository;

	@Autowired
	CidadeRepository cidadeRepository;

	@Autowired
	EnderecoRepository enderecoRepository;

	@Autowired
	ClienteRepository clienteRepository;

	@Autowired
	PedidoRepository pedidoRepository;

	@Autowired
	PagamentoRepository pagamentoRepository;

	public static void main(String[] args) {
		SpringApplication.run(ProjetoSpringBootApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		var cat1 = new Categoria(null, "Informatica");
		var cat2 = new Categoria(null, "Escritorio");

		var prod1 = new Produto(null, "Computador", 2000.00);
		var prod2 = new Produto(null, "Impressora", 800.00);
		var prod3 = new Produto(null, "Mouse", 80.00);

		cat1.getProdutos().addAll(Arrays.asList(prod1, prod2, prod3));
		cat2.getProdutos().addAll(Arrays.asList(prod3));

		prod1.getCategorias().addAll(Arrays.asList(cat1));
		prod2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		prod3.getCategorias().addAll(Arrays.asList(cat1));

		categoriaRepository.saveAll(Arrays.asList(cat1,cat2));
		produtoRepository.saveAll(Arrays.asList(prod1, prod2, prod3));

		var est1 = new Estado(null, "Minas Gerais");
		var est2 = new Estado(null, "Sao Paulo");

		var c1 = new Cidade(null, "Uberlandia", est1);
		var c2 = new Cidade(null, "Sao Paulo", est2);
		var c3 = new Cidade(null, "Campinas", est2);

		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2, c3));

		estadoRepository.saveAll(Arrays.asList(est1, est2));
		cidadeRepository.saveAll(Arrays.asList(c1, c2, c3));

		Cliente cli1 = new Cliente(null, "Maria Silva", "maria@gmail.com", "36378912377", TipoCliente.PESSOA_FISICA);

		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 203", "Jardim", "38220834", cli1, c1);
		Endereco e2 = new Endereco(null, "Avenida Matos", "105", "Sala 800", "Centro", "38777012", cli1, c2);

		cli1.getEnderecos().addAll(Arrays.asList(e1, e2));

		String t1 = "27363323";
		String t2 = "93838393";
		cli1.getTelefones().addAll(Arrays.asList(t1, t2));

		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(e1, e2));

        // novos objetos instanciados
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");

		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), e1, cli1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), e2, cli1);

		Pagamento pgto1 = new PagamentoComCartao(null, EstadoPagameno.QUITADO, ped1, 6);
		ped1.setPagamento(pgto1);
		Pagamento pgto2 = new PagamentoComBoleto(null, EstadoPagameno.PEDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);
		ped2.setPagamento(pgto2);

		cli1.getPedidos().addAll(Arrays.asList(ped1, ped2));

		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pgto1, pgto2));
	}
}
```

a classe `SimpleDateFormat` nos auxilia a inserir uma data com uma mascara especifica, neste casso `dd/MM/yyyy HH:mm`. Ao acessar o end-point de cliente as novas entidades serao retornadas no json.



