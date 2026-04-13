public interface IOrdenador<T>{
    public T[] ordenar(T[] dados);
    public long getComparacoes();
    public long getMovimentacoes();
    public double getTempoOrdenacao();
}