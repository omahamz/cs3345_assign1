package src;

public class BrowserArrayList<T> implements Iterable<T> {
    private Object[] data;
    private int head = 0; // index of front
    private int size = 0;

    public BrowserArrayList() {
        data = new Object[8];
    }

    private int cap() {
        return data.length;
    }

    private int idx(int i) {
        return (head + i) % cap();
    }

    private void ensure(int need) {
        if (need <= cap())
            return;
        int newCap = Math.max(cap() * 2, need);
        Object[] nd = new Object[newCap];
        for (int i = 0; i < size; i++)
            nd[i] = data[idx(i)];
        data = nd;
        head = 0;
    }

    // queue ops will put at tail = idx(size)
    public void addLast(T val) {
        ensure(size + 1);
        data[idx(size)] = val;
        size++;
    }

    public T removeFirst() {
        if (size == 0)
            throw new java.util.NoSuchElementException();
        @SuppressWarnings("unchecked")
        T v = (T) data[head];
        data[head] = null;
        head = (head + 1) % cap();
        size--;
        return v;
    }

    public T get(int i) {
        if (i < 0 || i >= size)
            throw new IndexOutOfBoundsException();
        @SuppressWarnings("unchecked")
        T v = (T) data[idx(i)];
        return v;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < size; i++)
            data[idx(i)] = null;
        head = 0;
        size = 0;
    }

    @Override
    public java.util.Iterator<T> iterator() {
        return new java.util.Iterator<T>() {
            int i = 0;

            public boolean hasNext() {
                return i < size;
            }

            public T next() {
                T v = get(i);
                i++;
                return v;
            }
        };
    }
}
