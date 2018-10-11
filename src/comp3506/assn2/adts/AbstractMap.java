package comp3506.assn2.adts;

import java.util.Iterator;

/* [1 pp. 407] */
public abstract class AbstractMap<K, V> implements Map<K, V> {

    /**
     * Returns a boolean indicating whether M is empty.
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns an iterable collection containing all the keys stored in M.
     */
    public Iterable<K> keySet() {
        return new KeyIterable();
    }

    /**
     * Returns an iterable collection containing all the values of entries stored in M
     * (with repetition if multiple keys map to the same value).
     */
    public Iterable<V> values() {
        return new ValueIterable();
    }

    /**
     * The nested class that represent the Map entry
     */
    protected static class MapEntry<K, V> implements Entry<K, V> {
        private K k;
        private V v;

        public MapEntry(K key, V value) {
            k = key;
            v = value;
        }

        public K getKey() {
            return k;
        }

        public void setKey(K key) {
            k = key;
        }

        public V getValue() {
            return v;
        }

        public V setValue(V value) {
            V old = v;
            v = value;
            return old;
        }
    }

    /**
     * The nested Iterator and Iterables class
     */
    private class KeyIterator implements Iterator<K> {
        private Iterator<Entry<K, V>> entries = entrySet().iterator();

        public boolean hasNext() {
            return entries.hasNext();
        }

        public K next() {
            return entries.next().getKey();
        }
    }

    /**
     * The nested Iterator and Iterables class
     */
    private class KeyIterable implements Iterable<K> {
        public Iterator<K> iterator() {
            return new KeyIterator();
        }
    }

    /**
     * The nested Iterator and Iterables class
     */
    private class ValueIterator implements Iterator<V> {
        private Iterator<Entry<K, V>> entries = entrySet().iterator();

        public boolean hasNext() {
            return entries.hasNext();
        }

        public V next() {
            return entries.next().getValue();
        }
    }

    /**
     * The nested Iterator and Iterables class
     */
    private class ValueIterable implements Iterable<V> {
        public Iterator<V> iterator() {
            return new ValueIterator();
        }
    }
}

/*
 * REFERENCE
 * [1] M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, Data
 * structures and algorithms in Java. John Wiley & Sons, 2014.
 *
 */