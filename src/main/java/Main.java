import org.apache.log4j.Logger;

import java.util.Scanner;
import java.util.Set;

public class Main {
    private final static Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Grafo g = adicionarGrafo(scanner);

        while (true) {
            imprimirOpcoes();

            switch (scanner.next()) {
                case "addVertice": adicionarVertice(g, scanner); break;
                case "rmVertice": removerVertice(g, scanner); break;
                case "addAresta": adicionarAresta(g, scanner); break;
                case "rmAresta": removerAresta(g, scanner); break;
                case "graus": g.obterGrausMinimoMedioMaximo(); break;
                case "matriz": g.exibirMatrizAdjacente(); break;
                case "euler": existeCaminhoDeEuler(g); break;
                case "existeVertice": existeArestaEntreVertices(g, scanner); break;
                case "conexo": isConexo(g); break;
                case "grauVertice": exibeGrauDoVertice(g, scanner); break;
                case "verticeAdjacente": exibeVerticesAdjacentes(g, scanner); break;
                case "exit": System.exit(0);
                default: break;
            }
        }
    }

    private static void imprimirOpcoes() {
        LOGGER.info("Possiveis commandos:\n" +
                    "addVertice             Adiciona um novo vértice.\n" +
                    "rmVertice              Remove um vértice.\n" +
                    "addAresta              Adiciona uma nova aresta.\n" +
                    "rmAresta               Remove uma aresta.\n" +
                    "graus                  Mostra o grau mínimo, médio e máximo.\n" +
                    "matriz                 Mostra a matriz adjacente.\n" +
                    "euler                  Mostra se existe caminho de euler.\n" +
                    "existeVertice          Mostra se existe vértice entre arestas.\n" +
                    "conexo                 Mostra se o grafo é conexo.\n" +
                    "grauVertice            Mostra o grau de um determinado vérrtice.\n" +
                    "verticeAdjacente       Mostra os vértices adjacentes a um determinado vérrtice.\n" +
                    "exit                   Finaliza a aplicação.");
    }

    private static Grafo adicionarGrafo(Scanner scanner) {
        LOGGER.info("Digite o identificador do grafo e depois se é dirigido ou não (true ou false):");
        return new Grafo(scanner.next(), scanner.nextBoolean());
    }

    private static void adicionarVertice(Grafo g, Scanner scanner) {
        LOGGER.info("Digite o identificador do novo vertice:");
        g.adicionarVertice(new Vertice(scanner.next()));
    }

    private static void adicionarAresta(Grafo g, Scanner scanner) {
        LOGGER.info("Vértices já existentes: " + g.obterTodosOsVerticesAsString());

        LOGGER.info("Digite o identificador do vértices de origem:");
        Vertice verticeOrigem = g.obterVerticePeloIdentificador(scanner.next());

        if (verticeOrigem == null) {
            LOGGER.info("Não foi possível encontrar o vértices de origem informado. Tente adicionar a aresta novamente.");
            return;
        }

        LOGGER.info("Digite o identificador do vértices de destino:");
        Vertice verticeDestino = g.obterVerticePeloIdentificador(scanner.next());

        if (verticeDestino == null) {
            LOGGER.info("Não foi possível encontrar o vértices de destino informado. Tente adicionar a aresta novamente.");
            return;
        }

        LOGGER.info("Digite o identificador da nova aresta:");
        g.adicionarAresta(new Aresta(scanner.next(), verticeOrigem, verticeDestino));
    }

    private static void removerVertice(Grafo g, Scanner scanner) {
        LOGGER.info("Vértices já existentes: " + g.obterTodosOsVerticesAsString());
        LOGGER.info("Digite o identificador do vértices á ser removido:");
        g.removerVertice(g.obterVerticePeloIdentificador(scanner.next()));
    }

    private static void removerAresta(Grafo g, Scanner scanner) {
        LOGGER.info("Arestas já existentes: " + g.obterTodasAsArestasAsString());
        LOGGER.info("Digite o identificador da aresta á ser removida:");
        g.removerAresta(g.obterArestaPeloIdentificador(scanner.next()));
    }

    private static void existeArestaEntreVertices(Grafo g, Scanner scanner) {
        LOGGER.info("Vértices já existentes: " + g.obterTodosOsVerticesAsString());

        LOGGER.info("Digite o identificador do vértice de origem:");
        Vertice verticeOrigem = g.obterVerticePeloIdentificador(scanner.next());

        LOGGER.info("Digite o identificador do vértice de destino:");
        Vertice verticeDestino = g.obterVerticePeloIdentificador(scanner.next());

        if (Boolean.TRUE.equals(g.existeArestaEntreVertices(verticeOrigem, verticeDestino))) {
            LOGGER.info("Existe aresta entre os vértices.");
        } else {
            LOGGER.info("Não existe aresta entre os vértices.");
        }
    }

    private static void isConexo(Grafo g) {
        if (Boolean.TRUE.equals(g.isGrafoConexo())) {
            LOGGER.info("Sim, o grafo é conexo.");
        } else {
            LOGGER.info("Não, o grafo não é conexo.");
        }
    }

    private static void exibeGrauDoVertice(Grafo g, Scanner scanner) {
        LOGGER.info("Vértices já existentes: " + g.obterTodosOsVerticesAsString());

        LOGGER.info("Digite o identificador do vértice que deseja saber o grau:");
        LOGGER.info("O grau do vértice é: " + g.obterGrauDoVertice(g.obterVerticePeloIdentificador(scanner.next())));
    }

    private static void exibeVerticesAdjacentes(Grafo g, Scanner scanner) {
        LOGGER.info("Vértices já existentes: " + g.obterTodosOsVerticesAsString());

        LOGGER.info("Digite o identificador do vértice que deseja saber os vértices adjacentes:");
        Set<Vertice> verticesAdjacentes = g.obterVerticesAdjacentes(g.obterVerticePeloIdentificador(scanner.next()));

        verticesAdjacentes.forEach(v -> LOGGER.info(v.getIdentificador()));
    }

    private static void existeCaminhoDeEuler(Grafo g) {
        if (Boolean.TRUE.equals(g.existeCaminhoDeEuler())) {
            LOGGER.info("Sim, existe caminho de euler.");
        } else {
            LOGGER.info("Não existe caminho de euler.");
        }
    }
}