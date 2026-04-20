import java.util.Comparator;

public class ComparadorPedidoPorValorFinalSomente implements Comparator<Pedido> {
    @Override
    public int compare(Pedido p1, Pedido p2) {
        int cmp = Double.compare(p1.valorFinal(), p2.valorFinal());
        if (cmp != 0) {
            return cmp;
        }
        return Integer.compare(p1.getIdPedido(), p2.getIdPedido());
    }
}
