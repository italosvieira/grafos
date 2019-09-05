import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Grafo {
    private String identificador;
    private Map<Vertice, List<Aresta>> teste;

    public Grafo(String identificador) {
        this.identificador = identificador;
        this.teste = new HashMap<>();
    }

    public String getIdentificador() {
        return identificador;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.identificador);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof Grafo) {
            return  Objects.equals(this.identificador, ((Grafo) obj).identificador);
        } else {
            return false;
        }
    }
}