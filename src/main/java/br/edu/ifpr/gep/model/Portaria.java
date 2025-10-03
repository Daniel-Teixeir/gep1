package br.edu.ifpr.gep.model;

import java.time.LocalDate;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Portaria {
    private String emissor;
    private Integer numero;  // Sem acento
    private LocalDate publicacao;  // Sem acento
    private String membro;

    public Portaria() {}

    @JsonCreator
    public Portaria(@JsonProperty("emissor") String emissor,
                    @JsonProperty("numero") Integer numero,
                    @JsonProperty("publicacao") LocalDate publicacao,
                    @JsonProperty("membro") String membro) {
        this.emissor = emissor;
        this.numero = numero;
        this.publicacao = publicacao;
        this.membro = membro;
    }

    // Getters e Setters sem acentos
    public String getEmissor() { return emissor; }
    public void setEmissor(String emissor) { this.emissor = emissor; }

    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }

    public LocalDate getPublicacao() { return publicacao; }
    public void setPublicacao(LocalDate publicacao) { this.publicacao = publicacao; }

    public String getMembro() { return membro; }
    public void setMembro(String membro) { this.membro = membro; }

    @Override
    public String toString() {
        return "Portaria[emissor=" + emissor + ", numero=" + numero +
                ", publicacao=" + publicacao + ", membro=" + membro + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        if (this == obj) return true;
        Portaria other = (Portaria) obj;
        return Objects.equals(emissor, other.emissor) &&
                Objects.equals(numero, other.numero) &&
                Objects.equals(publicacao, other.publicacao) &&
                Objects.equals(membro, other.membro);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emissor, numero, publicacao, membro);
    }
}