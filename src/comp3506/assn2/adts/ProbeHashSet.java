package comp3506.assn2.adts;

import java.util.Iterator;

public class ProbeHashSet<K> implements Set<K> {

    private Map<K, Boolean> table;

    public ProbeHashSet() {
        table = new ProbeHashMap<>();
    }

    public int size() {
        return table.size();
    }

    public boolean add(K key) {
        if (!contains(key)) {
            table.put(key, true);
            return true;
        } else {
            return false;
        }
    }

    public boolean remove(K key) {
        return (table.remove(key) == null);
    }

    public boolean contains(K key) {
        return (table.get(key) != null);
    }

    public void addAll(Set<K> target) {
        for (K key : target) {
            this.add(key);
        }
    }

    public void retainAll(Set<K> target) {
        for (K key : this) {
            if (!target.contains(key)) {
                this.remove(key);
            }
        }
    }

    public void removeAll(Set<K> target) {
        for (K key : target) {
            this.remove(key);
        }
    }

    public Iterator<K> iterator() {
        return table.keySet().iterator();
    }


}