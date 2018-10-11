package comp3506.assn2.adts;

import java.util.Random;

/* [1 pp. 426] */

/**
 * ProbeHashMap class, using open addressing with linear probing
 *
 * Space Complexity: O(n) (n is the number of entry)
 */
public class ProbeHashMap<K, V> extends AbstractMap<K, V> {

    private int n = 0;
    private int capacity;
    private int prime;
    private long scale, shift;

    private MapEntry<K, V>[] table;
    // Define the Defunct value
    private MapEntry<K, V> DEFUNCT = new MapEntry<>(null, null);

    /**
     * Constructor
     *
     * Time complexity: O(1)
     */
    public ProbeHashMap(int cap, int p) {
        prime = p;
        capacity = cap;
        Random rand = new Random();
        scale = rand.nextInt(prime-1) + 1;
        shift = rand.nextInt(prime);
        createTable();
    }

    /**
     * Constructor
     *
     * Time complexity: O(1)
     */
    public ProbeHashMap(int cap) {
        this(cap, 109345121);
    }

    /**
     * Constructor
     *
     * Time complexity: O(1)
     */
    public ProbeHashMap() {
        this(17);
    }

    /* [1 pp. 423] */
    /**
     * Returns the number of entries in M.
     *
     * Time complexity: O(1)
     */
    public int size() {
        return n;
    }

    /**
     * Returns the value v associated with key k, if such an entry exists; otherwise returns null.
     *
     * Time complexity: O(1) (Expected)
     */
    public V get(K key) {
        return bucketGet(hashValue(key), key);
    }

    /**
     * If M does not have an entry with key equal to k, then adds entry (k,v) to M and returns null;
     * else, replaces with v the existing value of the entry with key equal to k and returns the old value.
     *
     * Time complexity: O(1) (Expected)
     */
    public V put(K key, V value) {
        V answer = bucketPut(hashValue(key), key, value);
        if (n > capacity / 2) {
            resize(2 * capacity - 1);
        }
        return answer;
    }

    /**
     * Removes from M the entry with key equal to k, and returns its value;
     * if M has no such entry, then returns null.
     *
     * Time complexity: O(1) (Expected)
     */
    public V remove(K key) {
        return bucketRemove(hashValue(key), key);
    }


    /**
     * Returns an iterable collection of all key-value entries of the map.
     *
     */
    public Iterable<Entry<K, V>> entrySet() {
        ArrayList<Entry<K, V>> buffer = new ArrayList<>();
        for (int h = 0; h < capacity; h++) {
            if (!isAvailable(h)) {
                buffer.add(buffer.size(), table[h]);
            }
        }
        return buffer;
    }

    /** Creates an empty table having length equal to current capacity. */
    private void createTable() {
        table = (MapEntry<K, V>[]) new MapEntry[capacity];
    }


    /** Returns true if location is either empty or the ”defunct” sentinel. */
    private boolean isAvailable(int j) {
        return (table[j] == null || table[j] == DEFUNCT);
    }

    /** Returns index with key k, or −(a+1) such that k could be added at index a. */
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

    /** Returns value associated with key k in bucket with hash value h, or else null. */
    private V bucketGet(int h, K k) {
        int j = findSlot(h, k);
        if (j < 0) {
            return null;
        }
        return table[j].getValue();
    }

    /** Associates key k with value v in bucket with hash value h; returns old value. */
    private V bucketPut(int h, K k, V v) {
        int j = findSlot(h, k);
        if (j >= 0) {
            return table[j].setValue(v);
        }
        table[-(j + 1)] = new MapEntry<>(k, v);
        n++;
        return null;
    }

    /** Removes entry having key k from bucket with hash value h (if any). */
    private V bucketRemove(int h, K k) {
        int j = findSlot(h, k);
        if (j < 0) {
            return null;
        }
        V answer = table[j].getValue();
        table[j] = DEFUNCT;
        n--;
        return answer;
    }

    /** Calculate the hash value */
    private int hashValue(K key) {
        return (int) ((Math.abs(key.hashCode() * scale + shift) % prime) % capacity);
    }

    /** Resize the HashMap*/
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

}

/*
 * REFERENCE
 * [1] M. T. Goodrich, R. Tamassia, and M. H. Goldwasser, Data
 * structures and algorithms in Java. John Wiley & Sons, 2014.
 *
 */