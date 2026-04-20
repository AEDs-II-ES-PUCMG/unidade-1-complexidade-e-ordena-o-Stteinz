import java.util.Comparator;

public class ComparadorPedidoPorValorFinal implements Comparator<Pedido> {
    @Override
    public int compare(Pedido p1, Pedido p2) {
        int cmp = Double.compare(p1.valorFinal(), p2.valorFinal());
        if (cmp != 0) {
            return cmp;
        }

        cmp = Integer.compare(p1.volumeTotalItens(), p2.volumeTotalItens());
        if (cmp != 0) {
            return cmp;
        }

        return Integer.compare(p1.codigoIdentificadorPrimeiroItem(), p2.codigoIdentificadorPrimeiroItem());
    }
}
