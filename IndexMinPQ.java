import java.util.NoSuchElementException;

public class IndexMinPQ<Item extends Comparable<Item>> {
    private int maxN;           // maximum number of elements
    private int n;              // current number of elements
    private int[] pq;           // binary heap using 1-based indexing
    private int[] qp;           // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
    private Item[] items;       // items with priorities

    public IndexMinPQ(int maxN) {
        this.maxN = maxN;
        n = 0;
        items = (Item[]) new Comparable[maxN + 1];
        pq = new int[maxN + 1];
        qp = new int[maxN + 1];
        for (int i = 0; i <= maxN; i++) qp[i] = -1;
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public boolean contains(int k) {
        return qp[k] != -1;
    }

    public int size() {
        return n;
    }

    public void insert(int k, Item item) {
        n++;
        qp[k] = n;
        pq[n] = k;
        items[k] = item;
        swim(n);
    }

    public void change(int k, Item item) {
        items[k] = item;
        swim(qp[k]);
        sink(qp[k]);
    }

    public void delete(int k) {
        int index = qp[k];
        exch(index, n--);
        swim(index);
        sink(index);
        items[k] = null;
        qp[k] = -1;
    }

    public Item min() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return items[pq[1]];
    }

    public int minIndex() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];
    }

    public int delMin() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        int minIndex = pq[1];
        exch(1, n--);
        sink(1);
        qp[minIndex] = -1;
        items[pq[n + 1]] = null;
        pq[n + 1] = -1;
        return minIndex;
    }

    private void swim(int k) {
        while (k > 1 && greater(k / 2, k)) {
            exch(k, k / 2);
            k = k / 2;
        }
    }

    private void sink(int k) {
        while (2 * k <= n) {
            int j = 2 * k;
            if (j < n && greater(j, j + 1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

    private boolean greater(int i, int j) {
        return items[pq[i]].compareTo(items[pq[j]]) > 0;
    }

    private void exch(int i, int j) {
        int swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
        qp[pq[i]] = i;
        qp[pq[j]] = j;
    }
}

