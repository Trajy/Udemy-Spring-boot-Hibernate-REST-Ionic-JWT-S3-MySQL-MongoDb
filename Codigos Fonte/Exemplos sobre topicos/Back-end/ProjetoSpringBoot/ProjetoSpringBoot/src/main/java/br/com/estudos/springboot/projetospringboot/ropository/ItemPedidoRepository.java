package br.com.estudos.springboot.projetospringboot.ropository;

import br.com.estudos.springboot.projetospringboot.domain.ItemPedido;
import br.com.estudos.springboot.projetospringboot.domain.ItemPedidoPK;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, ItemPedidoPK> {
}
