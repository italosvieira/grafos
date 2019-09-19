public class Main {
    public static void main(String[] args) {
        Grafo g = new Grafo("g1", false);
        Vertice v1 = new Vertice("v1");
        Vertice v2 = new Vertice("v2");
        Aresta a1 = new Aresta("a1", v2, v1);
        Aresta a2 = new Aresta("a2", v1, v2);

        g.adicionarVertice(v1);
        g.adicionarVertice(v2);
        g.adicionarAresta(a1);
        g.adicionarAresta(a2);

        g.obterGrausMinimoMedioMaximo();
        g.exibirMatrizAdjacente();
    }
}