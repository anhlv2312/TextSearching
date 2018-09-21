package comp3506.assn2.adts;

import java.util.Iterator;

public class ProbeHashSet<K> implements Set<K> {
    Map<K, Boolean> table;

    public ProbeHashSet() {
        table = new ProbeHashMap<>();
    }

    public ProbeHashSet(int cap) {
        table = new ProbeHashMap<>(cap);
    }

    public ProbeHashSet(int cap, int p) {
        table = new ProbeHashMap<>(cap, p);
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
        if (table.remove(key) == null) {
            return false;
        } else {
            return true;
        }
    }

    public boolean contains(K key) {
        return (table.get(key) != null);
    }

    public void addAll(Set<K> target) {
        Iterator<K> keySetIterator = target.iterator();
        while (keySetIterator.hasNext()) {
            this.add(keySetIterator.next());
        }
    }

    public void retainAll(Set<K> target) {
        Iterator<K> keySetIterator = this.iterator();
        while (keySetIterator.hasNext()) {
            K key = keySetIterator.next();
            if (!target.contains(key)) {
                this.remove(key);
            }
        }
    }

    public void removeAll(Set<K> target) {
        Iterator<K> keySetIterator = target.iterator();
        while (keySetIterator.hasNext()) {
            this.remove(keySetIterator.next());
        }
    }

    public Iterator<K> iterator() {
        return table.keySet().iterator();
    }


}