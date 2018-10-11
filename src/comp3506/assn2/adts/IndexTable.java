package comp3506.assn2.adts;

import comp3506.assn2.utils.Pair;
import comp3506.assn2.utils.Triple;

/* [1 pp. 449] */

/**
 * IndexTable class which act like a multimap stores the positions of a word
 * the key is the line number and the value is the list of column
 *
 * @author Vu Anh LE <vuanh.le@uq.edu.au>
 */
public class IndexTable {

    private int total = 0;
    private String word;
    private Map<Integer, List<Integer>> positions;

    /**
     * Constructor
     *
     * @param word the word to be stored
     */
    public IndexTable(String word) {
        this.word = word;
        positions = new ProbeHashMap<>();
    }

    /**
     * Return the total number of positions.
     *
     * @return The total number of the positions
     */
    public int size() {
        return total;
    }

    /**
     * Add a entry into the map
     *
     * @param line The lineNumber that contain the word
     * @param column The word to be counted in the document.
     */
    public void addPosition(int line, int column) {
        List<Integer> secondary = positions.get(line);
        // if the secondary list is not set
        if (secondary == null) {
            // create a new list
            secondary = new ArrayList<>();
            // add the new list to the map
            positions.put(line, secondary);
        }

        // add the column number to secondary list
        secondary.add(column);
        total++;
    }

    /**
     * Get all the position pairs (line number, column)
     *
     * @return The Set of pair that contains line and column
     */
    public Set<Pair<Integer, Integer>> getPositionPairs() {
        Set<Pair<Integer, Integer>> result = new ProbeHashSet<>();
        // for each entry in the map
        for (Map.Entry<Integer, List<Integer>> line : positions.entrySet()) {
            // for each entry in the secondary list
            for (int column : line.getValue()) {
                result.add(new Pair<>(line.getKey(), column));
            }
        }
        return result;
    }

    /**
     * Get all the position triple (word, line number, column)
     *
     * @return The Set of triple that contains line and column and the word itself
     */
    public Set<Triple<Integer, Integer, String>> getPositionTriples() {
        Set<Triple<Integer, Integer, String>> result = new ProbeHashSet<>();
        // for each entry in the map
        for (Map.Entry<Integer, List<Integer>> line : positions.entrySet()) {
            // for each entry in the secondary list
            for (int column : line.getValue()) {
                result.add(new Triple<>(line.getKey(), column, word));
            }
        }
        return result;
    }

    /**
     * Get all the distinct line number of the lines that contain the word
     *
     * @return The Set line number that contains the word
     */
    public Set<Integer> getLineNumbers() {
        Set<Integer> result = new ProbeHashSet<>();
        // for each item in the key set
        for (int lineNumber : positions.keySet()) {
            // Add it to the result set
            result.add(lineNumber);
        }
        return result;
    }


}
