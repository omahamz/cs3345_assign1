package src;

import java.util.Iterator;

public class StackIterator<T> implements Iterator<T> {
    private final Iterator<T> base;

    public StackIterator(Iterator<T> base) {
        this.base = base;
    }

    @Override
    public boolean hasNext() {
        return base.hasNext();
    }

    @Override
    public T next() {
        return base.next();
    }
}
