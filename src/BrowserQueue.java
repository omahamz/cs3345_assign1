package src;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class BrowserQueue<T> implements Iterable<T> {
    private final BrowserArrayList<T> arr = new BrowserArrayList<>();

    public void enqueue(T v) {
        arr.addLast(v);
    }

    public T dequeue() {
        if (arr.isEmpty())
            throw new NoSuchElementException();
        return arr.removeFirst();
    }

    public boolean isEmpty() {
        return arr.isEmpty();
    }

    public int size() {
        return arr.size();
    }

    public void clear() {
        arr.clear();
    }

    public T get(int i) {
        return arr.get(i);
    }

    @Override
    public Iterator<T> iterator() {
        return arr.iterator();
    }
}
