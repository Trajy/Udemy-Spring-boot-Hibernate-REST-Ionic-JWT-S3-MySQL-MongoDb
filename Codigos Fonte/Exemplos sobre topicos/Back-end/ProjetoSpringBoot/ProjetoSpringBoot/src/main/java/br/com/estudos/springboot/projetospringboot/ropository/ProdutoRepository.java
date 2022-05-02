package br.com.estudos.springboot.projetospringboot.ropository;

import br.com.estudos.springboot.projetospringboot.domain.Categoria;
import br.com.estudos.springboot.projetospringboot.domain.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    @Transactional(readOnly = true)
    // Consulta com JPQL hard coded
    @Query(value = "SELECT DISTINCT prod FROM Produto prod INNER JOIN prod.categorias cat WHERE prod.nome LIKE %:nome% AND cat IN :categorias")
    Page<Produto> search(@Param("nome") String nome, @Param("categorias") List<Categoria> categorias, Pageable requisicaoPagina);

    @Transactional(readOnly = true)
    // Consulta utilizando o padrao de nomes do Spring data, documentacao disponivel em https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods
    Page<Produto> findDistinctByNomeContainingAndCategoriasIn(@Param("nome") String nome, @Param("categorias") List<Categoria> categorias, Pageable requisicaoPagina);

    /* OBSERVACAO IMPORTANTE:
        Os metodos acima realizam a mesma funcionalidade, estao declarados para exempliificar as formas diferentes de realizar consultas com o Spring Data, porem se um
        metodo que tenha sua assinatura com o padrao de nomes do Spring Data for anotado com @Query, a annotaition ira ter preferencia.

        EXEMPLO:
         @Query(value = "SELECT DISTINCT prod FROM Produto prod INNER JOIN prod.categorias cat WHERE prod.nome LIKE %:nome% AND cat IN :categorias")
         Page<Produto> findDistinctByNomeContainingAndCategoriasIn(@Param("nome") String nome, @Param("categorias") List<Categoria> categorias, Pageable requisicaoPagina);
     */

}
