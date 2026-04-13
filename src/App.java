import java.util.Arrays;
import java.util.Random;

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


    static void testarOrdenadores(int tamanho) {
        Integer[] vetor = gerarVetorObjetos(tamanho);

        BubbleSort<Integer> bolha = new BubbleSort<>();
        InsertionSort<Integer> insercao = new InsertionSort<>();
        SelectionSort<Integer> selecao = new SelectionSort<>();

        Integer[] vetorOrdenadoBolha = bolha.ordenar(vetor);
        Integer[] vetorOrdenadoInsercao = insercao.ordenar(vetor);
        Integer[] vetorOrdenadoSelecao = selecao.ordenar(vetor);

        System.out.println("Tamanho do vetor: " + tamanho);
        System.out.println("-----------------------------------------------");

        System.out.println("BubbleSort:");
        System.out.println("  Comparações:            " + bolha.getComparacoes());
        System.out.println("  Movimentações:          " + bolha.getMovimentacoes());
        System.out.println("  Tempo de ordenação (ms): " + bolha.getTempoOrdenacao());

        System.out.println("InsertionSort:");
        System.out.println("  Comparações:            " + insercao.getComparacoes());
        System.out.println("  Movimentações:          " + insercao.getMovimentacoes());
        System.out.println("  Tempo de ordenação (ms): " + insercao.getTempoOrdenacao());

        System.out.println("SelectionSort:");
        System.out.println("  Comparações:            " + selecao.getComparacoes());
        System.out.println("  Movimentações:          " + selecao.getMovimentacoes());
        System.out.println("  Tempo de ordenação (ms): " + selecao.getTempoOrdenacao());

        System.out.println("===============================================\n");
    }

    public static void main(String[] args) {
        System.out.println("\n=== Comparação de Algoritmos de Ordenação ===\n");

        for (int tam : tamanhosTestePequeno) {
            testarOrdenadores(tam);
        }

        for (int tam : tamanhosTesteMedio) {
            testarOrdenadores(tam);
        }
    }
}
