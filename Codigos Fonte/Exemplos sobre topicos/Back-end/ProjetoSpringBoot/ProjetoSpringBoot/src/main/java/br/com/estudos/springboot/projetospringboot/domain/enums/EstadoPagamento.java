package br.com.estudos.springboot.projetospringboot.domain.enums;

public enum EstadoPagamento {

    PENDENTE(1, "pedente"),
    QUITADO(2, "quitado"),
    CANCELADO(3, "cancelado");

    private Integer id;
    private String descricao;

    EstadoPagamento(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EstadoPagamento toEnum(Integer id){

        for (EstadoPagamento x : EstadoPagamento.values()) if (x.getId() == id) return x;

        throw new IllegalArgumentException("id fornecido para EstadoPagamento invalido");
    }
}
