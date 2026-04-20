import java.util.Comparator;

public class ComparadorPedidoPorCodigo implements Comparator<Pedido> {
    @Override
    public int compare(Pedido p1, Pedido p2) {
        return Integer.compare(p1.getIdPedido(), p2.getIdPedido());
    }
}
