import java.util.Objects;
import java.util.UUID;

public class Aresta {
    private final UUID id;
    private final String identificador;
    private final Vertice verticeOrigem;
    private final Vertice verticeDestino;

    Aresta(String identificador, Vertice verticeOrigem, Vertice verticeDestino) {
        this.id = UUID.randomUUID();
        this.identificador = identificador;
        this.verticeOrigem = verticeOrigem;
        this.verticeDestino = verticeDestino;
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