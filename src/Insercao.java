public class Insercao<T> extends AbstractOrdenador<T> {

    @Override
    public T[] ordenar(T[] dados) {
        T[] v = copiar(dados);
        iniciarContadores();

        for (int i = 1; i < v.length; i++) {
            T chave = v[i];
            int j = i - 1;

            while (j >= 0 && comparar(v[j], chave) > 0) {
                v[j + 1] = v[j];
                movimentacoes++;
                j--;
            }

            v[j + 1] = chave;
            movimentacoes++;
        }

        terminarContadores();
        return v;
    }
}
