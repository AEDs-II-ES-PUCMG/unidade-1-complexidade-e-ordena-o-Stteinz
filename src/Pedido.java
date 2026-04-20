import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Pedido implements Comparable<Pedido> {

    private static int ultimoID = 1;
    private static final int MAX_ITENS = 10;
    private static final double DESCONTO_PG_A_VISTA = 0.15;

    private int idPedido;
    private LocalDate dataPedido;
    private int formaDePagamento;
    private ItemDePedido[] itens;
    private int quantItens;

    private Double cacheValorFinal;
    private Integer cacheVolumeTotal;
    private Double cacheEconomia;
    private Integer cachePrimeiroCodigo;

    public Pedido(LocalDate dataPedido, int formaDePagamento) {
        this.idPedido = ultimoID++;
        this.dataPedido = dataPedido;
        this.formaDePagamento = formaDePagamento;
        this.itens = new ItemDePedido[MAX_ITENS];
        this.quantItens = 0;
    }

    public boolean incluirProduto(Produto novo) {
        return incluirItem(new ItemDePedido(novo, 1));
    }

    public boolean incluirItem(ItemDePedido item) {
        if (quantItens < MAX_ITENS) {
            itens[quantItens++] = item;
            invalidarCaches();
            return true;
        }
        return false;
    }

    private void invalidarCaches() {
        cacheValorFinal = null;
        cacheVolumeTotal = null;
        cacheEconomia = null;
        cachePrimeiroCodigo = null;
    }

    private double arredondar2(double valor) {
        BigDecimal bd = new BigDecimal(Double.toString(valor));
        return bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public double valorFinalBrutoRegistrado() {
        double total = 0.0;
        for (int i = 0; i < quantItens; i++) {
            total += itens[i].getSubtotalCongelado();
        }
        return arredondar2(total);
    }

    public double valorFinal() {
        if (cacheValorFinal == null) {
            double total = valorFinalBrutoRegistrado();
            if (formaDePagamento == 1) {
                total *= (1.0 - DESCONTO_PG_A_VISTA);
            }
            cacheValorFinal = arredondar2(total);
        }
        return cacheValorFinal;
    }

    public int volumeTotalItens() {
        if (cacheVolumeTotal == null) {
            int volume = 0;
            for (int i = 0; i < quantItens; i++) {
                volume += itens[i].getQuantidade();
            }
            cacheVolumeTotal = volume;
        }
        return cacheVolumeTotal;
    }

    public int codigoIdentificadorPrimeiroItem() {
        if (cachePrimeiroCodigo == null) {
            if (quantItens == 0) {
                cachePrimeiroCodigo = Integer.MAX_VALUE;
            } else {
                cachePrimeiroCodigo = itens[0].getProduto().getDescricao().hashCode();
            }
        }
        return cachePrimeiroCodigo;
    }

    public double valorAtualCatalogoSemDesconto() {
        double total = 0.0;
        for (int i = 0; i < quantItens; i++) {
            total += itens[i].getSubtotalAtualCatalogo();
        }
        return arredondar2(total);
    }

    public double indiceEconomia() {
        if (cacheEconomia == null) {
            double economia = valorAtualCatalogoSemDesconto() - valorFinal();
            cacheEconomia = arredondar2(economia);
        }
        return cacheEconomia;
    }

    public LocalDate getDataPedido() {
        return dataPedido;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public int getQuantosProdutos() {
        return quantItens;
    }

    public ItemDePedido[] getItens() {
        ItemDePedido[] copia = new ItemDePedido[quantItens];
        for (int i = 0; i < quantItens; i++) {
            copia[i] = itens[i];
        }
        return copia;
    }

    @Override
    public int compareTo(Pedido outro) {
        return Integer.compare(this.idPedido, outro.idPedido);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        sb.append(String.format("Número do pedido: %02d%n", idPedido));
        sb.append("Data do pedido: ").append(formatoData.format(dataPedido)).append("\n");
        sb.append("Itens no pedido: ").append(quantItens).append("\n");
        sb.append("Volume total de itens: ").append(volumeTotalItens()).append("\n");
        sb.append("Itens registrados:\n");

        for (int i = 0; i < quantItens; i++) {
            sb.append(" - ").append(itens[i]).append("\n");
        }

        if (formaDePagamento == 1) {
            sb.append(String.format("Pagamento: à vista (desconto %.2f%%)%n", DESCONTO_PG_A_VISTA * 100));
        } else {
            sb.append("Pagamento: parcelado\n");
        }

        sb.append(String.format("Valor final do pedido: R$ %.2f%n", valorFinal()));
        sb.append(String.format("Valor atual no catálogo: R$ %.2f%n", valorAtualCatalogoSemDesconto()));
        sb.append(String.format("Índice de economia: R$ %.2f", indiceEconomia()));

        return sb.toString();
    }
}
