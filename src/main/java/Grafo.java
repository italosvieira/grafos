import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class Grafo {
    private final UUID id;
    private final String identificador;
    private final Boolean isDirigido;
    private final TreeMap<Vertice, List<Aresta>> grafo;

    Grafo(final String identificador, final Boolean isDirigido) {
        this.id = UUID.randomUUID();
        this.grafo = new TreeMap<>();
        this.isDirigido = isDirigido;
        this.identificador = identificador;
    }

    /**
     *
     *
     * @param v Novo vértice a ser adicionado no grafo.
     * @return void.
     * @throws GrafoException aaaaa.
     */
    void adicionarVertice(final Vertice v) {
        if (v == null) {
            throw new GrafoException(""); //TODO
        }

        this.grafo.putIfAbsent(v, new ArrayList<>());
    }

    /**
     *
     *
     * @param v Vértice a ser removido no grafo.
     * @return void
     * @throws GrafoException aaaaa.
     */
    void removerVertice(Vertice v) {
        if (v == null) {
            throw new GrafoException(""); //TODO
        }

        if (!this.grafo.containsKey(v)) {
            throw new GrafoException(""); //TODO
        }

        this.grafo.remove(v);
        this.obterVerticesAdjacentes(v).forEach(verticeAdjacente -> {
            this.grafo.get(verticeAdjacente).removeIf(a -> a.getVerticeOrigem().equals(v) || a.getVerticeDestino().equals(v));
        });
    }

    /**
     *
     *
     * @param a Vértice a ser removido no grafo.
     * @return void
     * @throws GrafoException aaaaa.
     */
    void adicionarAresta(Aresta a) {
        this.validarAresta(a);
        this.grafo.get(a.getVerticeOrigem()).add(a);
        this.grafo.get(a.getVerticeDestino()).add(a);
    }

    void removerAresta(Aresta a) {
        this.validarAresta(a);
        this.grafo.get(a.getVerticeOrigem()).remove(a);
        this.grafo.get(a.getVerticeDestino()).remove(a);
    }

    private void validarAresta(Aresta a) {
        if (a == null) {
            throw new GrafoException(""); //TODO
        }

        if (!this.grafo.containsKey(a.getVerticeOrigem())) {
            throw new GrafoException(""); //TODO
        }

        if (!this.grafo.containsKey(a.getVerticeDestino())) {
            throw new GrafoException(""); //TODO
        }
    }

    private Set<Vertice> obterTodosOsVertices() {
        return this.grafo.keySet();
    }

    String obterTodosOsVerticesAsString() {
        StringJoiner s = new StringJoiner(" | ");
        this.grafo.keySet().forEach(a -> s.add(a.getIdentificador()));
        return s.toString();
    }

    Vertice obterVerticePeloIdentificador(String identificador) {
        return this.grafo.keySet().stream().filter(vertice -> vertice.getIdentificador().equals(identificador)).findFirst().orElse(null);
    }

    Aresta obterArestaPeloIdentificador(String identificador) {
        return this.obterTodasAsArestas().stream().filter(vertice -> vertice.getIdentificador().equals(identificador)).findFirst().orElse(null);
    }

    String obterTodasAsArestasAsString() {
        StringJoiner s = new StringJoiner(" | ");
        this.obterTodasAsArestas().forEach(a -> s.add(a.getIdentificador()));
        return s.toString();
    }

    private Set<Aresta> obterTodasAsArestas() {
        return this.grafo.values().stream().flatMap(List::stream).collect(Collectors.toSet());
    }

    Boolean existeArestaEntreVertices(Vertice v1, Vertice v2) {
        return this.grafo.get(v1).stream().anyMatch(a -> a.getVerticeOrigem().equals(v1) && a.getVerticeDestino().equals(v2)) ||
                this.grafo.get(v2).stream().anyMatch(a -> a.getVerticeOrigem().equals(v2) && a.getVerticeDestino().equals(v1));
    }

    Set<Vertice> obterVerticesAdjacentes(Vertice v) {
        Set<Vertice> verticesAdjacentes = new HashSet<>();

        Set<Vertice> v1 = this.grafo.get(v).stream().map(Aresta::getVerticeOrigem).collect(Collectors.toSet());
        verticesAdjacentes.addAll(v1);

        Set<Vertice> v2 = this.grafo.get(v).stream().map(Aresta::getVerticeDestino).collect(Collectors.toSet());
        verticesAdjacentes.addAll(v2);

        verticesAdjacentes.remove(v);

        return verticesAdjacentes;
    }

    Integer obterGrauDoVertice(Vertice v) {
        return this.grafo.get(v).size();
    }

    void obterGrausMinimoMedioMaximo() {
        List<Integer> graus = this.grafo.values().stream().mapToInt(List::size).boxed().collect(Collectors.toList());

        if (graus.isEmpty()) {
            System.out.println("Não é possível gerar os graus sem arestas");
            return;
        }

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

    Boolean isGrafoConexo() {
        if (this.obterTodosOsVertices().isEmpty()) {
            return Boolean.FALSE;
        }

        return this.grafo.values().stream().noneMatch(List::isEmpty);
    }

    Boolean existeCaminhoDeEuler() {
        if (this.grafo.values().isEmpty()) {
            return Boolean.FALSE;
        }

        long quantidadeDeverticesDeGrauImpar = this.grafo.values().stream().mapToInt(List::size).filter(i -> i % 2 != 0).count();
        return this.isGrafoConexo() && (quantidadeDeverticesDeGrauImpar == 0L || quantidadeDeverticesDeGrauImpar == 2L);
    }

    void exibirMatrizAdjacente() {
        if (Boolean.TRUE.equals(this.isDirigido)) {
            this.exibirMatrizAdjacenteGrafoDirigido();
        } else {
            this.exibirMatrizAdjacenteGrafoNaoDirigido();
        }
    }

    private void exibirMatrizAdjacenteGrafoDirigido() {
        StringJoiner matriz = new StringJoiner(System.lineSeparator());
        Set<Vertice> vertices = this.obterTodosOsVertices();

        StringBuilder s1 = new StringBuilder("  ");
        vertices.forEach(v -> s1.append(v.getIdentificador()).append(" "));
        matriz.add(s1);

        vertices.forEach(verticeLinha -> {
            StringBuilder matrizLinha = new StringBuilder(verticeLinha.getIdentificador() + " ");
            List<Aresta> arestasDoVerticeLinha = this.grafo.get(verticeLinha);

            vertices.forEach(verticeColuna -> {
                if (arestasDoVerticeLinha.stream().anyMatch(a -> a.getVerticeOrigem().equals(verticeLinha) && a.getVerticeDestino().equals(verticeColuna))) {
                    matrizLinha.append("1 ");
                } else {
                    matrizLinha.append("0 ");
                }
            });

            matriz.add(matrizLinha);
        });

        System.out.println(matriz);
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