package comp3506.assn2.application;

import comp3506.assn2.adts.*;
import comp3506.assn2.utils.Pair;
import comp3506.assn2.utils.Triple;

/* [1 pp. 449] */
public class PositionMap {

    private int total = 0;
    private String word;
    private ProbeHashMap<Integer, List<Integer>> positions;

    public PositionMap(String word) {
        this.word = word;
        positions = new ProbeHashMap<>();
    }

    public int size() {
        return total;
    }

    public void addPosition(int line, int column) {
        List<Integer> secondary = positions.get(line);
        if (secondary == null) {
            secondary = new ArrayList<>();
            positions.put(line, secondary);
        }
        secondary.add(column);
        total++;
    }

    public Set<Pair<Integer, Integer>> getPositionPairs() {
        Set<Pair<Integer, Integer>> result = new ProbeHashSet<>();
        for (Map.Entry<Integer, List<Integer>> line : positions.entrySet()) {
            for (int column : line.getValue()) {
                result.add(new Pair<>(line.getKey(), column));
            }
        }
        return result;
    }

    public Set<Triple<Integer, Integer, String>> getPositionTriples() {
        Set<Triple<Integer, Integer, String>> result = new ProbeHashSet<>();
        for (Map.Entry<Integer, List<Integer>> line : positions.entrySet()) {
            for (int column : line.getValue()) {
                result.add(new Triple<>(line.getKey(), column, word));
            }
        }
        return result;
    }

    public Set<Integer> getLineNumbers() {
        Set<Integer> result = new ProbeHashSet<>();
        for (int lineNumber : positions.keySet()) {
            result.add(lineNumber);
        }
        return result;
    }


}
