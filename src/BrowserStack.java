package src;

import java.util.Iterator;

public class BrowserStack<T> implements Iterable<T> {
    private final BrowserLinkedList<T> list = new BrowserLinkedList<>();

    public void push(T v) {
        list.addFirst(v);
    }

    public T pop() {
        return list.removeFirst();
    }

    public T peek() {
        return list.peekFirst();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }

    public void clear() {
        while (!isEmpty())
            pop();
    }

    // Internal helper so StackIterator can delegate
    Iterable<T> asIterable() {
        return list;
    }

    @Override
    public Iterator<T> iterator() {
        // Special iterator class
        return new StackIterator<>(list.iterator());

    }
}