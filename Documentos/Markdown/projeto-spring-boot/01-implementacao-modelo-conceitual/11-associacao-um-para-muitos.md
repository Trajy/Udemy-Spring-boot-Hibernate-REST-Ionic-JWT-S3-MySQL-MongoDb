# __ASSOCIACAO UM PARA MUITOS__

Conforme o [diagrama UML de classes](../../ConteudoDoCurso/Secao2-ImplementacaoDoModeloConceitual/Diagrama/diagrama-de-classes.png), sera feita a implementacao das demais classes e seus relacinamentos, a proxima entidade a ser implementada sera `Cidade` que possui um relacionamento de muitos para um com a classe `Estado`.

criando as classes de dominio.

classe de dominio `Cidade`

```java

package br.com.estudos.springboot.projetospringboot.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;

    public Cidade(){

    }

    public Cidade(Integer id, String nome, Estado estado) {
        this.id = id;
        this.nome = nome;
        this.estado = estado;
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

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cidade cidade = (Cidade) o;
        return Objects.equals(id, cidade.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Cidade{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", estado=" + estado +
                '}';
    }

}
```

note que o atributo do tipo `Estado` contem as _annotations_ `@ManyToOne` que significa que muitos deste contexto (neste caso `Cidade`) possuem relacionamento com apenas um do contexto adjacente (neste caso `Estado`), e `@JoinColumn` que recebe o atributo `name` que ira declarar o nome do campo no banco de dados que contem a chave estrangeira relativa ao contexto adjacente (`Estado`).

```java
package br.com.estudos.springboot.projetospringboot.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Estado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;

    @OneToMany(mappedBy = "estado")
    private List<Cidade> cidades = new ArrayList<>();

    public Estado() {

    }

    public Estado(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estado estado = (Estado) o;
        return Objects.equals(id, estado.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Estado{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}

```
Na outra extremidade do relacionamento, basta declarar a _annotation_ `@OneToMany`, relacao um deste contexto (note que estamos agora invertendo os papeis, pois, estamos nos referindo a classe `Estado` que passa a ser nosso contexto, e a classe `Cidade` passa a ser o contexto adjacente) para muitos do contexto adjacente (um estado possui muitas cidades) que recebe como atributo `mapedBy` e seu valor e a referencia do atributo no contexto adjacente (`estado`).

#
## Refatoracoes adjascentes

Antes de iniciar a aplicacao vamos realizar algumas refatoracoes, a primeira e implementar as classes da camada `repository` referentes a cada uma das classes da camada `domain`.

```java
package br.com.estudos.springboot.projetospringboot.ropository;

import br.com.estudos.springboot.projetospringboot.domain.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CidadeRepository extends JpaRepository<Cidade, Integer> {
}
```

```java
package br.com.estudos.springboot.projetospringboot.ropository;

import br.com.estudos.springboot.projetospringboot.domain.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<Estado, Integer> {
}
```

implementadas as interfaces para acesso ao banco de dados, vamos refatorar o metodo `run()` na classe de inicializacao da aplicacao, `ProjetoSpringBootApplication`, para instanciar os objetos de acordo com o [diagrama UML de objetos](../../ConteudoDoCurso/Secao2-ImplementacaoDoModeloConceitual/Diagrama/diagrama-de-objetos.png) e persistir dados no banco.

```java
package br.com.estudos.springboot.projetospringboot;

import br.com.estudos.springboot.projetospringboot.domain.Categoria;
import br.com.estudos.springboot.projetospringboot.domain.Cidade;
import br.com.estudos.springboot.projetospringboot.domain.Estado;
import br.com.estudos.springboot.projetospringboot.domain.Produto;
import br.com.estudos.springboot.projetospringboot.ropository.CategoriaRepository;
import br.com.estudos.springboot.projetospringboot.ropository.CidadeRepository;
import br.com.estudos.springboot.projetospringboot.ropository.EstadoRepository;
import br.com.estudos.springboot.projetospringboot.ropository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
	}
}

```



