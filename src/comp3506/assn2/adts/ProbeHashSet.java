package comp3506.assn2.adts;

public class ProbeHashSet<K> extends ProbeHashMap<K, Boolean> {

    public ProbeHashSet() {
        super();
    }

    public ProbeHashSet(int cap) {
        super(cap);
    }

    public ProbeHashSet(int cap, int p) {
        super(cap, p);
    }

}