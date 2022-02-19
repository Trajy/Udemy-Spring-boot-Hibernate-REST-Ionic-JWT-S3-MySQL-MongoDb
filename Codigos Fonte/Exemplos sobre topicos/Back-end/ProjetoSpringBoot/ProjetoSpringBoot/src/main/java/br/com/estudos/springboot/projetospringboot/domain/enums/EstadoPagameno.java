package br.com.estudos.springboot.projetospringboot.domain.enums;

public enum EstadoPagameno {

    PEDENTE(1, "pedente"),
    QUITADO(2, "quitado"),
    CANCELADO(3, "cancelado");

    private Integer id;
    private String descricao;

    EstadoPagameno(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public static EstadoPagameno toEnum(Integer id){

        for (EstadoPagameno x : EstadoPagameno.values()) if (x.getId() == id) return x;

        throw new IllegalArgumentException("id fornecido para EstadoPagamento invalido");
    }
}
