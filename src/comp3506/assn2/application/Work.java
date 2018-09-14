package comp3506.assn2.application;

public class Work {
    private String title;
    private int startLine;
    private Map<Integer, Line> lines;

    public Work(String title, int startLine) {
        this.title = title;
        this.startLine = startLine;
        lines = new ProbeHashMap<>();
    }

    public void addLine(int lineNumber, Line line) {
        lines.put(lineNumber, line);
    }

    public String getTitle() {
        return title;
    }

    public int getStartLine() {
        return startLine;
    }

    public Map<Integer, Line> getLines() {
        return lines;
    }
}
