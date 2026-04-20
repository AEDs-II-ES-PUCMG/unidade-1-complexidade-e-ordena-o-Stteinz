public class Heapsort<T> extends AbstractOrdenador<T> {

    @Override
    public T[] ordenar(T[] dados) {
        T[] v = copiar(dados);
        iniciarContadores();

        for (int i = v.length / 2 - 1; i >= 0; i--) {
            heapify(v, v.length, i);
        }

        for (int fim = v.length - 1; fim > 0; fim--) {
            swap(v, 0, fim);
            heapify(v, fim, 0);
        }

        terminarContadores();
        return v;
    }

    private void heapify(T[] v, int tamanhoHeap, int raiz) {
        int maior = raiz;
        int esq = 2 * raiz + 1;
        int dir = 2 * raiz + 2;

        if (esq < tamanhoHeap && comparar(v[esq], v[maior]) > 0) {
            maior = esq;
        }

        if (dir < tamanhoHeap && comparar(v[dir], v[maior]) > 0) {
            maior = dir;
        }

        if (maior != raiz) {
            swap(v, raiz, maior);
            heapify(v, tamanhoHeap, maior);
        }
    }
}
