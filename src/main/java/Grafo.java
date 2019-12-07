import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Grafo implements Serializable {

    private static final long serialVersionUID = -6977015641015205734L;

    @JsonIgnore
    private final UUID id;
    private String identificador;
    private Boolean dirigido;
    private Boolean ponderado;
    private TreeMap<Vertice, List<Aresta>> grafo;

    Grafo() {
        this.id = UUID.randomUUID();
    }

    Grafo(final String identificador, final Boolean dirigido, final Boolean ponderado) {
        this.id = UUID.randomUUID();
        this.grafo = new TreeMap<>();
        this.identificador = identificador;
        this.dirigido = dirigido;
        this.ponderado = ponderado;
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

        Set<String> verticesVisitados = new HashSet<>();
        Map.Entry<Vertice, List<Aresta>> firstEntry = this.grafo.firstEntry();

        this.buscaEmProfundidade(verticesVisitados, firstEntry.getKey(), firstEntry.getValue());

        return verticesVisitados.containsAll(this.grafo.keySet().stream()
                .map(Vertice::getIdentificador)
                .collect(Collectors.toSet()));
    }

    private void buscaEmProfundidade(Set<String> verticesVisitados, Vertice vertice, List<Aresta> arestas) {
        //Se o vértice já foi visitado retorna.
        if (verticesVisitados.contains(vertice.getIdentificador())) {
            return;
        }

        //Marca o vértice atual como visitado.
        verticesVisitados.add(vertice.getIdentificador());

        //Chama recursivamente a busca em profundidade para cada vértice de origem e destino.
        arestas.forEach(a -> {
            buscaEmProfundidade(verticesVisitados, a.getVerticeOrigem(), this.grafo.get(a.getVerticeOrigem()));
            buscaEmProfundidade(verticesVisitados, a.getVerticeDestino(), this.grafo.get(a.getVerticeDestino()));
        });
    }

    Boolean existeCaminhoDeEuler() {
        if (this.grafo.values().isEmpty()) {
            return Boolean.FALSE;
        }

        long quantidadeDeverticesDeGrauImpar = this.grafo.values().stream().mapToInt(List::size).filter(i -> i % 2 != 0).count();
        return this.isGrafoConexo() && (quantidadeDeverticesDeGrauImpar == 0L || quantidadeDeverticesDeGrauImpar == 2L);
    }

    void exibirMatrizAdjacente() {
        if (Boolean.TRUE.equals(this.dirigido)) {
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

    private Integer obterNumeroDeVertices() {
        return this.grafo.keySet().size();
    }

    public Boolean[][] obterMatrizAdjacenteBooleana() {
        Set<Vertice> vertices = this.obterTodosOsVertices();
        Boolean[][] matriz = new Boolean[vertices.size()][vertices.size()];

        AtomicInteger i = new AtomicInteger(0);

        vertices.forEach(verticeLinha -> {
            AtomicInteger j = new AtomicInteger(0);
            List<Aresta> arestasDoVerticeLinha = this.grafo.get(verticeLinha);

            vertices.forEach(verticeColuna -> matriz[i.get()][j.getAndIncrement()] = arestasDoVerticeLinha.stream().anyMatch(a -> a.getVerticeOrigem().equals(verticeLinha) && a.getVerticeDestino().equals(verticeColuna)));
            i.getAndIncrement();
        });

        return matriz;
    }

    // Algoritimo de warshall. Esse algoritimo utiliza a matriz de acessibilidade
    // para gerar uma matriz de acessibilidade completa.
    public void exibirMatrizDeAcessibilidade() {
        Integer numeroDeVertices = this.obterNumeroDeVertices();
        Boolean[][] matriz = this.obterMatrizAdjacenteBooleana();

        for (int k = 0; k < numeroDeVertices; k++) {
            for (int i = 0; i < numeroDeVertices; i++) {
                for (int j = 0; j < numeroDeVertices; j++) {
                    matriz[i][j] = matriz[i][j] || (matriz[i][k] && matriz[k][j]);
                }
            }
        }

        this.imprimeMatrizBooleana(matriz);
    }

    private void imprimeMatrizBooleana(Boolean[][] matriz) {
        Arrays.stream(matriz).forEach(row -> {
            System.out.print("[");
            Arrays.stream(row).forEach((b) -> System.out.print(" " + this.converteBooleanParaBinario(b) + " "));
            System.out.println("]");
        });
    }

    public void bellmanFord(Vertice origem) {
        Integer numeroDeVertices = this.obterNumeroDeVertices();

        Float[] distancia = new Float[numeroDeVertices];
        String[] predecessor = new String[numeroDeVertices];
        LinkedList<Vertice> vertices = new LinkedList<>(this.grafo.keySet());
        LinkedList<Aresta> arestas = new LinkedList<>(this.obterTodasAsArestas());

        for (int i = 0; i < numeroDeVertices; i++) {
            distancia[i] = Float.POSITIVE_INFINITY;
            predecessor[i] = "";
        }

        distancia[vertices.indexOf(origem)] = (float) 0;

        for (int i = 1; i < numeroDeVertices; ++i) {
            for (Aresta aresta : arestas) {
                Float peso = aresta.getPeso();
                int indexVerticeOrigem = vertices.indexOf(aresta.getVerticeOrigem());
                int indexVerticeDestino = vertices.indexOf(aresta.getVerticeDestino());

                if (distancia[indexVerticeOrigem] != Float.POSITIVE_INFINITY && distancia[indexVerticeOrigem] + peso < distancia[indexVerticeDestino]) {
                    distancia[indexVerticeDestino] = distancia[indexVerticeOrigem] + peso;
                }
            }
        }

        //Checa por ciclo de pesos negativos.
        for (Aresta aresta : arestas) {
            Float peso = aresta.getPeso();
            int indexVerticeOrigem = vertices.indexOf(aresta.getVerticeOrigem());
            int indexVerticeDestino = vertices.indexOf(aresta.getVerticeDestino());

            if (distancia[indexVerticeOrigem] != Float.POSITIVE_INFINITY && distancia[indexVerticeOrigem] + peso < distancia[indexVerticeDestino]) {
                System.out.println("Grafo contem ciclo negativo de pesos.");
                return;
            }
        }

        for (int i = 0; i < numeroDeVertices; ++i) {
            System.out.println("Origem: " + origem.getIdentificador() + ". Destino: " + vertices.get(i) + ". Peso: " + String.format("%.0f", distancia[i]));
        }
    }

    public Float[][] obterMatrizAdjacente() {
        Set<Vertice> vertices = this.obterTodosOsVertices();
        Float[][] matriz = new Float[vertices.size()][vertices.size()];

        AtomicInteger i = new AtomicInteger(0);

        vertices.forEach(verticeLinha -> {
            AtomicInteger j = new AtomicInteger(0);
            List<Aresta> arestasDoVerticeLinha = this.grafo.get(verticeLinha);

            vertices.forEach(verticeColuna -> {
                if (verticeLinha.equals(verticeColuna)) {
                    matriz[i.get()][j.getAndIncrement()] = (float) 0;
                } else {
                    if (Boolean.TRUE.equals(this.dirigido)) {
                        Aresta aresta = arestasDoVerticeLinha.stream()
                            .filter(a -> a.getVerticeOrigem().equals(verticeLinha) && a.getVerticeDestino().equals(verticeColuna))
                            .findAny()
                            .orElse(null);
                        matriz[i.get()][j.getAndIncrement()] = aresta != null ? aresta.getPeso() : Float.POSITIVE_INFINITY;
                    } else {
                        Aresta aresta = arestasDoVerticeLinha.stream()
                            .filter(a -> a.getVerticeOrigem().equals(verticeColuna) && a.getVerticeDestino().equals(verticeColuna))
                            .findAny()
                            .orElse(null);
                        matriz[i.get()][j.getAndIncrement()] = aresta != null ? aresta.getPeso() : Float.POSITIVE_INFINITY;
                    }
                }
            });
            i.getAndIncrement();
        });

        return matriz;
    }

    // Algoritimo de warshall. Esse algoritimo utiliza a matriz de acessibilidade
    // para gerar uma matriz de acessibilidade completa.
    public void floydWarshall() {
        Integer numeroDeVertices = this.obterNumeroDeVertices();
        Float[][] matriz = this.obterMatrizAdjacente();

        for (int k = 0; k < numeroDeVertices; k++) {
            for (int i = 0; i < numeroDeVertices; i++) {
                for (int j = 0; j < numeroDeVertices; j++) {
                    matriz[i][j] = (matriz[i][j] > (matriz[i][k] + matriz[k][j])) ? (matriz[i][k] + matriz[k][j]) : matriz[i][j];
                }
            }
        }

        this.imprimeMatrizComPesos(matriz);
    }

    private void imprimeMatrizComPesos(Float[][] matriz) {
        Arrays.stream(matriz).forEach(row -> {
            System.out.print("[");
            Arrays.stream(row).forEach((b) -> System.out.print(" " + String.format("%.0f", b) + " "));
            System.out.println("]");
        });
    }

    private String converteBooleanParaBinario(Boolean b) {
        return Boolean.TRUE.equals(b) ? "1" : "0";
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public Boolean getDirigido() {
        return dirigido;
    }

    public void setDirigido(Boolean dirigido) {
        this.dirigido = dirigido;
    }

    public TreeMap<Vertice, List<Aresta>> getGrafo() {
        return grafo;
    }

    public void setGrafo(TreeMap<Vertice, List<Aresta>> grafo) {
        this.grafo = grafo;
    }

    public Boolean getPonderado() {
        return ponderado;
    }

    public void setPonderado(Boolean ponderado) {
        this.ponderado = ponderado;
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
