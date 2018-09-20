package comp3506.assn2.application;

import comp3506.assn2.adts.ArrayList;
import comp3506.assn2.adts.List;
import comp3506.assn2.adts.Map;
import comp3506.assn2.adts.Trie;
import comp3506.assn2.utils.Pair;

import java.io.FileNotFoundException;

public class Searcher {

    private Map<String, Integer> indexes;
    private Map<Integer, String> lines;
    private List<String> stopWords;
    private Trie<IndexTable> wordIndexes;


    public Searcher(String documentFileName, String indexFileName, String stopWordsFileName)
            throws FileNotFoundException, IllegalArgumentException {

        if (indexFileName != null && indexFileName.length() > 0) {
            indexes = Utility.getIndexes(indexFileName);
        } else {
            indexes = new ProbeHashMap<>();
            indexes.put("", 0);
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

        wordIndexes = buildWordIndexes(lines);
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
//                System.out.print(word.getValue() + " ");
                IndexTable wordIndex = wordIndexes.getElement(word.getValue());
                if (wordIndex == null) {
                    wordIndex = new IndexTable(word.getValue());
                    wordIndexes.setElement(word.getValue(), wordIndex);
                }
                wordIndex.addPosition(line.getKey(), word.getKey());
            }
//            System.out.println();
        }
        return wordIndexes;
    }



    public int wordCount(String word) throws IllegalArgumentException {

        if (word == null || word.length() == 0) {
            throw new IllegalArgumentException();
        }

        IndexTable wordIndex = wordIndexes.getElement(word.toLowerCase().trim());
        if (wordIndex != null) {
            return wordIndex.size();
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

        IndexTable wordIndex = wordIndexes.getElement(firstWord);

        if (wordIndex != null) {
            for (Pair<Integer, Integer> position : wordIndex.getPositions()) {
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
        } else {
            System.out.println(pattern);
            System.out.println(firstWord);
        }

        return result;

    }

    public List<Pair<Integer,Integer>> prefixOccurrence(String prefix) throws IllegalArgumentException {
        List<Pair<Integer,Integer>> result = new ArrayList<>();
        for (IndexTable indexTable: wordIndexes.getDescendantElements(prefix.toLowerCase().trim())) {
            for (Pair<Integer, Integer> position: indexTable.getPositions()) {
                result.add(position);
            }
        }
        return result;
    }

}
