package comp3506.assn2.application;

import comp3506.assn2.adts.Map;
import comp3506.assn2.adts.ProbeHashMap;

public class Utility {
    public static Map<Integer, String> tokenizeString(String string) {
        string = sanitizeString(string);
        StringBuilder sb = new StringBuilder();
        Map<Integer, String> tokens = new ProbeHashMap<>();
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) != ' ') {
                sb.append(string.charAt(i));
            } else {
                if (sb.length() > 0) {
                    tokens.put(i - sb.length() + 1, sb.toString());
                    sb = new StringBuilder();
                }
            }
            if ((i == string.length() - 1) && (sb.length() > 0)) {
                tokens.put((i + 1) - sb.length() + 1, sb.toString());
            }
        }
        return tokens;
    }

    public static String sanitizeString(String string) {
        return string.toLowerCase().replaceAll("[^0-9a-z ']", " ").replaceAll("' | '", "  ");
    }

    public static String removeContinuousSpaces(String string) {
        return string.toLowerCase().replaceAll(" +", " ").trim();
    }
}
