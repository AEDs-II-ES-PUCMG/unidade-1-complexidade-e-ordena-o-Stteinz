import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class App {

    static String nomeArquivoDados;
    static Scanner teclado;
    static Produto[] produtosCadastrados;
    static int quantosProdutos = 0;
    static Pedido[] pedidosCadastrados;
    static Pedido[] pedidosOrdenadosPorData;
    static int quantPedidos = 0;
    static IOrdenator<Pedido> ordenador;

    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    static void cabecalho() {
        System.out.println("AEDs II COMÉRCIO DE COISINHAS");
        System.out.println("=============================");
    }

    static <T extends Number> T lerOpcao(String mensagem, Class<T> classe) {
        T valor;
        System.out.println(mensagem);
        try {
            valor = classe.getConstructor(String.class).newInstance(teclado.nextLine());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            return null;
        }
        return valor;
    }

    static Produto[] lerProdutos(String nomeArquivoDados) {
        Scanner arquivo = null;
        int numProdutos;
        String linha;
        Produto produto;
        Produto[] produtos;

        try {
            arquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"));
            numProdutos = Integer.parseInt(arquivo.nextLine().trim());
            produtos = new Produto[numProdutos];

            for (int i = 0; i < numProdutos; i++) {
                linha = arquivo.nextLine();
                produto = Produto.criarDoTexto(linha);
                produtos[i] = produto;
            }

            quantosProdutos = numProdutos;
        } catch (Exception e) {
            System.out.println("Erro ao ler produtos: " + e.getMessage());
            produtos = new Produto[0];
            quantosProdutos = 0;
        } finally {
            if (arquivo != null) {
                arquivo.close();
            }
        }

        return produtos;
    }

    static Pedido[] lerPedidos(String nomeArquivoDados) {
        Pedido[] pedidos;
        Scanner arquivo = null;
        int numPedidos;
        String linha;
        Pedido pedido;

        try {
            arquivo = new Scanner(new File(nomeArquivoDados), Charset.forName("UTF-8"));
            numPedidos = Integer.parseInt(arquivo.nextLine().trim());
            pedidos = new Pedido[numPedidos];

            for (int i = 0; i < numPedidos; i++) {
                linha = arquivo.nextLine();
                pedido = criarPedido(linha);
                pedidos[i] = pedido;
            }

            quantPedidos = numPedidos;
        } catch (Exception e) {
            System.out.println("Erro ao ler pedidos: " + e.getMessage());
            pedidos = new Pedido[0];
            quantPedidos = 0;
        } finally {
            if (arquivo != null) {
                arquivo.close();
            }
        }

        return pedidos;
    }

    private static Pedido criarPedido(String dados) {
        String[] dadosPedido = dados.split(";");
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dataDoPedido = LocalDate.parse(dadosPedido[0].trim(), formatoData);
        int formaDePagamento = Integer.parseInt(dadosPedido[1].trim());

        Pedido pedido = new Pedido(dataDoPedido, formaDePagamento);

        for (int i = 2; i < dadosPedido.length; i++) {
            Produto produto = pesquisarProduto(dadosPedido[i].trim());
            if (produto != null) {
                pedido.incluirItem(new ItemDePedido(produto, 1));
            }
        }

        return pedido;
    }

    static Produto pesquisarProduto(String pesquisado) {
        if (produtosCadastrados == null || produtosCadastrados.length == 0) {
            return null;
        }

        for (int i = 0; i < quantosProdutos; i++) {
            if (produtosCadastrados[i] != null
                    && produtosCadastrados[i].getDescricao().equals(pesquisado)) {
                return produtosCadastrados[i];
            }
        }

        return null;
    }

    static int menu() {
        cabecalho();
        System.out.println("1 - Procurar por pedidos realizados em uma data");
        System.out.println("2 - Ordenar pedidos");
        System.out.println("3 - Embaralhar pedidos");
        System.out.println("4 - Listar todos os pedidos");
        System.out.println("5 - Localizar pedidos premium");
        System.out.println("0 - Finalizar");

        Integer opcao = lerOpcao("Digite sua opção: ", Integer.class);
        return opcao == null ? -1 : opcao;
    }

    static void localizarPedidosPorData() {
        cabecalho();

        if (pedidosCadastrados == null || pedidosCadastrados.length == 0) {
            System.out.println("Nenhum pedido foi carregado.");
            return;
        }

        System.out.println("Digite a data desejada (dd/MM/yyyy):");
        String entrada = teclado.nextLine();

        try {
            LocalDate data = LocalDate.parse(entrada, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            if (pedidosOrdenadosPorData == null) {
                IOrdenator<Pedido> ordData = new MergeSort<>();
                ordData.setComparador(new ComparadorPedidoPorData());
                pedidosOrdenadosPorData = ordData.ordenar(pedidosCadastrados);
            }

            int pos = buscaBinariaPrimeiraData(pedidosOrdenadosPorData, data);

            if (pos == -1) {
                System.out.println("Nenhum pedido encontrado para essa data.");
                return;
            }

            while (pos < pedidosOrdenadosPorData.length
                    && pedidosOrdenadosPorData[pos].getDataPedido().equals(data)) {
                System.out.println(pedidosOrdenadosPorData[pos]);
                System.out.println();
                pos++;
            }
        } catch (Exception e) {
            System.out.println("Data inválida.");
        }
    }

    static int exibirMenuOrdenadores() {
        cabecalho();
        System.out.println("1 - Bolha");
        System.out.println("2 - Inserção");
        System.out.println("3 - Seleção");
        System.out.println("4 - Mergesort");
        System.out.println("5 - Heapsort");
        System.out.println("0 - Cancelar");

        Integer opcao = lerOpcao("Digite sua opção: ", Integer.class);
        return opcao == null ? -1 : opcao;
    }

    static int exibirMenuComparadores() {
        cabecalho();
        System.out.println("1 - Critério A - Valor Final do Pedido");
        System.out.println("2 - Critério B - Volume Total de Itens");
        System.out.println("3 - Critério C - Índice de Economia (ordem decrescente)");
        System.out.println("0 - Cancelar");

        Integer opcao = lerOpcao("Digite sua opção: ", Integer.class);
        return opcao == null ? -1 : opcao;
    }

    static void ordenarPedidos() {
        if (pedidosCadastrados == null || pedidosCadastrados.length == 0) {
            System.out.println("Erro: pedidos não foram carregados.");
            return;
        }

        int opcaoOrdenador = exibirMenuOrdenadores();
        if (opcaoOrdenador == 0) {
            return;
        }

        int opcaoComparador = exibirMenuComparadores();
        if (opcaoComparador == 0) {
            return;
        }

        ordenador = criarOrdenador(opcaoOrdenador);
        Comparator<Pedido> comparador = criarComparador(opcaoComparador);

        if (ordenador == null || comparador == null) {
            System.out.println("Opção inválida.");
            return;
        }

        ordenador.setComparador(comparador);
        pedidosCadastrados = ordenador.ordenar(pedidosCadastrados);
        pedidosOrdenadosPorData = null;

        System.out.printf("Tempo de ordenação: %.3f ms%n", ordenador.getTempoOrdenacao());
        System.out.printf("Comparações: %d%n", ordenador.getComparacoes());
        System.out.printf("Movimentações: %d%n", ordenador.getMovimentacoes());
    }

    static void embaralharPedidos() {
        if (pedidosCadastrados == null || pedidosCadastrados.length == 0) {
            System.out.println("Nenhum pedido para embaralhar.");
            return;
        }

        java.util.List<Pedido> lista = java.util.Arrays.asList(pedidosCadastrados);
        Collections.shuffle(lista);
        pedidosCadastrados = lista.toArray(new Pedido[0]);
        pedidosOrdenadosPorData = null;
    }

    static void listarTodosOsPedidos() {
        cabecalho();

        if (pedidosCadastrados == null || pedidosCadastrados.length == 0) {
            System.out.println("Nenhum pedido cadastrado.");
            return;
        }

        System.out.println("\nPedidos cadastrados:");
        for (int i = 0; i < quantPedidos; i++) {
            System.out.println(String.format("%02d - %s%n", (i + 1), pedidosCadastrados[i].toString()));
        }
    }

    static void localizarPedidosPremium() {
        cabecalho();

        if (pedidosCadastrados == null || pedidosCadastrados.length == 0) {
            System.out.println("Nenhum pedido foi carregado.");
            return;
        }

        System.out.println("Digite o valor de corte para pedidos premium:");
        String entrada = teclado.nextLine().replace(",", ".");

        try {
            double corte = Double.parseDouble(entrada);

            IOrdenator<Pedido> ord = new MergeSort<>();
            ord.setComparador(new ComparadorPedidoPorValorFinalSomente());
            Pedido[] ordenadosPorValor = ord.ordenar(pedidosCadastrados);

            int primeiraPosicao = buscaBinariaPrimeiroPremium(ordenadosPorValor, corte);

            if (primeiraPosicao == -1) {
                System.out.println("Nenhum pedido premium encontrado.");
                return;
            }

            for (int i = primeiraPosicao; i < ordenadosPorValor.length; i++) {
                System.out.println(ordenadosPorValor[i]);
                System.out.println();
            }
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido.");
        }
    }

    static int buscaBinariaPrimeiraData(Pedido[] vetor, LocalDate data) {
        int esq = 0;
        int dir = vetor.length - 1;
        int resultado = -1;

        while (esq <= dir) {
            int meio = (esq + dir) / 2;
            int cmp = vetor[meio].getDataPedido().compareTo(data);

            if (cmp >= 0) {
                if (cmp == 0) {
                    resultado = meio;
                }
                dir = meio - 1;
            } else {
                esq = meio + 1;
            }
        }

        return resultado;
    }

    static int buscaBinariaPrimeiroPremium(Pedido[] vetor, double corte) {
        int esq = 0;
        int dir = vetor.length - 1;
        int resultado = -1;

        while (esq <= dir) {
            int meio = (esq + dir) / 2;

            if (vetor[meio].valorFinal() >= corte) {
                resultado = meio;
                dir = meio - 1;
            } else {
                esq = meio + 1;
            }
        }

        return resultado;
    }

    static IOrdenator<Pedido> criarOrdenador(int opcao) {
        return switch (opcao) {
            case 1 -> new BubbleSort<>();
            case 2 -> new Insercao<>();
            case 3 -> new Selecao<>();
            case 4 -> new MergeSort<>();
            case 5 -> new Heapsort<>();
            default -> null;
        };
    }

    static Comparator<Pedido> criarComparador(int opcao) {
        return switch (opcao) {
            case 1 -> new ComparadorPedidoPorValorFinal();
            case 2 -> new ComparadorPedidoPorVolumeTotal();
            case 3 -> new ComparadorPedidoPorIndiceEconomia();
            default -> null;
        };
    }

    public static void main(String[] args) {
        teclado = new Scanner(System.in, Charset.forName("UTF-8"));

        String pastaDados = "src";
        nomeArquivoDados = pastaDados + File.separator + "produtos.txt";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        System.out.println("Produtos carregados: " + produtosCadastrados.length);

        String nomeArquivoPedidos = pastaDados + File.separator + "pedidos.txt";
        pedidosCadastrados = lerPedidos(nomeArquivoPedidos);
        System.out.println("Pedidos carregados: " + pedidosCadastrados.length);

        int opcao;
        do {
            opcao = menu();

            switch (opcao) {
                case 1 -> localizarPedidosPorData();
                case 2 -> ordenarPedidos();
                case 3 -> embaralharPedidos();
                case 4 -> listarTodosOsPedidos();
                case 5 -> localizarPedidosPremium();
                case 0 -> System.out.println("FLW VLW OBG VLT SMP.");
                default -> System.out.println("Opção inválida.");
            }

            if (opcao != 0) {
                pausa();
            }
        } while (opcao != 0);

        teclado.close();
    }
}