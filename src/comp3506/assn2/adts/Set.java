package comp3506.assn2.adts;

import java.util.Iterator;

public interface Set<E> extends Iterable<E> {
    int size();

    boolean isEmpty();

    void add(E e) throws IndexOutOfBoundsException;

    E remove(int i) throws IndexOutOfBoundsException;

    Iterator<E> iterator();

}
