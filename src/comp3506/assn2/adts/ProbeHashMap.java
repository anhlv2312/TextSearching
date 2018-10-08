package comp3506.assn2.adts;

import java.util.Random;

/* [1 pp. 426] */
public class ProbeHashMap<K, V> extends AbstractMap<K, V> {

    private int n = 0;
    private int capacity;
    private int prime;
    private long scale, shift;

    private MapEntry<K, V>[] table;
    private MapEntry<K, V> DEFUNCT = new MapEntry<>(null, null);

    public ProbeHashMap(int cap, int p) {
        prime = p;
        capacity = cap;
        Random rand = new Random();
        scale = rand.nextInt(prime-1) + 1;
        shift = rand.nextInt(prime);
        createTable();
    }

    public ProbeHashMap(int cap) {
        this(cap, 109345121);
    }

    public ProbeHashMap() {
        this(17);
    }

    private void createTable() {
        table = (MapEntry<K, V>[]) new MapEntry[capacity];
    }

    private boolean isAvailable(int j) {
        return (table[j] == null || table[j] == DEFUNCT);
    }


    public int size() {
        return n;
    }

    public V get(K key) {
        return bucketGet(hashValue(key), key);
    }

    public V remove(K key) {
        return bucketRemove(hashValue(key), key);
    }

    public V put(K key, V value) {
        V answer = bucketPut(hashValue(key), key, value);
        if (n > capacity / 2) {
            resize(2 * capacity - 1);
        }
        return answer;
    }

    private int hashValue(K key) {
        return (int) ((Math.abs(key.hashCode() * scale + shift) % prime) % capacity);
    }

    private void resize(int newCap) {
        ArrayList<Entry<K, V>> buffer = new ArrayList<>(n);
        for (Entry<K, V> e : entrySet()) {
            buffer.add(buffer.size(), e);
        }
        capacity = newCap;
        createTable();
        n = 0;
        for (Entry<K, V> e : buffer) {
            put(e.getKey(), e.getValue());
        }
    }

    private int findSlot(int h, K k) {
        int avail = -1;
        int j = h;
        do {
            if (isAvailable(j)) {
                if (avail == -1) {
                    avail = j;
                }
                if (table[j] == null) {
                    break;
                }
            } else if (table[j].getKey().equals(k)) {
                return j;
            }
            j = (j + 1) % capacity;
        } while (j != h);
        return -(avail + 1);
    }

    protected V bucketGet(int h, K k) {
        int j = findSlot(h, k);
        if (j < 0) {
            return null;
        }
        return table[j].getValue();
    }

    protected V bucketPut(int h, K k, V v) {
        int j = findSlot(h, k);
        if (j >= 0) {
            return table[j].setValue(v);
        }
        table[-(j + 1)] = new MapEntry<>(k, v);
        n++;
        return null;
    }

    protected V bucketRemove(int h, K k) {
        int j = findSlot(h, k);
        if (j < 0) {
            return null;
        }
        V answer = table[j].getValue();
        table[j] = DEFUNCT;
        n--;
        return answer;
    }

    public Iterable<Entry<K, V>> entrySet() {
        ArrayList<Entry<K, V>> buffer = new ArrayList<>();
        for (int h = 0; h < capacity; h++) {
            if (!isAvailable(h)) {
                buffer.add(buffer.size(), table[h]);
            }
        }
        return buffer;
    }

}

/*
 * REFERENCE
 * [1] M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, Data
 * structures and algorithms in Java. John Wiley & Sons, 2014.
 *
 */