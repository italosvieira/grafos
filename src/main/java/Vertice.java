import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Vertice implements Comparable<Vertice>, Serializable {

    @JsonIgnore
    private final UUID id;
    private final String identificador;

    Vertice(String identificador) {
        this.id = UUID.randomUUID();
        this.identificador = identificador;
    }

    String getIdentificador() {
        return identificador;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.identificador);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Vertice) {
            return  Objects.equals(this.identificador, ((Vertice) obj).identificador);
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Vertice outro) {
        return this.identificador.compareTo(outro.getIdentificador());
    }

    @Override
    @JsonValue
    public String toString() {
        return this.identificador;
    }
}