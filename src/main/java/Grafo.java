import java.util.*;
import java.util.stream.Collectors;

public class Grafo {
    private String identificador;
    private Boolean dirigido;//TODO talvez remover isso.
    private TreeMap<Vertice, List<Aresta>> grafo;

    public Grafo(String identificador) {
        this.identificador = identificador;
        this.grafo = new TreeMap<>();
    }

    public void adicionarVertice(Vertice v) {
        this.grafo.putIfAbsent(v, new ArrayList<>());
    }

    public void removerVertice(Vertice v) {
        if (this.grafo.containsKey(v)) {
            this.grafo.remove(v);

            this.obterVerticesAdjacentes(v).forEach(verticeAdjacente -> {
                this.grafo.get(verticeAdjacente).removeIf(a -> a.getVerticeOrigem().equals(v) || a.getVerticeDestino().equals(v));
            });
        }
    }

    public void adicionarAresta(Aresta a) {
        this.grafo.get(a.getVerticeOrigem()).add(a);
        this.grafo.get(a.getVerticeDestino()).add(a);
    }

    public void removerAresta(Aresta a) {
        this.grafo.get(a.getVerticeOrigem()).remove(a);
        this.grafo.get(a.getVerticeDestino()).remove(a);
    }

    public Set<Vertice> obterTodosOsVertices() {
        return this.grafo.keySet();
    }

    public Set<Aresta> obterTodasAsArestas() {
        return this.grafo.values().stream().flatMap(List::stream).collect(Collectors.toSet());
    }

    public Boolean existeArestaEntreVertices(Vertice v1, Vertice v2) {
        return this.grafo.get(v1).stream().anyMatch(a -> a.getVerticeOrigem().equals(v1) && a.getVerticeDestino().equals(v2)) ||
                this.grafo.get(v2).stream().anyMatch(a -> a.getVerticeOrigem().equals(v2) && a.getVerticeDestino().equals(v1));
    }

    public Set<Vertice> obterVerticesAdjacentes(Vertice v) {
        Set<Vertice> verticesAdjacentes = new HashSet<>();

        Set<Vertice> v1 = this.grafo.get(v).stream().map(Aresta::getVerticeOrigem).collect(Collectors.toSet());
        verticesAdjacentes.addAll(v1);

        Set<Vertice> v2 = this.grafo.get(v).stream().map(Aresta::getVerticeDestino).collect(Collectors.toSet());
        verticesAdjacentes.addAll(v2);

        verticesAdjacentes.remove(v);

        return verticesAdjacentes;
    }

    public Integer obterGrauDoVertice(Vertice v) {
        return this.grafo.get(v).size();
    }

    public List<String> obterGrausMinimoMedioMaximo() {
        List<Integer> graus = this.grafo.values().stream().mapToInt(List::size).boxed().collect(Collectors.toList());
        Set<Integer> setGraus = new HashSet<>(graus);

        List<String> retorno = new ArrayList<>();
        retorno.add("Grau Médio: " + (graus.stream().mapToInt(g -> g).sum() + graus.size()));
        retorno.add("Grau Mínimo: " + setGraus.stream().mapToInt(g -> g).min());
        retorno.add("Grau Máximo: " + setGraus.stream().mapToInt(g -> g).max());

        return retorno;
    }

    //TODO Qual a diferença entre um grafo ponderado e dirigido? Se tiver um vértice que só recebe a direção ele não conexo?
    public Boolean isGrafoConexo() {
        if (this.grafo == null || this.obterTodosOsVertices().isEmpty()) {
            return Boolean.FALSE;
        }

        if (this.isGrafoPonderado()) {
            return this.isGrafoPonderadoConexo();
        } else {
            return this.isGrafoDirigidoConexo();
        }
    }

    private Boolean isGrafoPonderado() {
        return this.obterTodasAsArestas().stream().noneMatch(a -> Boolean.TRUE.equals(a.getDirigida()));
    }

    private Boolean isGrafoDirigido() {
        return this.obterTodasAsArestas().stream().anyMatch(a -> Boolean.TRUE.equals(a.getDirigida()));
    }

    private Boolean isGrafoPonderadoConexo() {
        return this.grafo.values().stream().noneMatch(List::isEmpty);
    }

    private Boolean isGrafoDirigidoConexo() {
        Set<Aresta> arestas = this.obterTodasAsArestas();


        return this.grafo.values().stream().anyMatch(List::isEmpty);
    }

    /*private void exibirMatrizAdjacente() {
        StringJoiner matriz = new StringJoiner(System.lineSeparator());
        Set<Vertice> vertices = this.obterTodosOsVertices();

        for (Vertice linha: vertices) {
            StringBuilder matrizLinha = new StringBuilder(linha.getIdentificador() + " ");
            List<Aresta> arestasDoVerticeLinha = this.grafo.get(linha);

            //TODO Se tiver uma aresta com 2 vertices um direcionado e outro não. Qual que prevalece na criação da matriz adjacente?
            //TODO Se tiver vários vertice direcionado ligando os mesmos vertices? Como que representa em uma matriz adjacente?
            for (Vertice coluna: vertices) {
                Aresta arestaOrigemDestino = arestasDoVerticeLinha.stream().filter(a -> a.getVerticeOrigem().equals(linha) && a.getVerticeDestino().equals(coluna)).findFirst().orElse(null);
                Aresta arestaDestinoOrigem = arestasDoVerticeLinha.stream().filter(a -> a.getVerticeOrigem().equals(coluna) && a.getVerticeDestino().equals(linha)).findFirst().orElse(null);

                if (arestaOrigemDestino!= null) {
                    if (Boolean.TRUE.equals(arestaOrigemDestino.getDirigida())) {

                    } else {

                    }
                } else {
                    matriz.add("0 ");
                }
            }

            matriz.add(matrizLinha);
        }

        System.out.println(matriz);
    }*/

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