package comp3506.assn2.application;

import comp3506.assn2.adts.ArrayList;
import comp3506.assn2.adts.List;
import comp3506.assn2.utils.Pair;

public class IndexTable {

    private String word;
    private List<Pair<Integer, Integer>> positions;

    public IndexTable(String word) {
        this.word = word;
        positions = new ArrayList<>();
    }

    public void addPosition(int line, int column) {
        positions.add(new Pair<>(line, column));
    }

    public List<Pair<Integer, Integer>> getPositions() {
        return positions;
    }

    public String getWord() {
        return word;
    }

    public int size() {
        return positions.size();
    }

}
