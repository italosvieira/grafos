import java.util.*;
import java.util.stream.Collectors;

public class Grafo {
    private String identificador;
    private Map<Vertice, List<Aresta>> teste;

    public Grafo(String identificador) {
        this.identificador = identificador;
        this.teste = new HashMap<>();
    }

    public void adicionarVertice(Vertice v) {
        this.teste.putIfAbsent(v, new ArrayList<>());
    }

    public void removerVertice(Vertice v) {
        if (this.teste.containsKey(v)) {
            this.teste.remove(v);

            this.obterVerticesAdjacentes(v).forEach(verticeAdjacente -> {
                this.teste.get(verticeAdjacente).removeIf(a -> a.getVerticeOrigem().equals(v) || a.getVerticeDestino().equals(v));
            });
        }
    }

    public void adicionarAresta(Aresta a) {
        this.teste.get(a.getVerticeOrigem()).add(a);
        this.teste.get(a.getVerticeDestino()).add(a);
    }

    public void removerAresta(Aresta a) {
        this.teste.get(a.getVerticeOrigem()).remove(a);
        this.teste.get(a.getVerticeDestino()).remove(a);
    }

    public Set<Vertice> obterTodosOsVertices() {
        return this.teste.keySet();
    }

    public Set<Aresta> obterTodasAsArrestas() {
        return this.teste.values().stream().flatMap(List::stream).collect(Collectors.toSet());
    }

    public Boolean existeArestaEntreVertices(Vertice v1, Vertice v2) {
        return this.teste.get(v1).stream().anyMatch(a -> a.getVerticeOrigem().equals(v1) && a.getVerticeDestino().equals(v2)) ||
                this.teste.get(v2).stream().anyMatch(a -> a.getVerticeOrigem().equals(v2) && a.getVerticeDestino().equals(v1));
    }

    public Set<Vertice> obterVerticesAdjacentes(Vertice v) {
        Set<Vertice> verticesAdjacentes = new HashSet<>();

        Set<Vertice> v1 = this.teste.get(v).stream().map(Aresta::getVerticeOrigem).collect(Collectors.toSet());
        verticesAdjacentes.addAll(v1);

        Set<Vertice> v2 = this.teste.get(v).stream().map(Aresta::getVerticeDestino).collect(Collectors.toSet());
        verticesAdjacentes.addAll(v2);

        verticesAdjacentes.remove(v);

        return verticesAdjacentes;
    }

    public Integer obterGrauDoVertice(Vertice v) {
        return this.teste.get(v).size();
    }

    public List<String> obterGrausMinimoMedioMaximo() {
        List<Integer> graus = this.teste.values().stream().mapToInt(List::size).boxed().collect(Collectors.toList());
        Set<Integer> setGraus = new HashSet<>(graus);

        List<String> retorno = new ArrayList<>();
        retorno.add("Grau Médio: " + (graus.stream().mapToInt(g -> g).sum() + graus.size()));
        retorno.add("Grau Mínimo: " + setGraus.stream().mapToInt(g -> g).min());
        retorno.add("Grau Máximo: " + setGraus.stream().mapToInt(g -> g).max());

        return retorno;
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