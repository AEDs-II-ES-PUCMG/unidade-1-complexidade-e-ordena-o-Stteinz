public class Selecao<T> extends AbstractOrdenador<T> {

    @Override
    public T[] ordenar(T[] dados) {
        T[] v = copiar(dados);
        iniciarContadores();

        for (int i = 0; i < v.length - 1; i++) {
            int menor = i;
            for (int j = i + 1; j < v.length; j++) {
                if (comparar(v[j], v[menor]) < 0) {
                    menor = j;
                }
            }
            if (menor != i) {
                swap(v, i, menor);
            }
        }

        terminarContadores();
        return v;
    }
}
