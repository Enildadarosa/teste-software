package br.ucdb.pos.engenhariasoftware.testesoftware.modelo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public enum Categoria {

    @JsonProperty("Alimentacao")
    ALIMENTACAO("Alimentacao"),
    @JsonProperty("Salario")
    SALARIO("Salario"),
    @JsonProperty("Lazer")
    LAZER("Lazer"),
    @JsonProperty("Telefone e Internet")
    TELEFONE_INTERNET("Telefone e Internet"),
    @JsonProperty("Carro")
    CARRO("Carro"),
    @JsonProperty("Emprestimo")
    EMPRESTIMO("Emprestimo"),
    @JsonProperty("Investimentos")
    INVESTIMENTOS("Investimentos"),
    @JsonProperty("Outros")
    OUTROS("Outros");

    @Getter
    private String nome;

    private Categoria(String nome){
        this.nome = nome;
    }
}
