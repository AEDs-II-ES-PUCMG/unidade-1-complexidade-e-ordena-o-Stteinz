import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

/**
 * MIT License
 *
 * Copyright(c) 2022-25 João Caram <caram@pucminas.br>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

public class AppOficina {

    static final int MAX_PEDIDOS = 100;
    static Produto[] produtos;
    static Produto[] produtosOrdenadosPorId;
    static Produto[] produtosOrdenadosPorDescricao;
    static int quantProdutos = 0;
    static String nomeArquivoDados = "produtos.txt";
    static IOrdenador<Produto> ordenador;

    static Scanner teclado;

    // #region utilidades

    static <T extends Number> T lerNumero(String mensagem, Class<T> classe) {
        System.out.print(mensagem + ": ");
        T valor;
        try {
            valor = classe.getConstructor(String.class).newInstance(teclado.nextLine());
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            return null;
        }
        return valor;
    }

    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static void pausa() {
        System.out.println("Tecle Enter para continuar.");
        teclado.nextLine();
    }

    static void cabecalho() {
        limparTela();
        System.out.println("XULAMBS COMÉRCIO DE COISINHAS v0.2\n================");
    }

    static int exibirMenuPrincipal() {
        cabecalho();
        System.out.println("1 - Procurar produto");
        System.out.println("2 - Filtrar produtos por preço máximo");
        System.out.println("3 - Ordenar produtos");
        System.out.println("4 - Embaralhar produtos");
        System.out.println("5 - Listar produtos");
        System.out.println("0 - Finalizar");
        return lerNumero("Digite sua opção", Integer.class);
    }

    static int exibirMenuOrdenadores() {
        cabecalho();
        System.out.println("Escolha o algoritmo de ordenação:");
        System.out.println("1 - Bolha (BubbleSort)");
        System.out.println("2 - Inserção (InsertionSort)");
        System.out.println("3 - Seleção (SelectionSort)");
        System.out.println("4 - MergeSort");
        System.out.println("0 - Cancelar");
        return lerNumero("Digite sua opção", Integer.class);
    }

    static int exibirMenuComparadores() {
        cabecalho();
        System.out.println("Escolha o critério de ordenação:");
        System.out.println("1 - Padrão (por descrição)");
        System.out.println("2 - Por código (identificador)");
        return lerNumero("Digite sua opção", Integer.class);
    }

    // #endregion

    static Produto[] carregarProdutos(String nomeArquivo) {
        Scanner dados;
        Produto[] dadosCarregados;
        try {
            dados = new Scanner(new File(nomeArquivo));
            int tamanho = Integer.parseInt(dados.nextLine().trim());
            dadosCarregados = new Produto[tamanho];
            while (dados.hasNextLine()) {
                String linha = dados.nextLine().trim();
                if (!linha.isEmpty()) {
                    Produto novoProduto = Produto.criarDoTexto(linha);
                    dadosCarregados[quantProdutos] = novoProduto;
                    quantProdutos++;
                }
            }
            dados.close();
        } catch (FileNotFoundException fex) {
            System.out.println("Arquivo não encontrado: " + nomeArquivo);
            dadosCarregados = null;
        }
        return dadosCarregados;
    }

    static Produto buscaBinariaPorId(int id) {
        if (produtosOrdenadosPorId == null) return null;

        int inicio = 0;
        int fim = produtosOrdenadosPorId.length - 1;

        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            int codigo = produtosOrdenadosPorId[meio].hashCode();

            if (codigo == id)
                return produtosOrdenadosPorId[meio];
            else if (codigo < id)
                inicio = meio + 1;
            else
                fim = meio - 1;
        }
        return null;
    }

    static Produto buscaBinariaPorDescricao(String descricao) {
        if (produtosOrdenadosPorDescricao == null) return null;

        String desc = descricao.toLowerCase();
        int inicio = 0;
        int fim = produtosOrdenadosPorDescricao.length - 1;

        // Busca binária: navega pelo array usando comparação lexicográfica
        while (inicio <= fim) {
            int meio = (inicio + fim) / 2;
            String descMeio = produtosOrdenadosPorDescricao[meio].descricao.toLowerCase();

            if (descMeio.contains(desc))
                return produtosOrdenadosPorDescricao[meio];

            if (descMeio.compareTo(desc) < 0)
                inicio = meio + 1;
            else
                fim = meio - 1;
        }

        for (int i = 0; i < produtosOrdenadosPorDescricao.length; i++) {
            if (produtosOrdenadosPorDescricao[i].descricao.toLowerCase().contains(desc))
                return produtosOrdenadosPorDescricao[i];
        }

        return null;
    }

    static Produto localizarProduto() {
        cabecalho();
        System.out.println("Localizando um produto");
        System.out.println("Escolha o critério de busca:");
        System.out.println("1 - Por identificador");
        System.out.println("2 - Por descrição");
        int criterio = lerNumero("Digite sua opção", Integer.class);

        Produto localizado = null;

        if (criterio == 1) {
            int numero = lerNumero("Digite o identificador do produto", Integer.class);
            localizado = buscaBinariaPorId(numero);
        } else if (criterio == 2) {
            System.out.print("Digite a descrição (ou parte dela): ");
            String descricao = teclado.nextLine();
            localizado = buscaBinariaPorDescricao(descricao);
        } else {
            System.out.println("Opção inválida.");
        }

        return localizado;
    }

    private static void mostrarProduto(Produto produto) {
        cabecalho();
        if (produto != null)
            System.out.println("Dados do produto:\n" + produto);
        else
            System.out.println("Produto não encontrado.");
    }

    private static void filtrarPorPrecoMaximo() {
        cabecalho();
        System.out.println("Filtrando por valor máximo:");
        double valor = lerNumero("Valor máximo", Double.class);
        StringBuilder relatorio = new StringBuilder();
        for (int i = 0; i < quantProdutos; i++) {
            if (produtos[i].valorDeVenda() <= valor)
                relatorio.append(produtos[i]).append("\n");
        }
        System.out.println(relatorio.isEmpty() ? "Nenhum produto encontrado." : relatorio);
    }

    static void ordenarProdutos() {
        cabecalho();

        int opcaoOrdenador = exibirMenuOrdenadores();
        if (opcaoOrdenador == 0) return;

        String nomeAlgoritmo;
        switch (opcaoOrdenador) {
            case 1 -> { ordenador = new BubbleSort<>();    nomeAlgoritmo = "BubbleSort";    }
            case 2 -> { ordenador = new InsertionSort<>(); nomeAlgoritmo = "InsertionSort"; }
            case 3 -> { ordenador = new SelectionSort<>(); nomeAlgoritmo = "SelectionSort"; }
            case 4 -> { ordenador = new MergeSort<>();     nomeAlgoritmo = "MergeSort";     }
            default -> { System.out.println("Opção inválida."); return; }
        }

        int opcaoComparador = exibirMenuComparadores();

        Produto[] dadosOrdenados;
        String nomeCriterio;
        switch (opcaoComparador) {
            case 1 -> {
                dadosOrdenados = ordenador.ordenar(produtos);
                nomeCriterio = "Padrão (por descrição)";
            }
            case 2 -> {
                dadosOrdenados = ordenador.ordenar(produtos, new ComparadorPorCodigo());
                nomeCriterio = "Por código";
            }
            default -> { System.out.println("Opção inválida."); return; }
        }

        cabecalho();
        System.out.println("Ordenação concluída!");
        System.out.println("Algoritmo:      " + nomeAlgoritmo);
        System.out.println("Critério:       " + nomeCriterio);
        System.out.println("Comparações:    " + ordenador.getComparacoes());
        System.out.println("Movimentações:  " + ordenador.getMovimentacoes());
        System.out.printf( "Tempo:          %.2f ms%n", ordenador.getTempoOrdenacao());

        verificarSubstituicao(produtos, dadosOrdenados);
    }

    static void embaralharProdutos() {
        Collections.shuffle(Arrays.asList(produtos));
    }

    static void verificarSubstituicao(Produto[] dadosOriginais, Produto[] copiaDados) {
        cabecalho();
        System.out.print("Deseja sobrescrever os dados originais pelos ordenados (S/N)? ");
        String resposta = teclado.nextLine().toUpperCase();
        if (resposta.equals("S"))
            System.arraycopy(copiaDados, 0, dadosOriginais, 0, copiaDados.length);
    }

    static void listarProdutos() {
        cabecalho();
        for (int i = 0; i < quantProdutos; i++) {
            System.out.println(produtos[i]);
        }
    }

    public static void main(String[] args) {
        teclado = new Scanner(System.in);

        produtos = carregarProdutos(nomeArquivoDados);

        if (produtos != null) {
            IOrdenador<Produto> ordenadorId = new MergeSort<>();
            produtosOrdenadosPorId = ordenadorId.ordenar(produtos, new ComparadorPorCodigo());

            IOrdenador<Produto> ordenadorDesc = new MergeSort<>();
            produtosOrdenadosPorDescricao = ordenadorDesc.ordenar(produtos);
        }

        embaralharProdutos();

        int opcao = -1;

        do {
            opcao = exibirMenuPrincipal();
            switch (opcao) {
                case 1 -> mostrarProduto(localizarProduto());
                case 2 -> filtrarPorPrecoMaximo();
                case 3 -> ordenarProdutos();
                case 4 -> embaralharProdutos();
                case 5 -> listarProdutos();
                case 0 -> System.out.println("FLW VLW OBG VLT SMP.");
            }
            pausa();
        } while (opcao != 0);

        teclado.close();
    }
}
