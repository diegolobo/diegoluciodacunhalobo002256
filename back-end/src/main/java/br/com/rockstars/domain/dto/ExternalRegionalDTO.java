package br.com.rockstars.domain.dto;

public class ExternalRegionalDTO {

    private Long id;
    private String nome;

    public ExternalRegionalDTO() {
    }

    public ExternalRegionalDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
