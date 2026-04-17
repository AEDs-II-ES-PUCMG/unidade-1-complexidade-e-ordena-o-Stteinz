import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class ProdutoPerecivel extends Produto {
    private static final double DESCONTO = 0.25;
    private static final int PRAZO_DESCONTO = 7;

    private LocalDate dataDeValidade;

    public ProdutoPerecivel(String descricao, double precoCusto, double margemLucro, LocalDate validade) {
        super(descricao, precoCusto, margemLucro);
        if (validade.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Validade anterior ao dia de hoje!");
        dataDeValidade = validade;
    }

    @Override
    public double valorDeVenda() {
        double desconto = 0d;
        long diasValidade = LocalDate.now().until(dataDeValidade, ChronoUnit.DAYS);
        if (diasValidade <= PRAZO_DESCONTO)
            desconto = DESCONTO;
        return (precoCusto * (1 + margemLucro)) * (1 - desconto);
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return super.toString() + " - Válido até " + formato.format(dataDeValidade);
    }

    @Override
    public String gerarDadosTexto() {
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return String.format("2;%s;%.2f;%.2f;%s",
                descricao, precoCusto, margemLucro, formatador.format(dataDeValidade));
    }
}
