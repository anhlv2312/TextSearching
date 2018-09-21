package comp3506.assn2.adts;

import java.util.Iterator;

/* [1 pp. 260] */
public interface List<E> extends Iterable<E> {

    /** Returns the number of elements in the list. */
    int size();

    /** Returns a boolean indicating whether the list is empty. */
    boolean isEmpty();

    /**  Returns the element of the list having index i;
     * an error condition occurs if i is not in range [0, size( ) − 1].
     */
    E get(int i) throws IndexOutOfBoundsException;

    /** Replaces the element at index i with e, and returns the old element that was replaced;
     * an error condition occurs if i is not in range [0, size( ) − 1].
     */
    E set(int i, E e) throws IndexOutOfBoundsException;

    /** Inserts a new element e into the list so that it has index i, moving all subsequent elements.
     * one index later in the list; an error condition occurs if i is not in range [0,size()] */
    void add(int i, E e) throws IndexOutOfBoundsException;

    /** Inserts a new element e into the end position of the list */
    void add(E e) throws IndexOutOfBoundsException;

    /** Removes and returns the element at index i, moving all subsequent elements one index earlier in the list;
     * an error condition occurs if i is not in range [0, size( ) − 1]. */
    E remove(int i) throws IndexOutOfBoundsException;

    /** Returns an iterator of the elements of M. */
    Iterator<E> iterator();
}

 /*
 * REFERENCE
 * [1] M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, Data
 * structures and algorithms in Java. John Wiley & Sons, 2014.
 *
 */