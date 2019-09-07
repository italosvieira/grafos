public class Main {
    public static void main(String[] args) {
        Grafo g = new Grafo("Teste");
        Vertice v1 = new Vertice("v1");
        Vertice v2 = new Vertice("v2");
        g.adicionarVertice(v1);
        g.adicionarVertice(v2);

        Aresta a = new Aresta("A1", v1, v2, false);
        g.adicionarAresta(a);

        g.existeArestaEntreVertices(new Vertice("v2"), new Vertice("v2"));
    }
}