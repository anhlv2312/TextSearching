package comp3506.assn2.application;

import comp3506.assn2.adts.*;
import comp3506.assn2.utils.Pair;

import java.io.FileNotFoundException;

public class Searcher {

    private Map<String, Integer> works;
    private Map<Integer, String> lines;
    private List<String> stopWords;
    private Trie<IndexTable> indexTables;

    public Searcher(String documentFileName, String indexFileName, String stopWordsFileName)
            throws FileNotFoundException, IllegalArgumentException {

        if (indexFileName != null && indexFileName.length() > 0) {
            works = Utility.getIndexes(indexFileName);
        } else {
            works = new ProbeHashMap<>();
            works.put("", 0);
        }

        if (stopWordsFileName != null && stopWordsFileName.length() > 0) {
            stopWords = Utility.getStopWords(stopWordsFileName);
        } else {
            stopWords = new ArrayList<>();
        }

        if (documentFileName != null && documentFileName.length() > 0) {
            lines = Utility.getLines(documentFileName);
        } else {
            throw new IllegalArgumentException();
        }

        indexTables = buildWordIndexes(lines);
    }

    public String getLine(int lineNumber) {
        return lines.get(lineNumber);
    }

    private Trie<IndexTable> buildWordIndexes(Map<Integer, String> lines) {
        Trie<IndexTable> wordIndexes = new Trie<>();
        for (Map.Entry<Integer, String> line : lines.entrySet()) {
            Map<Integer, String> tokens = Utility.tokenizeString(line.getValue());
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

    public List<Pair<Integer, Integer>> phraseOccurrence(String phrase) throws IllegalArgumentException {
        if (phrase == null || phrase.length() == 0) {
            throw new IllegalArgumentException();
        }

        List<Pair<Integer, Integer>> result = new ArrayList<>();

        String pattern = Utility.sanitizeString(phrase);
        pattern = Utility.removeContinuousSpaces(pattern) + " ";
        String firstWord = pattern.split(" ")[0];

        IndexTable indexTable = indexTables.getElement(firstWord);

        if (indexTable == null) {
            return result;
        }

        for (Pair<Integer, Integer> position : indexTable.getPositions()) {
            String text = lines.get(position.getLeftValue());
            text = Utility.sanitizeString(text);
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

}
