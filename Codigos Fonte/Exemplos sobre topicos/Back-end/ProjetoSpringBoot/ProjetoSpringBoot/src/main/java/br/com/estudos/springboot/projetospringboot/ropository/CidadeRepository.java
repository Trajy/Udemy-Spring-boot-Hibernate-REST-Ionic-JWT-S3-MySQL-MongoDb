package br.com.estudos.springboot.projetospringboot.ropository;

import br.com.estudos.springboot.projetospringboot.domain.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer> {
}
