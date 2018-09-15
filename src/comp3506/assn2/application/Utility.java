package comp3506.assn2.application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Utility {

    public static Map<Integer, Line> getLines(String documentFileName) throws FileNotFoundException {
        Map<Integer, Line> lines = new ProbeHashMap<>();
        int lineNumber = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(documentFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.put(lineNumber, new Line(line));
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
                stopWords.add(stopWords.size(), line.trim());
            }
        } catch (IOException ex) {
            throw new FileNotFoundException(stopWordsFileName);
        }
        return stopWords;
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
