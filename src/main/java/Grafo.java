import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class Grafo {
    private String identificador;
    private Boolean isDirigido;
    private TreeMap<Vertice, List<Aresta>> grafo;

    public Grafo(String identificador, Boolean isDirigido) {
        this.grafo = new TreeMap<>();
        this.isDirigido = isDirigido;
        this.identificador = identificador;
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

    public void obterGrausMinimoMedioMaximo() {
        List<Integer> graus = this.grafo.values().stream().mapToInt(List::size).boxed().collect(Collectors.toList());
        Set<Integer> setGraus = new HashSet<>(graus);
        StringJoiner sj = new StringJoiner(System.lineSeparator());

        this.obterGrauMedio(sj, graus);
        this.obterGrauMinimo(sj, setGraus);
        this.obterGrauMaximo(sj, setGraus);

        System.out.println(sj);
    }

    private void obterGrauMinimo(StringJoiner sj, Set<Integer> setGraus) {
        OptionalInt o = setGraus.stream().mapToInt(g -> g).min();

        if (o.isPresent()) {
            sj.add("Grau Mínimo: " + o.getAsInt());
        } else {
            sj.add("Grau Mínimo: Erro ao obter grau mínimo.");
        }
    }

    private void obterGrauMaximo(StringJoiner sj, Set<Integer> setGraus) {
        OptionalInt o = setGraus.stream().mapToInt(g -> g).max();

        if (o.isPresent()) {
            sj.add("Grau Máximo: " + o.getAsInt());
        } else {
            sj.add("Grau Máximo: Erro ao obter grau mínimo.");
        }
    }

    private void obterGrauMedio(StringJoiner sj, List<Integer> graus) {
        BigDecimal resultado = new BigDecimal(String.valueOf(graus.stream().mapToInt(g -> g).sum())).divide(new BigDecimal(String.valueOf(graus.size())), RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP);
        sj.add("Grau Médio: " + resultado.toPlainString());

    }

    public Boolean isGrafoConexo() {
        if (this.grafo == null || this.obterTodosOsVertices().isEmpty()) {
            return Boolean.FALSE;
        }

        return this.grafo.values().stream().noneMatch(List::isEmpty);
    }

    public Boolean existeCaminhoDeEuler() {
        Long quantidadeDeverticesDeGrauImpar = this.grafo.values().stream().mapToInt(List::size).filter(i -> i % 2 != 0).count();
        return this.isGrafoConexo() && (quantidadeDeverticesDeGrauImpar == 0L || quantidadeDeverticesDeGrauImpar == 2L);
    }

    public void exibirMatrizAdjacente() {
        if (Boolean.TRUE.equals(this.isDirigido)) {
            this.exibirMatrizAdjacenteGrafoDirigido();
        } else {
            this.exibirMatrizAdjacenteGrafoNaoDirigido();
        }
    }

    private void exibirMatrizAdjacenteGrafoDirigido() {
        //TODO
    }

    private void exibirMatrizAdjacenteGrafoNaoDirigido() {
        StringJoiner matriz = new StringJoiner(System.lineSeparator());
        Set<Vertice> vertices = this.obterTodosOsVertices();

        StringBuilder s1 = new StringBuilder("  ");
        vertices.forEach(v -> s1.append(v.getIdentificador()).append(" "));
        matriz.add(s1);

        vertices.forEach(verticeLinha -> {
            StringBuilder matrizLinha = new StringBuilder(verticeLinha.getIdentificador() + " ");
            List<Aresta> arestasDoVerticeLinha = this.grafo.get(verticeLinha);

            vertices.forEach(verticeColuna -> {
                boolean existeLigacao;

                if (verticeLinha.equals(verticeColuna)) {
                    existeLigacao = arestasDoVerticeLinha.stream().anyMatch(a -> a.getVerticeOrigem().equals(verticeLinha) && a.getVerticeDestino().equals(verticeColuna));
                } else {
                    existeLigacao = arestasDoVerticeLinha.stream().anyMatch(a -> a.getVerticeOrigem().equals(verticeColuna) || a.getVerticeDestino().equals(verticeColuna));
                }

                if (existeLigacao) {
                    matrizLinha.append("1 ");
                } else {
                    matrizLinha.append("0 ");
                }
            });

            matriz.add(matrizLinha);
        });

        System.out.println(matriz);
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