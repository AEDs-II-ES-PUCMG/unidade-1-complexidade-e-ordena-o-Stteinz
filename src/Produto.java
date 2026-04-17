import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * MIT License
 * Copyright(c) 2025 João Caram <caram@pucminas.br>
 *
 * Classe abstrata que representa um produto do comércio XULAMBS.
 */
public abstract class Produto implements Comparable<Produto> {
    private static final double MARGEM_PADRAO = 0.2;
    private static int ultimoID = 10_000;

    protected int idProduto;
    protected String descricao;
    protected double precoCusto;
    protected double margemLucro;

    private void init(String desc, double precoCusto, double margemLucro) {
        if (desc.length() < 3 || precoCusto <= 0 || margemLucro <= 0)
            throw new IllegalArgumentException("Valores inválidos para o produto");
        descricao = desc;
        this.precoCusto = precoCusto;
        this.margemLucro = margemLucro;
        idProduto = ultimoID++;
    }

    protected Produto(String desc, double precoCusto, double margemLucro) {
        init(desc, precoCusto, margemLucro);
    }

    protected Produto(String desc, double precoCusto) {
        init(desc, precoCusto, MARGEM_PADRAO);
    }

    public abstract double valorDeVenda();

    @Override
    public int hashCode() {
        return idProduto;
    }

    @Override
    public String toString() {
        NumberFormat moeda = NumberFormat.getCurrencyInstance();
        return String.format("%05d - %s: %s", idProduto, descricao, moeda.format(valorDeVenda()));
    }

    @Override
    public int compareTo(Produto outro) {
        return this.descricao.compareTo(outro.descricao);
    }

    @Override
    public boolean equals(Object obj) {
        try {
            Produto outro = (Produto) obj;
            return this.hashCode() == outro.hashCode();
        } catch (ClassCastException ex) {
            return false;
        }
    }

    /**
     * Cria um produto a partir de uma linha de texto no formato:
     * "tipo;descrição;precoCusto;margemLucro[;dataValidade]"
     * Tipo 1 = não perecível, Tipo 2 = perecível.
     */
    static Produto criarDoTexto(String linha) {
        Produto novoProduto = null;
        String[] detalhes = linha.split(";");
        String descr = detalhes[1];
        double precoCusto = Double.parseDouble(detalhes[2].replace(",", "."));
        double margem = Double.parseDouble(detalhes[3].replace(",", "."));
        if (detalhes[0].equals("1")) {
            novoProduto = new ProdutoNaoPerecivel(descr, precoCusto, margem);
        } else {
            LocalDate dataValidade =
                    LocalDate.parse(detalhes[4], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            novoProduto = new ProdutoPerecivel(descr, precoCusto, margem, dataValidade);
        }
        return novoProduto;
    }

    public abstract String gerarDadosTexto();
}
