import java.util.Arrays;
import java.util.Comparator;

public class BubbleSort<T extends Comparable<T>> implements IOrdenador<T> {

    private long comparacoes;
    private long movimentacoes;
    private double tempoOrdenacao;
    private double inicio;

    private double nanoToMilli = 1.0 / 1_000_000;

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

    private void iniciar() {
        this.comparacoes = 0;
        this.movimentacoes = 0;
        this.inicio = System.nanoTime();
    }

    private void terminar() {
        this.tempoOrdenacao = (System.nanoTime() - this.inicio) * nanoToMilli;
    }

    private void swap(int x, int y, T[] vetor) {
        T temp = vetor[x];
        vetor[x] = vetor[y];
        vetor[y] = temp;
        movimentacoes += 3;
    }

    @Override
    public T[] ordenar(T[] dados) {
        return ordenar(dados, (a, b) -> a.compareTo(b));
    }

    @Override
    public T[] ordenar(T[] dados, Comparator<T> comparador) {
        T[] dadosOrdenados = Arrays.copyOf(dados, dados.length);
        int tamanho = dadosOrdenados.length;
        iniciar();
        for (int i = tamanho - 1; i > 0; i--) {
            boolean trocou = false;
            for (int j = 0; j < i; j++) {
                comparacoes++;
                if (comparador.compare(dadosOrdenados[j], dadosOrdenados[j + 1]) > 0) {
                    swap(j, j + 1, dadosOrdenados);
                    trocou = true;
                }
            }
            if (!trocou) break;
        }
        terminar();
        return dadosOrdenados;
    }
}
