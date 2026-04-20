import java.util.Comparator;

public class ComparadorPedidoPorIndiceEconomia implements Comparator<Pedido> {
    @Override
    public int compare(Pedido p1, Pedido p2) {
        int cmp = Double.compare(p2.indiceEconomia(), p1.indiceEconomia());
        if (cmp != 0) {
            return cmp;
        }

        cmp = Double.compare(p1.valorFinal(), p2.valorFinal());
        if (cmp != 0) {
            return cmp;
        }

        return Integer.compare(p1.getIdPedido(), p2.getIdPedido());
    }
}
