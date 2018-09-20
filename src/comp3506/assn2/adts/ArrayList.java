package comp3506.assn2.adts;

import java.util.Iterator;
import java.util.NoSuchElementException;

/* [1 pp. 260] */
public class ArrayList<E> implements List<E> {

    public static final int CAPACITY = 16;
    private E[] data;
    private int size = 0;

    public ArrayList() {
        this(CAPACITY);
    }

    public ArrayList(int capacity) {
        data = (E[]) new Object[capacity];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public E get(int i) throws IndexOutOfBoundsException {
        checkIndex(i, size);
        return data[i];
    }

    public E set(int i, E e) throws IndexOutOfBoundsException {
        checkIndex(i, size);
        E temp = data[i];
        data[i] = e;
        return temp;
    }

    public void add(E e) throws IndexOutOfBoundsException, IllegalStateException {
        add(size, e);
    }

    public void add(int i, E e) throws IndexOutOfBoundsException, IllegalStateException {
        checkIndex(i, size + 1);
        if (size == data.length) {
            resize(2 * data.length);
        }
        for (int k = size - 1; k >= i; k--) {
            data[k + 1] = data[k];
        }
        data[i] = e;
        size++;
    }

    public E remove(int i) throws IndexOutOfBoundsException {
        checkIndex(i, size);
        E temp = data[i];
        for (int k = i; k < size - 1; k++)
            data[k] = data[k + 1];
        data[size - 1] = null;
        size--;
        return temp;
    }

    protected void checkIndex(int i, int n) throws IndexOutOfBoundsException {
        if (i < 0 || i >= n) {
            throw new IndexOutOfBoundsException("Illegal index: " + i);
        }
    }

    /* [1 pp. 265] */
    /** Resize internal array to have given capacity >= size. */
    protected void resize(int capacity) {
        E[] temp = (E[]) new Object[capacity];
        for (int k=0; k < size; k++) {
            temp[k] = data[k];
        }
        data = temp;
    }

    public Iterator<E> iterator() {
        return new ArrayIterator();
    }

    /* [1 pp. 285] */
    /** A (nonstatic) inner class. Note well that each instance contains an implicit
     * reference to the containing list, allowing it to access the list's members. */
    private class ArrayIterator implements Iterator<E> {
        private int j = 0;
        private boolean removable = false;

        /** Tests whether the iterator has a next object. */
        public boolean hasNext() {
            return j < size;
        }

        /** Returns the next object in the iterator. */
        public E next() throws NoSuchElementException {
            if (j == size) {
                throw new NoSuchElementException("No next element");
            }
            removable = true; // this element can subsequently be removed
            return data[j++]; // post-increment j, so it is ready for future call to next
        }

        /** Removes the element returned by most recent call to next. */
        public void remove() throws IllegalStateException {
            if (!removable) {
                throw new IllegalStateException("nothing to remove");
            }
            ArrayList.this.remove(j-1); j--;
            removable = false;
        }
    }

}

/*
 * REFERENCE
 * [1] M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, Data
 * structures and algorithms in Java. John Wiley & Sons, 2014.
 *
 */