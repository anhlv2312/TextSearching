package comp3506.assn2.adts;

import java.util.Iterator;

/**
 * An unordered data structure that does not contains duplicate keys
 *
 * space Complexity: O(n) (n is the number of entry)
 *
 * @author Vu Anh LE <vuanh.le@uq.edu.au>
 */
public class ProbeHashSet<K> implements Set<K> {

    private Map<K, Boolean> table;

    public ProbeHashSet() {
        table = new ProbeHashMap<>();
    }

    /**
     * Return the size of the Set
     *
     * Time complexity: O(1) (Expected)
     *
     * @return size of the set
     */
    public int size() {
        return table.size();
    }

    /**
     * Add a key into the Set
     *
     * Time complexity: O(1) (Expected)
     *
     * @return true if the key is added successfully, false if the key already exists
     */
    public boolean add(K key) {
        if (!contains(key)) {
            table.put(key, true);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Remove key from the Set
     *
     * Time complexity: O(1) (Expected)
     *
     * @return true if the key is removed successfully, false if the key does not exists
     */
    public boolean remove(K key) {
        return (table.remove(key) == null);
    }

    /**
     * Check if the key is in the Set or not
     *
     * Time complexity: O(1) (Expected)
     *
     * @return true if the set contains key, false otherwise
     */
    public boolean contains(K key) {
        return (table.get(key) != null);
    }

    /**
     * Add all items in the target set to this Set
     *
     * Time complexity: O(n) with n is the size of the target set
     *
     */
    public void addAll(Set<K> target) {
        for (K key : target) {
            this.add(key);
        }
    }

    /**
     * Remove all the items that is NOT in the target Set
     *
     * Time complexity: O(n*m) with n is the size of this set and m is the size of the target set
     *
     */
    public void retainAll(Set<K> target) {
        for (K key : this) {
            if (!target.contains(key)) {
                this.remove(key);
            }
        }
    }

    /**
     * Remove all the items that is in the target Set
     *
     * Time complexity: O(n) with n is the size of the target set
     *
     */
    public void removeAll(Set<K> target) {
        for (K key : target) {
            this.remove(key);
        }
    }

    /**
     * Return the Iterator object of the Set
     *
     * @return the iterator
     */
    public Iterator<K> iterator() {
        return table.keySet().iterator();
    }


}