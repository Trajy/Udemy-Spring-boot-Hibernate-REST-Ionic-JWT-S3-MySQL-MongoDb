package br.com.estudos.springboot.projetospringboot.domain.enums;

public enum TipoCliente {

    PESSOA_FISICA(1, "pessoa fisica"),
    PESSOA_JURIDICA(2, "pessoa juridica");

    private Integer id;

    private String descricao;

    private TipoCliente(Integer id, String descricao){
        this.id = id;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoCliente toEnum(Integer id){

        if(id.equals(null)) return null;

        for(TipoCliente x : TipoCliente.values()) if (x.getId() == id) return x;

        throw new IllegalArgumentException("id fornecido para tipo pessoa invalido");
    }

}
