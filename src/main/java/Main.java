import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
 
import java.io.File;
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
                case "export": export(g); break;
                case "warshall": warshall(g); break;
                case "bellmanFord": bellmanFord(g, scanner); break;
                case "floydWarshall": floydWarshall(g); break;
                case "dijkstra": dijkstra(g, scanner); break;
                case "compararAlgoritimos": compararAlgoritimos(g); break;
                case "identificarComponentesNoGrafo": identificarComponentesNoGrafo(g); break;
                case "exit": System.exit(0);
                default: break;
            }
        }
    }

    private static void imprimirOpcoes() {
        LOGGER.info("Possiveis commandos:\n" +
                    "addVertice                       Adiciona um novo vértice.\n" +
                    "rmVertice                        Remove um vértice.\n" +
                    "addAresta                        Adiciona uma nova aresta.\n" +
                    "rmAresta                         Remove uma aresta.\n" +
                    "graus                            Mostra o grau mínimo, médio e máximo.\n" +
                    "matriz                           Mostra a matriz adjacente.\n" +
                    "euler                            Mostra se existe caminho de euler.\n" +
                    "existeVertice                    Mostra se existe vértice entre arestas.\n" +
                    "conexo                           Mostra se o grafo é conexo.\n" +
                    "grauVertice                      Mostra o grau de um determinado vérrtice.\n" +
                    "verticeAdjacente                 Mostra os vértices adjacentes a um determinado vérrtice.\n" +
                    "warshall                         Exibe a matriz de acessibilidade pelo algoritimo de warshall.\n" +
                    "bellmanFord                      Exibe a matriz de menores caminhos.\n" +
                    "floydWarshall                    Exibe a matriz de menores caminhos pelo algoritimo de floyd-warshall.\n" +
                    "dijkstra                         Exibe os menores caminhos do vértice origem pelo algoritimo de dijkstra.\n" +
                    "compararAlgoritimos              Compara o tempo de execução dos algoritimos.\n" +
                    "identificarComponentesNoGrafo    Compara o tempo de execução dos algoritimos.\n" +
                    "exit                             Finaliza a aplicação.");
    }

    private static Grafo adicionarGrafo(Scanner scanner) {
        LOGGER.info("Deseja importar um arquivo(true)?");

        if (Boolean.TRUE.equals(scanner.nextBoolean())) {
            return imports(scanner);
        }

        LOGGER.info("Deseja gerar um grafo aleatório(true)? ou criar o grafo em memória(false)?");

        if (Boolean.TRUE.equals(scanner.nextBoolean())) {
            LOGGER.info("Digite se o grafo é dirigido ou não(true ou false):");
            Boolean isDirigido = scanner.nextBoolean();

            LOGGER.info("Digite se o grafo é ponderado ou não(true ou false):");
            Boolean isPonderado = scanner.nextBoolean();

            LOGGER.info("Digite se as arestas podem ter peso negativo ou não(true ou false):");
            Boolean pesoNegativo = scanner.nextBoolean();

            LOGGER.info("Digite a quantidade de vértices que deseja:");
            Integer qtdVertices = scanner.nextInt();

            Grafo g = GeradorDeGrafoAleatorio.generate(isDirigido, isPonderado, qtdVertices, pesoNegativo);
            LOGGER.info("O grafo gerado foi o: " + g.getIdentificador());
            return g;
        }

        LOGGER.info("Digite o identificador do grafo:");
        String identificador = scanner.next();

        LOGGER.info("Digite se o grafo é dirigido ou não(true ou false):");
        Boolean dirigido = scanner.nextBoolean();

        LOGGER.info("Digite se o grafo é ponderado ou não(true ou false):");
        Boolean ponderado = scanner.nextBoolean();

        return new Grafo(identificador, dirigido, ponderado);
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

        Float peso = null;

        if (Boolean.TRUE.equals(g.getPonderado())) {
            LOGGER.info("Digite o peso da aresta:");

            try {
                peso = Float.valueOf(scanner.next());
            } catch (Exception e) {
                LOGGER.info("Erro ao converter o peso para bigdecimal. Peso inválido.");
                return;
            }
        }

        LOGGER.info("Digite o identificador da nova aresta:");
        g.adicionarAresta(new Aresta(scanner.next(), verticeOrigem, verticeDestino, peso));
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

    private static void export(Grafo g) {
        try {
            getObjectMapper().writeValue(new File("resultado.json"), g);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

    private static Grafo imports(Scanner scanner) {
        LOGGER.info("Possiveis arquivos: floydWarshall, bellmanFord, grafoDesconexo, dijkstra.");
        LOGGER.info("Digite o nome do arquivo:");

        try {
            ObjectMapper mapper = new ObjectMapper();
            getObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            return mapper.readValue(Thread.currentThread().getContextClassLoader().getResourceAsStream(scanner.next() + ".json"), new TypeReference<Grafo>() {});
        } catch (Exception e) {
            LOGGER.error("Não foi possível ler o arquivo.", e);
            System.exit(1);
            return null;
        }
    }

    private static ObjectMapper getObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return mapper;
    }

    private static void warshall(Grafo g) {
        g.warshall();
    }

    private static void bellmanFord(Grafo g, Scanner scanner) {
        LOGGER.info("Vértices já existentes: " + g.obterTodosOsVerticesAsString());
        g.bellmanFord(g.obterVerticePeloIdentificador(scanner.next()));
    }

    private static void floydWarshall(Grafo g) {
        g.floydWarshall();
    }

    private static void compararAlgoritimos(Grafo g) {
        g.compararAlgoritimos();
    }

    private static void identificarComponentesNoGrafo(Grafo g) {
        g.identificarComponentesNoGrafo();
    }

    private static void dijkstra(Grafo g, Scanner scanner) {
        LOGGER.info("Vértices já existentes: " + g.obterTodosOsVerticesAsString());
        g.dijkstra(g.obterVerticePeloIdentificador(scanner.next()));
    }
}
