import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Aresta {
    private String identificador;
    private Vertice verticeOrigem;
    private Vertice verticeDestino;
    private Boolean dirigida;

    Aresta(String identificador, Vertice verticeOrigem, Vertice verticeDestino, Boolean dirigida) {
        this.identificador = identificador;
        this.verticeOrigem = verticeOrigem;
        this.verticeDestino = verticeDestino;
        this.dirigida = dirigida;
    }

    public Set<Vertice> obterVertices() {
        return new HashSet<>(Arrays.asList(this.verticeOrigem, this.verticeDestino));
    }

    public String getIdentificador() {
        return identificador;
    }

    public Vertice getVerticeOrigem() {
        return verticeOrigem;
    }

    public void setVerticeOrigem(Vertice verticeOrigem) {
        this.verticeOrigem = verticeOrigem;
    }

    public Vertice getVerticeDestino() {
        return verticeDestino;
    }

    public void setVerticeDestino(Vertice verticeDestino) {
        this.verticeDestino = verticeDestino;
    }

    public Boolean getDirigida() {
        return dirigida;
    }

    public void setDirigida(Boolean dirigida) {
        this.dirigida = dirigida;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.identificador, this.verticeOrigem, this.verticeDestino, this.dirigida);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Aresta) {
            final Aresta other = (Aresta) obj;
            return  Objects.equals(this.identificador, other.identificador) &&
                    Objects.equals(this.verticeOrigem, other.verticeOrigem) &&
                    Objects.equals(this.verticeDestino, other.verticeDestino) &&
                    Objects.equals(this.dirigida, other.dirigida);
        } else {
            return false;
        }
    }
}