import java.util.Comparator;

public class ComparadorPedidoPorVolumeTotal implements Comparator<Pedido> {
    @Override
    public int compare(Pedido p1, Pedido p2) {
        int cmp = Integer.compare(p1.volumeTotalItens(), p2.volumeTotalItens());
        if (cmp != 0) {
            return cmp;
        }

        cmp = p1.getDataPedido().compareTo(p2.getDataPedido());
        if (cmp != 0) {
            return cmp;
        }

        return Integer.compare(p1.getIdPedido(), p2.getIdPedido());
    }
}
