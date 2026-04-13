import java.util.Arrays;

public class MergeSort<T extends Comparable<T>> implements IOrdenador<T> {
    private long comparacoes;
    private long movimentacoes;
    private double tempoOrdenacao;
    private double inicio;

    private double nanoToMilli = 1.0/1_000_000;

    @Override
    public long getComparacoes() {
        return comparacoes;
    }

    @Override
    public long getMovimentacoes() {
        return movimentacoes;
    }

    @Override
    public double getTempoOrdenacao() {
        return tempoOrdenacao;
    }

    private void iniciar(){
        this.comparacoes = 0;
        this.movimentacoes = 0;
        this.inicio = System.nanoTime();
    }

    private void terminar(){
        this.tempoOrdenacao = (System.nanoTime() - this.inicio) * nanoToMilli;
    }

    @Override
    public T[] ordenar(T[] dados) {
        T[] dadosOrdenados = Arrays.copyOf(dados, dados.length);
        iniciar();
        mergesort(dadosOrdenados, 0, dadosOrdenados.length - 1);
        terminar();
        return dadosOrdenados;
    }

    private void mergesort(T[] vetor, int esquerda, int direita) {
        if (esquerda < direita) {
            int meio = (esquerda + direita) / 2;
            mergesort(vetor, esquerda, meio);
            mergesort(vetor, meio + 1, direita);
            intercalar(vetor, esquerda, meio, direita);
        }
    }

    @SuppressWarnings("unchecked")
    private void intercalar(T[] vetor, int esquerda, int meio, int direita) {
        int tamanho = direita - esquerda + 1;
        T[] temp = (T[]) new Comparable[tamanho];

        int i = esquerda;
        int j = meio + 1;
        int k = 0;

        while (i <= meio && j <= direita) {
            comparacoes++;
            if (vetor[i].compareTo(vetor[j]) <= 0) {
                temp[k] = vetor[i];
                i++;
            } else {
                temp[k] = vetor[j];
                j++;
            }
            movimentacoes++;
            k++;
        }

        while (i <= meio) {
            temp[k] = vetor[i];
            movimentacoes++;
            i++;
            k++;
        }

        while (j <= direita) {
            temp[k] = vetor[j];
            movimentacoes++;
            j++;
            k++;
        }

        for (k = 0; k < tamanho; k++) {
            vetor[esquerda + k] = temp[k];
            movimentacoes++;
        }
    }
}
