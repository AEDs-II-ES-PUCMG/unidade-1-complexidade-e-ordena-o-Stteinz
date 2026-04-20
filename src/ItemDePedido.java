public class ItemDePedido {

    private Produto produto;
    private int quantidade;
    private double precoUnitarioCongelado;

    public ItemDePedido(Produto produto) {
        this(produto, 1);
    }

    public ItemDePedido(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitarioCongelado = produto.valorDeVenda();
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getPrecoUnitarioCongelado() {
        return precoUnitarioCongelado;
    }

    public double getSubtotalCongelado() {
        return precoUnitarioCongelado * quantidade;
    }

    public double getSubtotalAtualCatalogo() {
        return produto.valorDeVenda() * quantidade;
    }

    @Override
    public String toString() {
        return String.format("%s | qtd: %d | preço registrado: R$ %.2f | subtotal: R$ %.2f",
                produto.getDescricao(), quantidade, precoUnitarioCongelado, getSubtotalCongelado());
    }
}
