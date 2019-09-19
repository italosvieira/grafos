import org.apache.log4j.Logger;

import java.util.Scanner;

public class Main {
    private final static Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Grafo g = adicionarGrafo(scanner);

        while (true) {
            imprimirOpcoes();

            switch (scanner.next()) {
                case "addVertice": adicionarVertice(g, scanner); break;
                case "addAresta": adicionarAresta(g, scanner); break;
                case "exit": System.exit(0);
                default: break;
            }
        }
    }

    private static void imprimirOpcoes() {
        LOGGER.info("Possiveis commandos:\n" +
                    "addVertice     Adiciona um novo vértice.\n" +
                    "addAresta      Adiciona uma nova aresta.\n" +
                    "exit           Finaliza a aplicação.");
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
        LOGGER.info("Digite o identificador do vertice de origem:");
        Vertice verticeOrigem = g.obterVerticePeloIdentificador(scanner.next());

        if (verticeOrigem == null) {
            LOGGER.info("Não foi possível encontrar o vertice de origem informado. Tente adicionar a aresta novamente.");
            return;
        }

        LOGGER.info("Digite o identificador do vertice de destino:");
        Vertice verticeDestino = g.obterVerticePeloIdentificador(scanner.next());

        if (verticeDestino == null) {
            LOGGER.info("Não foi possível encontrar o vertice de destino informado. Tente adicionar a aresta novamente.");
            return;
        }

        LOGGER.info("Digite o identificador da nova aresta:");
        g.adicionarAresta(new Aresta(scanner.next(), verticeOrigem, verticeDestino));
    }
}