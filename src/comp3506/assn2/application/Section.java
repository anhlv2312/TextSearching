package comp3506.assn2.application;

import comp3506.assn2.adts.*;
import comp3506.assn2.utils.Pair;
import comp3506.assn2.utils.Triple;

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
        Map<Integer, String> tokens = tokenizeString(text);

        for (Map.Entry<Integer, String> token: tokens.entrySet()) {
            int position = token.getKey();
            String word = token.getValue();

            positionTrie.insert(token.getValue());
            PositionMap positionMap = positionTrie.getElement(word);
            if (positionMap == null) {
                positionMap = new PositionMap(word);
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

        String pattern = sanitizeString(phrase);
        String firstWord = pattern.split(" ")[0];
        PositionMap positionMap = positionTrie.getElement(firstWord);

        if (positionMap != null) {
            for (Pair<Integer, Integer> position : positionMap.getPositionPairs()) {
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
        }
        return result;
    }

    public Set<Pair<Integer,Integer>> prefixOccurrence(String prefix) throws IllegalArgumentException {
        if (prefix == null || prefix.length() == 0) {
            throw new IllegalArgumentException();
        }
        Set<Pair<Integer,Integer>> result = new ProbeHashSet<>();
        for (PositionMap positionMap : positionTrie.getDescendantElements(prefix.toLowerCase().trim())) {
            for (Pair<Integer, Integer> position: positionMap.getPositionPairs()) {
                result.add(position);
            }
        }
        return result;
    }


    public Set<Integer> wordsOnLine(String[] words) throws IllegalArgumentException {
        if (words == null || words.length == 0) {
            throw new IllegalArgumentException();
        }

        Set<Integer> result = new ProbeHashSet<>();

        for (String word: words) {
            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }
            Set<Integer> lineNumbers;
            PositionMap positionMap = positionTrie.getElement(word.toLowerCase().trim());
            if (positionMap == null) {
                return new ProbeHashSet<>();
            } else {
                lineNumbers = positionMap.getLineNumbers();
                if (result.size() == 0) {
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

        Set<Integer> result = new ProbeHashSet<>();
        for (String word: words) {
            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }
            PositionMap positionMap = positionTrie.getElement(word.toLowerCase().trim());
            if (positionMap != null) {
                result.addAll(positionMap.getLineNumbers());
            }
        }

        return result;
    }

    public Set<Integer> wordsNotOnLine(String[] wordsRequired, String[] wordsExcluded) throws IllegalArgumentException {
        if (wordsRequired == null || wordsRequired.length == 0 || wordsExcluded == null || wordsExcluded.length == 0) {
            throw new IllegalArgumentException();
        }

        for (String word: wordsRequired) {
            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }
        }

        Set<Integer> result = wordsOnLine(wordsRequired);

        for (String word: wordsExcluded) {
            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }
            PositionMap positionMap = positionTrie.getElement(word.toLowerCase().trim());
            if (positionMap != null) {
                result.removeAll(positionMap.getLineNumbers());
            }
        }

        return result;
    }

    public Set<Triple<Integer,Integer,String>> simpleAndSearch(String[] words)
            throws IllegalArgumentException {

        if (words == null || words.length == 0) {
            throw new IllegalArgumentException();
        }

        Set<Triple<Integer,Integer,String>> result = new ProbeHashSet<>();
        for (String word: words) {
            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }
            PositionMap positionMap = positionTrie.getElement(word.toLowerCase().trim());
            if (positionMap != null) {
                result.addAll(positionMap.getPositionTriples());
            } else {
                return new ProbeHashSet<>();
            }
        }
        return result;
    }

    public Set<Triple<Integer,Integer,String>> simpleOrSearch(String[] words)
            throws IllegalArgumentException {

        if (words == null || words.length == 0) {
            throw new IllegalArgumentException();
        }

        Set<Triple<Integer,Integer,String>> result = new ProbeHashSet<>();

        for (String word: words) {
            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }
            PositionMap positionMap = positionTrie.getElement(word.toLowerCase().trim());
            if (positionMap != null) {
                result.addAll(positionMap.getPositionTriples());
            }
        }
        return result;
    }


    public Set<Triple<Integer,Integer,String>> simpleNotSearch(String[] wordsRequired, String[] wordsExcluded)
            throws IllegalArgumentException {

        if (wordsRequired == null || wordsRequired.length == 0 || wordsExcluded == null || wordsExcluded.length == 0) {
            throw new IllegalArgumentException();
        }

        for (String word: wordsRequired) {
            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }
        }

        for (String word: wordsExcluded) {
            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }

            PositionMap positionMap = positionTrie.getElement(word.toLowerCase().trim());
            if (positionMap != null) {
                return new ProbeHashSet<>();
            }
        }

        return simpleAndSearch(wordsRequired);
    }

    public Set<Triple<Integer,Integer,String>> compoundAndOrSearch(String[] wordsRequired, String[] orWords)
            throws IllegalArgumentException {

        if (wordsRequired == null || wordsRequired.length == 0 || orWords == null || orWords.length == 0) {
            throw new IllegalArgumentException();
        }

        for (String word: wordsRequired) {
            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }
        }

        Set<Triple<Integer,Integer,String>> result = simpleAndSearch(wordsRequired);

        if (result.size() == 0) {
            return new ProbeHashSet<>();
        }

        Set<Triple<Integer, Integer, String>> orResult = new ProbeHashSet<>();
        for (String word : orWords) {
            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }
            PositionMap positionMap = positionTrie.getElement(word.toLowerCase().trim());
            if (positionMap != null) {
                orResult.addAll(simpleOrSearch(orWords));

            }
        }

        if (orResult.size() == 0) {
            return new ProbeHashSet<>();
        }

        result.addAll(orResult);

        return result;
    }

    private static Map<Integer, String> tokenizeString(String string) {
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

    private static String sanitizeString(String string) {
        return string.toLowerCase().replaceAll("[^0-9a-z ']", " ").replaceAll("' | '", "  ");
    }

}
