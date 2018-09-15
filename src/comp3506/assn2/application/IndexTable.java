package comp3506.assn2.application;

public class IndexTable {

    private String word;
    private List<Position> positions;

    public IndexTable(String word) {
        this.word = word;
        positions = new ArrayList<>();
    }

    public void addPosition(int line, int column) {
        positions.add(positions.size(), new Position(line, column));
    }

    public List<Position> getPositions() {
        return positions;
    }

    public List<Integer> getLineNumbers() {
        List<Integer> lines = new ArrayList<>();
        for (Position position: positions) {
            lines.add(lines.size(), position.getLine());
        }
        return lines;
    }

    public int size() {
        return positions.size();
    }

    public class Position {
        private int line;
        private int column;

        public Position(int line, int column) {
            this.line = line;
            this.column = column;
        }

        public int getLine() {
            return line;
        }

        public int getColumn() {
            return column;
        }
    }
}
