import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public class Aresta implements Serializable {

    @JsonIgnore
    private final UUID id;
    private String identificador;
    private Vertice verticeOrigem;
    private Vertice verticeDestino;
    private Integer peso;

    Aresta() {
        this.id = UUID.randomUUID();
    }

    Aresta(String identificador, Vertice verticeOrigem, Vertice verticeDestino, Integer peso) {
        this.id = UUID.randomUUID();
        this.identificador = identificador;
        this.verticeOrigem = verticeOrigem;
        this.verticeDestino = verticeDestino;
        this.peso = peso;
    }

    String getIdentificador() {
        return identificador;
    }

    Vertice getVerticeOrigem() {
        return verticeOrigem;
    }

    Vertice getVerticeDestino() {
        return verticeDestino;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public void setVerticeOrigem(Vertice verticeOrigem) {
        this.verticeOrigem = verticeOrigem;
    }

    public void setVerticeDestino(Vertice verticeDestino) {
        this.verticeDestino = verticeDestino;
    }

    public Integer getPeso() {
        return peso;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.identificador, this.verticeOrigem, this.verticeDestino);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Aresta) {
            final Aresta other = (Aresta) obj;
            return  Objects.equals(this.identificador, other.identificador) &&
                    Objects.equals(this.verticeOrigem, other.verticeOrigem) &&
                    Objects.equals(this.verticeDestino, other.verticeDestino);
        } else {
            return false;
        }
    }
}
