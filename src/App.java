import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class App {
    static final int[] tamanhosTesteGrande =  { 31_250_000, 62_500_000, 125_000_000, 250_000_000, 500_000_000 };
    static final int[] tamanhosTesteMedio =   {     12_500,     25_000,      50_000,     100_000,     200_000 };
    static final int[] tamanhosTestePequeno = {          3,          6,          12,          24,          48 };
    static Random aleatorio = new Random();
    static long operacoes;
    static double nanoToMilli = 1.0/1_000_000;

    /**
     * Gerador de vetores aleatórios de tamanho pré-definido. 
     * @param tamanho Tamanho do vetor a ser criado.
     * @return Vetor com dados aleatórios, com valores entre 1 e (tamanho/2), desordenado.
     */
    static int[] gerarVetor(int tamanho){
        int[] vetor = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = aleatorio.nextInt(1, tamanho/2);
        }
        return vetor;        
    }

    /**
     * Gerador de vetores de objetos do tipo Integer aleatórios de tamanho pré-definido. 
     * @param tamanho Tamanho do vetor a ser criado.
     * @return Vetor de Objetos Integer com dados aleatórios, com valores entre 1 e (tamanho/2), desordenado.
     */
    static Integer[] gerarVetorObjetos(int tamanho) {
        Integer[] vetor = new Integer[tamanho];
        for (int i = 0; i < tamanho; i++) {
            vetor[i] = aleatorio.nextInt(1, 10 * tamanho);
        }
        return vetor;
    }

    static void exibirResultado(String nome, IOrdenador<Integer> ordenador) {
        System.out.println(nome + ":");
        System.out.println("  Comparações:            " + ordenador.getComparacoes());
        System.out.println("  Movimentações:          " + ordenador.getMovimentacoes());
        System.out.println("  Tempo de ordenação (ms): " + ordenador.getTempoOrdenacao());
    }

    static void executarOrdenacao(IOrdenador<Integer> ordenador, String nome, Integer[] vetor) {
        Integer[] resultado = ordenador.ordenar(vetor);
        System.out.println("\nTamanho do vetor: " + vetor.length);
        System.out.println("-----------------------------------------------");
        exibirResultado(nome, ordenador);
        System.out.println("===============================================\n");
    }

    static void compararTodos(int tamanho) {
        Integer[] vetor = gerarVetorObjetos(tamanho);

        BubbleSort<Integer> bolha = new BubbleSort<>();
        InsertionSort<Integer> insercao = new InsertionSort<>();
        SelectionSort<Integer> selecao = new SelectionSort<>();
        MergeSort<Integer> merge = new MergeSort<>();

        bolha.ordenar(vetor);
        insercao.ordenar(vetor);
        selecao.ordenar(vetor);
        merge.ordenar(vetor);

        System.out.println("Tamanho do vetor: " + tamanho);
        System.out.println("-----------------------------------------------");
        exibirResultado("BubbleSort", bolha);
        exibirResultado("InsertionSort", insercao);
        exibirResultado("SelectionSort", selecao);
        exibirResultado("MergeSort", merge);
        System.out.println("===============================================\n");
    }

    static IOrdenador<Integer> criarOrdenador(int opcao) {
        switch (opcao) {
            case 1: return new BubbleSort<>();
            case 2: return new InsertionSort<>();
            case 3: return new SelectionSort<>();
            case 4: return new MergeSort<>();
            default: return null;
        }
    }

    static String nomeOrdenador(int opcao) {
        switch (opcao) {
            case 1: return "BubbleSort";
            case 2: return "InsertionSort";
            case 3: return "SelectionSort";
            case 4: return "MergeSort";
            default: return "";
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n=== Sistema de Ordenação ===");
            System.out.println("1 - BubbleSort");
            System.out.println("2 - InsertionSort");
            System.out.println("3 - SelectionSort");
            System.out.println("4 - MergeSort");
            System.out.println("5 - Comparar todos os algoritmos");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();

            if (opcao >= 1 && opcao <= 4) {
                System.out.print("Informe o tamanho do vetor: ");
                int tamanho = scanner.nextInt();
                Integer[] vetor = gerarVetorObjetos(tamanho);
                IOrdenador<Integer> ordenador = criarOrdenador(opcao);
                executarOrdenacao(ordenador, nomeOrdenador(opcao), vetor);

            } else if (opcao == 5) {
                System.out.print("Informe o tamanho do vetor: ");
                int tamanho = scanner.nextInt();
                compararTodos(tamanho);

            } else if (opcao != 0) {
                System.out.println("Opção inválida!");
            }
        }

        System.out.println("Encerrando...");
        scanner.close();
    }
}
