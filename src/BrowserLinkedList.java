package src;

import java.util.Iterator;

public class BrowserLinkedList<T> implements Iterable<T> {
    private static class Node<T> {
        T data;
        Node<T> prev, next;

        Node(T d) {
            this.data = d;
        }
    }

    private Node<T> head; // top for stack usage
    private Node<T> tail;
    private int size = 0;

    public void addFirst(T val) {
        Node<T> n = new Node<>(val);
        n.next = head;
        if (head != null)
            head.prev = n;
        head = n;
        if (tail == null)
            tail = n;
        size++;
    }

    public T removeFirst() {
        if (head == null)
            throw new java.util.EmptyStackException();
        T v = head.data;
        head = head.next;
        if (head != null)
            head.prev = null;
        else
            tail = null;
        size--;
        return v;
    }

    public T peekFirst() {
        if (head == null)
            throw new java.util.EmptyStackException();
        return head.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    // Iterate from head → tail (works for stack top-down traversal)
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            Node<T> cur = head;

            public boolean hasNext() {
                return cur != null;
            }

            public T next() {
                T v = cur.data;
                cur = cur.next;
                return v;
            }
        };
    }

    // Optional: tail helpers if ever needed
    public void clear() {
        head = tail = null;
        size = 0;
    }
}
