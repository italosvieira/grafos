import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class GeradorDeGrafoAleatorio {
    private static final String FORMATO_DATA = "dd/MM/yyyy";
    private static final String SEPARADOR = " -- ";
    private static final String V = "v";
    private static final String A = "a";

    public static Grafo generate(final Boolean isDirigido, final Boolean isPonderado, final Integer qtdVertices) {
        Grafo g = new Grafo(new SimpleDateFormat(FORMATO_DATA).format(new Date()) + SEPARADOR + UUID.randomUUID().toString(), isDirigido, isPonderado);

        for (int i = 0; i < qtdVertices; i++) {
            g.adicionarVertice(new Vertice(V+i));
        }

        Random random = new Random();
        AtomicInteger countAresta = new AtomicInteger(0);

        for (int i = 0; i < qtdVertices; i++) {
            adicionarArestaEmVerticeAleatorio(g, qtdVertices, random, countAresta, i);
        }

        for (int i = qtdVertices - 1; i > 0; i--) {
            adicionarArestaEmVerticeAleatorioInvertido(g, qtdVertices, random, countAresta, i);
        }

        return g;
    }

    private static void adicionarArestaEmVerticeAleatorio(final Grafo grafo, final Integer qtdVertices, final Random random, final AtomicInteger countAresta, int i) {
        Vertice verticeOrigem = new Vertice(V+i);
        List<Aresta> arestas = grafo.obterArestasDoVertice(verticeOrigem);

        AtomicInteger numeroVerticeDestino = new AtomicInteger(random.nextInt(qtdVertices));
        List<Aresta> arestasDestino = grafo.obterArestasDoVertice(new Vertice(V+numeroVerticeDestino.get()));
        boolean x = arestas.stream().anyMatch(a -> a.getVerticeOrigem().equals(new Vertice(V+numeroVerticeDestino.get())) ||
                                                   a.getVerticeDestino().equals(new Vertice(V+numeroVerticeDestino.get())));

        while(x || arestasDestino.size() >= 2 || verticeOrigem.getIdentificador().equalsIgnoreCase(V+numeroVerticeDestino.get())) {
            numeroVerticeDestino.set(random.nextInt(qtdVertices));
            arestasDestino = grafo.obterArestasDoVertice(new Vertice(V+numeroVerticeDestino.get()));
            x = arestas.stream().anyMatch(a -> a.getVerticeOrigem().equals(new Vertice(V+numeroVerticeDestino.get())) ||
                                               a.getVerticeDestino().equals(new Vertice(V+numeroVerticeDestino.get())));
        }

        grafo.adicionarAresta(new Aresta(A+countAresta.getAndIncrement(), verticeOrigem, new Vertice(V+numeroVerticeDestino.get()), (float) random.nextInt(100)));
    }

    private static void adicionarArestaEmVerticeAleatorioInvertido(final Grafo grafo, final Integer qtdVertices, final Random random, final AtomicInteger countAresta, int i) {
        Vertice verticeOrigem = new Vertice(V+i);
        List<Aresta> arestas = grafo.obterArestasDoVertice(verticeOrigem);

        AtomicInteger numeroVerticeDestino = new AtomicInteger(random.nextInt(qtdVertices));
        List<Aresta> arestasDestino = grafo.obterArestasDoVertice(new Vertice(V+numeroVerticeDestino.get()));
        boolean x = arestas.stream().anyMatch(a -> a.getVerticeOrigem().equals(new Vertice(V+numeroVerticeDestino.get())) ||
                                                  a.getVerticeDestino().equals(new Vertice(V+numeroVerticeDestino.get())));

        while(x || arestasDestino.size() >= 4 || verticeOrigem.getIdentificador().equalsIgnoreCase(V+numeroVerticeDestino.get())) {
            numeroVerticeDestino.set(random.nextInt(qtdVertices));
            arestasDestino = grafo.obterArestasDoVertice(new Vertice(V+numeroVerticeDestino.get()));
            x = arestas.stream().anyMatch(a -> a.getVerticeOrigem().equals(new Vertice(V+numeroVerticeDestino.get())) ||
                    a.getVerticeDestino().equals(new Vertice(V+numeroVerticeDestino.get())));
        }

        grafo.adicionarAresta(new Aresta(A+countAresta.getAndIncrement(), verticeOrigem, new Vertice(V+numeroVerticeDestino.get()), (float) random.nextInt(100)));
    }
}
