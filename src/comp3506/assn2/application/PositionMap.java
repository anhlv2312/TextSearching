package comp3506.assn2.application;

import comp3506.assn2.adts.*;
import comp3506.assn2.utils.Pair;

/* [1 pp. 449] */
public class PositionMap {

    private int total = 0;
    private ProbeHashMap<Integer, List<Integer>> positions;

    public PositionMap() {
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

    public List<Pair<Integer, Integer>> getPositions() {
        ArrayList<Pair<Integer, Integer>> result = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> line : positions.entrySet()) {
            for (int column : line.getValue()) {
                result.add(new Pair<>(line.getKey(), column));
            }
        }
        return result;
    }

    public ProbeHashSet<Integer> getLines() {
        ProbeHashSet<Integer> result = new ProbeHashSet<>();
        for (int lineNumber : positions.keySet()) {
            result.add(lineNumber);
        }
        return result;
    }


}
