import java.util.Comparator;

public interface IOrdenator<T> {
    void setComparador(Comparator<T> comparador);
    T[] ordenar(T[] dados);
    long getComparacoes();
    long getMovimentacoes();
    double getTempoOrdenacao();
}