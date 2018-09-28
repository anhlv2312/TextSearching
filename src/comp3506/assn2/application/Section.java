package comp3506.assn2.application;

import comp3506.assn2.adts.*;
import comp3506.assn2.utils.Pair;

public class Section {

    private Map<Integer, String> lines;
    private Trie<IndexTable> indexTables;

    public Section(String title) {
        indexTables = new Trie<>();
        lines = new ProbeHashMap<>();
    }

    public void addLine(int lineNumber, String text) {
        if (text.trim().length() == 0) {
            return;
        }
        lines.put(lineNumber, text);
        Map<Integer, String> tokens = Utility.tokenizeString(text);

        for (Map.Entry<Integer, String> token: tokens.entrySet()) {
            int position = token.getKey();
            String word = token.getValue();

            indexTables.insert(token.getValue());
            IndexTable indexTable = indexTables.getElement(word);
            if (indexTable == null) {
                indexTable = new IndexTable();
                indexTables.setElement(word, indexTable);
            }
            indexTable.addPosition(lineNumber, position);
        }
    }

    public int wordCount(String word) throws IllegalArgumentException {

        if (word == null || word.length() == 0) {
            throw new IllegalArgumentException();
        }

        int result = 0;

        IndexTable indexTable = indexTables.getElement(word.toLowerCase().trim());
        if (indexTable != null) {
            result += indexTable.size();
        }

        return result;
    }

    public Set<Pair<Integer, Integer>> phraseOccurrence(String phrase) throws IllegalArgumentException {
        if (phrase == null || phrase.length() == 0) {
            throw new IllegalArgumentException();
        }

        Set<Pair<Integer, Integer>> result = new ProbeHashSet<>();

        String pattern = Utility.sanitizeString(phrase);
        pattern = Utility.removeContinuousSpaces(pattern) + " ";
        String firstWord = pattern.split(" ")[0];
        IndexTable indexTable = indexTables.getElement(firstWord);

        if (indexTable != null) {
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
        }
        return result;
    }

    public Set<Pair<Integer,Integer>> prefixOccurrence(String prefix) throws IllegalArgumentException {
        if (prefix == null || prefix.length() == 0) {
            throw new IllegalArgumentException();
        }
        Set<Pair<Integer,Integer>> result = new ProbeHashSet<>();
        for (IndexTable indexTable : indexTables.getDescendantElements(prefix.toLowerCase().trim())) {
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

        for (String word : words) {
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

        for (String word : wordsExcluded) {
            IndexTable indexTable = indexTables.getElement(word.toLowerCase().trim());
            if (indexTable != null) {
                result.removeAll(indexTable.getLines());
            }
        }

        return result;
    }

}
