package br.com.estudos.springboot.projetospringboot.ropository;

import br.com.estudos.springboot.projetospringboot.domain.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
}
