import java.util.Objects;

public class Vertice {
    private String identificador;

    public Vertice(String identificador) {
        this.identificador = identificador;
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
        if (obj instanceof Vertice) {
            return  Objects.equals(this.identificador, ((Vertice) obj).identificador);
        } else {
            return false;
        }
    }
}