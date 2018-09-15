package comp3506.assn2.application;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Line {

    private String text;

    public Line(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public List<String> getWords() {
        List<String> words = new ArrayList<>();
        Pattern p = Pattern.compile("[a-zA-Z]+\'?+[a-zA-Z]");
        Matcher m = p.matcher(text.toLowerCase());
        while (m.find()) {
            words.add(words.size(), m.group());
        }
        return words;
    }

}
