package comp3506.assn2.adts;

/** pp. 404 */
public interface Map<K, V> {
    int size();

    boolean isEmpty();

    V get(K key);

    V put(K key, V value);

    V remove(K key);

    Iterable<K> keySet();

    Iterable<V> values();

    Iterable<Entry<K, V>> entrySet();

    interface Entry<K, V> {
        K getKey();

        void setKey(K key);

        V getValue();

        V setValue(V value);
    }
}
