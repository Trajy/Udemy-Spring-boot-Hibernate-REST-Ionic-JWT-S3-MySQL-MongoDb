package br.com.estudos.springboot.projetospringboot.domain;

import br.com.estudos.springboot.projetospringboot.domain.comum.EntidadeComum;
import br.com.estudos.springboot.projetospringboot.domain.enums.EstadoPagameno;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pagamento extends EntidadeComum {

    private Integer estadoPagamento;

    //@JsonBackReference
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "pedido_id")
    @MapsId
    private Pedido pedido;

    public Pagamento(){
    }

    public Pagamento(Integer id, EstadoPagameno estadoPagamento, Pedido pedido) {
        this.id = id;
        this.estadoPagamento = (estadoPagamento == null) ? null : estadoPagamento.getId();
        this.pedido = pedido;
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
