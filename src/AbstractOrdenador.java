import java.util.Arrays;
import java.util.Comparator;

public abstract class AbstractOrdenador<T> implements IOrdenator<T> {

    protected Comparator<T> comparador;
    protected long comparacoes;
    protected long movimentacoes;
    protected long inicio;
    protected long termino;

    @Override
    public void setComparador(Comparator<T> comparador) {
        this.comparador = comparador;
    }

    protected void iniciarContadores() {
        comparacoes = 0;
        movimentacoes = 0;
        inicio = System.nanoTime();
    }

    protected void terminarContadores() {
        termino = System.nanoTime();
    }

    protected int comparar(T a, T b) {
        comparacoes++;
        return comparador.compare(a, b);
    }

    protected void swap(T[] dados, int i, int j) {
        T tmp = dados[i];
        dados[i] = dados[j];
        dados[j] = tmp;
        movimentacoes += 3;
    }

    protected T[] copiar(T[] dados) {
        return Arrays.copyOf(dados, dados.length);
    }

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
        return (termino - inicio) / 1_000_000.0;
    }
}