package comp3506.assn2.adts;


import java.util.Iterator;

/* [1 pp. 445] */
public interface Set<K> {

    /** Returns the number of entries in S. */
    int size();

    /** Adds the element e to S (if not already present). */
    boolean add(K key);

    /** Removes the element e from S (if it is present). */
    boolean remove(K key);

    /** Returns whether e is an element of S. */
    boolean contains(K key);

    /** Updates S to also include all elements of set Target. */
    void addAll(Set<K> target);

    /** Updates S so that it only keeps those elements that are also elements of set Target. */
    void retainAll(Set<K> target);

    /** Updates S by removing any of its elements that also occur in set Target */
    void removeAll(Set<K> target);

    /** Returns an iterator of the elements of S. */
    Iterator<K> iterator();

}

/*
 * REFERENCE
 * [1] M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, Data
 * structures and algorithms in Java. John Wiley & Sons, 2014.
 *
 */
