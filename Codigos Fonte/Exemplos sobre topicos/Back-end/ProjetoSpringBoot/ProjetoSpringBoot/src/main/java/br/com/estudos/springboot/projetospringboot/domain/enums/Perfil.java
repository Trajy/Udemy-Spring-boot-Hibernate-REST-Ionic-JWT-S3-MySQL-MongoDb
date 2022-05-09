package br.com.estudos.springboot.projetospringboot.domain.enums;

public enum Perfil {

    // o fremework spring exige que os tipos devem possuir o profixo ROLE_
    ADM(1, "ROLE_ADMIN"),
    CLIENTE(2, "ROLE_CLIETE");

    private Integer id;
    private String descricao;

    Perfil(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Perfil toEnum(Integer id){

        for (Perfil x : Perfil.values()) if (x.getId() == id) return x;

        throw new IllegalArgumentException("id fornecido para EstadoPagamento invalido");
    }
}
