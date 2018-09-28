package comp3506.assn2.application;

import comp3506.assn2.adts.*;

public class Work {

    private String title;
    private Map<Integer, String> lines;
    public Work(String title) {
        this.title = title;
        lines = new ProbeHashMap<>();
    }

    public void addLine(int lineNumber, String text) {
        lines.put(lineNumber, text);
    }

}
