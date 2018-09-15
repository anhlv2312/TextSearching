package comp3506.assn2.application;

import java.util.Iterator;

/** pp. 260 */
public interface List<E> extends Iterable<E> {
    int size();

    boolean isEmpty();

    E get(int i) throws IndexOutOfBoundsException;

    E set(int i, E e) throws IndexOutOfBoundsException;

    void add(int i, E e) throws IndexOutOfBoundsException;

    E remove(int i) throws IndexOutOfBoundsException;

    Iterator<E> iterator();
}
