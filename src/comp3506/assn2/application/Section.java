package comp3506.assn2.application;

import comp3506.assn2.adts.*;
import comp3506.assn2.utils.Pair;

public class Section {

    private Map<Integer, String> lines;
    private Trie<PositionMap> positionTrie;

    public Section() {
        positionTrie = new Trie<>();
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

            positionTrie.insert(token.getValue());
            PositionMap positionMap = positionTrie.getElement(word);
            if (positionMap == null) {
                positionMap = new PositionMap();
                positionTrie.setElement(word, positionMap);
            }
            positionMap.addPosition(lineNumber, position);
        }
    }

    public int wordCount(String word) throws IllegalArgumentException {

        if (word == null || word.length() == 0) {
            throw new IllegalArgumentException();
        }

        int result = 0;

        PositionMap positionMap = positionTrie.getElement(word.toLowerCase().trim());
        if (positionMap != null) {
            result += positionMap.size();
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
        PositionMap positionMap = positionTrie.getElement(firstWord);

        if (positionMap != null) {
            for (Pair<Integer, Integer> position : positionMap.getPositions()) {
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
        for (PositionMap positionMap : positionTrie.getDescendantElements(prefix.toLowerCase().trim())) {
            for (Pair<Integer, Integer> position: positionMap.getPositions()) {
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
            PositionMap positionMap = positionTrie.getElement(word.toLowerCase().trim());
            if (positionMap == null) {
                return new ProbeHashSet<>();
            } else {
                lineNumbers = positionMap.getLines();
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
            PositionMap positionMap = positionTrie.getElement(word.toLowerCase().trim());
            if (positionMap != null) {
                result.addAll(positionMap.getLines());
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
            PositionMap positionMap = positionTrie.getElement(word.toLowerCase().trim());
            if (positionMap != null) {
                result.removeAll(positionMap.getLines());
            }
        }

        return result;
    }

}
