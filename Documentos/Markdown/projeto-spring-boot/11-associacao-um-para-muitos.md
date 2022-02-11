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



