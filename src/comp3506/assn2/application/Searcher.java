package comp3506.assn2.application;

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
            indexes.put(null, 0);
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

        wordIndexes = Utility.buildWordIndexes(lines);
    }

    public int wordCount(String word) throws IllegalArgumentException {

        if (word == null || word.length() == 0) {
            throw new IllegalArgumentException();
        }

        IndexTable wordIndex = wordIndexes.getElement(word.trim().toLowerCase());
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
        String firstWord = pattern.split(" ")[0];

        IndexTable wordIndex = wordIndexes.getElement(firstWord);
        if (wordIndex != null) {
            for (IndexTable.Position position : wordIndex.getPositions()) {

                String text = lines.get(position.getLine()).substring(position.getColumn() - 1);

                int match = Utility.findKMP(text.toCharArray(), pattern.toCharArray());
                if (match >= 0) {
                    result.add(result.size(), new Pair<>(position.getLine(), match + position.getColumn()));
                }
            }
        } else {
            System.out.println(pattern);
            System.out.println(firstWord);
        }

        return result;

    }

}
