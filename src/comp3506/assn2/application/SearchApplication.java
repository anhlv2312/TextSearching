package comp3506.assn2.application;

import comp3506.assn2.adts.*;
import comp3506.assn2.utils.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SearchApplication {

    private Map<String, Integer> works;
    private Map<Integer, String> lines;
    private List<String> stopWords;
    private Trie<IndexTable> indexTables;

    public SearchApplication(String documentFileName, String indexFileName, String stopWordsFileName)
            throws FileNotFoundException, IllegalArgumentException {

        if (indexFileName != null && indexFileName.length() > 0) {
            works = getIndexes(indexFileName);
        } else {
            works = new ProbeHashMap<>();
            works.put("", 0);
        }

        if (stopWordsFileName != null && stopWordsFileName.length() > 0) {
            stopWords = getStopWords(stopWordsFileName);
        } else {
            stopWords = new ArrayList<>();
        }

        if (documentFileName != null && documentFileName.length() > 0) {
            lines = getLines(documentFileName);
        } else {
            throw new IllegalArgumentException();
        }

        indexTables = buildWordIndexes(lines);
    }

    // TODO: fix this also
    private Trie<IndexTable> buildWordIndexes(Map<Integer, String> lines) {
        Trie<IndexTable> wordIndexes = new Trie<>();
        for (Map.Entry<Integer, String> line : lines.entrySet()) {
            Map<Integer, String> tokens = tokenizeString(line.getValue());
            for (Map.Entry<Integer, String> word: tokens.entrySet()) {
                wordIndexes.insert(word.getValue());
                IndexTable wordIndex = wordIndexes.getElement(word.getValue());
                if (wordIndex == null) {
                    wordIndex = new IndexTable();
                    wordIndexes.setElement(word.getValue(), wordIndex);
                }
                wordIndex.addPosition(line.getKey(), word.getKey());
            }
        }
        return wordIndexes;
    }


    public int wordCount(String word) throws IllegalArgumentException {

        if (word == null || word.length() == 0) {
            throw new IllegalArgumentException();
        }

        IndexTable indexTable = indexTables.getElement(word.toLowerCase().trim());
        if (indexTable != null) {
            return indexTable.size();
        } else {
            return 0;
        }
    }


    // TODO: refine this function
    public List<Pair<Integer, Integer>> phraseOccurrence(String phrase) throws IllegalArgumentException {
        if (phrase == null || phrase.length() == 0) {
            throw new IllegalArgumentException();
        }

        List<Pair<Integer, Integer>> result = new ArrayList<>();

        String pattern = sanitizeString(phrase);
        pattern = removeContinuousSpaces(pattern) + " ";
        String firstWord = pattern.split(" ")[0];

        IndexTable indexTable = indexTables.getElement(firstWord);

        if (indexTable == null) {
            return result;
        }

        for (Pair<Integer, Integer> position : indexTable.getPositions()) {
            String text = lines.get(position.getLeftValue());
            text = sanitizeString(text);
            text = text.substring(position.getRightValue() - 1);
            if (text.length() >= pattern.length()) {
                boolean match = true;
                for (int i = 0; i < pattern.length(); i++) {
                    if (text.charAt(i) != pattern.charAt(i)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    result.add(position);
                }
            }
        }
        return result;
    }

    public List<Pair<Integer,Integer>> prefixOccurrence(String prefix) throws IllegalArgumentException {
        if (prefix == null || prefix.length() == 0) {
            throw new IllegalArgumentException();
        }
        List<Pair<Integer,Integer>> result = new ArrayList<>();
        for (IndexTable indexTable: indexTables.getDescendantElements(prefix.toLowerCase().trim())) {
            for (Pair<Integer, Integer> position: indexTable.getPositions()) {
                result.add(position);
            }
        }
        return result;
    }


    // TODO: refine this function
    public Set<Integer> wordsOnLine(String[] words) throws IllegalArgumentException {
        if (words == null || words.length == 0) {
            throw new IllegalArgumentException();
        }

        for (String word: words) {
            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }
        }

        Set<Integer> result = null;
        for (String word: words) {
            Set<Integer> lineNumbers;
            IndexTable indexTable = indexTables.getElement(word.toLowerCase().trim());
            if (indexTable == null) {
                return new ProbeHashSet<>();
            } else {
                lineNumbers = indexTable.getLines();
                if (result == null) {
                    result = lineNumbers;
                } else {
                    result.retainAll(lineNumbers);
                }
            }

        }
        return result;
    }

    public Set<Integer> someWordsOnLine(String[] words) throws IllegalArgumentException {
        if (words == null || words.length == 0) {
            throw new IllegalArgumentException();
        }

        for (String word: words) {
            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }
        }

        Set<Integer> result = new ProbeHashSet<>();

        for (String word: words) {
            IndexTable indexTable = indexTables.getElement(word.toLowerCase().trim());
            if (indexTable != null) {
                result.addAll(indexTable.getLines());
            }
        }


        return result;
    }


    public Set<Integer> wordsNotOnLine(String[] wordsRequired, String[] wordsExcluded) throws IllegalArgumentException {
        if (wordsExcluded == null || wordsExcluded.length == 0) {
            throw new IllegalArgumentException();
        }

        for (String word: wordsExcluded) {
            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }
        }

        Set<Integer> result = wordsOnLine(wordsRequired);

        for (String word: wordsExcluded) {
            IndexTable indexTable = indexTables.getElement(word.toLowerCase().trim());
            if (indexTable != null) {
                result.removeAll(indexTable.getLines());
            }
        }


        return result;
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
                tokens.put((i + 1) - sb.length() + 1, sb.toString());
            }
        }
        return tokens;
    }

    public static String sanitizeString(String string) {
        return string.toLowerCase().replaceAll("[^0-9a-z ']", " ").replaceAll("' | '", "  ");
    }

    private static String removeContinuousSpaces(String string) {
        return string.toLowerCase().replaceAll(" +", " ").trim();
    }


    private static Map<Integer, String> getLines(String documentFileName) throws FileNotFoundException {
        Map<Integer, String> lines = new ProbeHashMap<>();
        int lineNumber = 1;
        try (BufferedReader br = new BufferedReader(new FileReader(documentFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
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

    private static Map<String, Integer> getIndexes(String indexFileName) throws FileNotFoundException {
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

    private static List<String> getStopWords(String stopWordsFileName) throws FileNotFoundException {
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

}
