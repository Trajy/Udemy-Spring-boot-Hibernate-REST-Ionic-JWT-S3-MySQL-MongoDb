package br.com.estudos.springboot.projetospringboot.ropository;

import br.com.estudos.springboot.projetospringboot.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    /* O padrao de nomes findBy<nome-da-entidade> faz com que o spring
        implemente automaticamento a busca pelo campo desejado */
    @Transactional
    Cliente findByEmail(String email);

}
