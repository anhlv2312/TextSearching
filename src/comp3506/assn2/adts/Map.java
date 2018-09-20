package comp3506.assn2.adts;

/* [1 pp. 404] */
public interface Map<K, V> {

    /** Returns the number of entries in M. */
    int size();

    /** Returns a boolean indicating whether M is empty. */
    boolean isEmpty();

    /** Returns the value v associated with key k, if such an entry exists; otherwise returns null. */
    V get(K key);

    /** If M does not have an entry with key equal to k, then adds entry (k,v) to M and returns null;
     *  else, replaces with v the existing value of the entry with key equal to k and returns the old value.
     */
    V put(K key, V value);

    /** Removes from M the entry with key equal to k, and returns its value;
     * if M has no such entry, then returns null.
     */
    V remove(K key);

    /** Returns an iterable collection containing all the keys stored in M. */
    Iterable<K> keySet();

    /** Returns an iterable collection containing all the values of entries stored in M
     * (with repetition if multiple keys map to the same value).
     */
    Iterable<V> values();

    /** Returns an iterable collection containing all the key-value en- tries in M. */
    Iterable<Entry<K, V>> entrySet();

    interface Entry<K, V> {
        K getKey();

        void setKey(K key);

        V getValue();

        V setValue(V value);
    }
}

/*
 * REFERENCE
 * [1] M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, Data
 * structures and algorithms in Java. John Wiley & Sons, 2014.
 *
 */