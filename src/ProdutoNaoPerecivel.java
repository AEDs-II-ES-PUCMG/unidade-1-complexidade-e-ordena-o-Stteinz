public class ProdutoNaoPerecivel extends Produto {

    public ProdutoNaoPerecivel(String descricao, double precoCusto, double margemLucro) {
        super(descricao, precoCusto, margemLucro);
    }

    public ProdutoNaoPerecivel(String descricao, double precoCusto) {
        super(descricao, precoCusto);
    }

    @Override
    public double valorDeVenda() {
        return precoCusto * (1 + margemLucro);
    }

    @Override
    public String gerarDadosTexto() {
        return String.format("1;%s;%.2f;%.2f", descricao, precoCusto, margemLucro);
    }
}
