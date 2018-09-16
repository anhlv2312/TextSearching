package comp3506.assn2.application;

import comp3506.assn2.utils.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utility {

    public static Map<Integer, String> getLines(String documentFileName) throws FileNotFoundException {
        Map<Integer, String> lines = new ProbeHashMap<>();
        int lineNumber = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(documentFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line;
                if (line.length() > 0) {
                    lines.put(lineNumber, line);
                }
                lineNumber++;
            }
        } catch (IOException ex) {
            throw new FileNotFoundException(documentFileName);
        }
        return lines;
    }

    public static Map<String, Integer> getIndexes(String indexFileName) throws FileNotFoundException {
        Map<String, Integer> works = new ProbeHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(indexFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String title = line.split(",")[0].trim();
                int startLine = Integer.parseInt(line.split(",")[1].trim());
                works.put(title, startLine);
            }
        } catch (IOException ex) {
            throw new FileNotFoundException(indexFileName);
        }
        return works;
    }

    public static List<String> getStopWords(String stopWordsFileName) throws FileNotFoundException {
        List<String> stopWords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(stopWordsFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                stopWords.add(line.trim());
            }
        } catch (IOException ex) {
            throw new FileNotFoundException(stopWordsFileName);
        }
        return stopWords;
    }

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
                tokens.put(i - sb.length() + 1, sb.toString());
            }
        }
        return tokens;
    }

    public static String sanitizeString(String string) {
        return string.toLowerCase().replaceAll("[^0-9a-z ']"," ").replaceAll("' | '","  ");
    }

    public static String removeContinuousSpaces(String string) {
        return string.toLowerCase().replaceAll(" +"," ").trim();
    }

    public static int findKMP(char[] text, char[] pattern) {
        int n = text.length;
        int m = pattern.length;
        if (m == 0) return 0;
        int[] fail = computeFailKMP(pattern);
        int j = 0;
        int k = 0;
        while (j < n) {
            if (text[j] == pattern[k]) {
                if (k == m - 1) {
                    return j - m + 1;
                }
                j++;
                k++;
            } else if (k > 0) {
                k = fail[k - 1];
            } else {
                j++;
            }
        }
        return -1;
    }

    private static int[] computeFailKMP(char[] pattern) {
        int m = pattern.length;
        int[] fail = new int[m];
        int j = 1;
        int k = 0;
        while (j < m) {
            if (pattern[j] == pattern[k]) {
                fail[j] = k + 1;
                j++;
                k++;
            } else if (k > 0) {
                k = fail[k - 1];
            } else {
                j++;
            }
        }
        return fail;
    }

}
