package comp3506.assn2.application;

import comp3506.assn2.adts.*;
import comp3506.assn2.utils.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SearchApplication {

    private Map<String, Integer> workIndexes;
    private List<String> stopWords;
    private Map<Integer, String> lines;
    private Trie<WordIndex> wordIndexes;

    public SearchApplication(String documentFileName, String indexFileName, String stopWordsFileName)
            throws FileNotFoundException, IllegalArgumentException {

        lines = new ProbeHashMap<>();
        wordIndexes = new Trie<>();

        if (indexFileName != null && indexFileName.length() > 0) {
            workIndexes = getWorkIndexes(indexFileName);
        } else {
            workIndexes = new ProbeHashMap<>();
            workIndexes.put("", 0);
        }

        if (stopWordsFileName != null && stopWordsFileName.length() > 0) {
            stopWords = getStopWords(stopWordsFileName);
        } else {
            stopWords = new ArrayList<>();
        }

        if (documentFileName != null && documentFileName.length() > 0) {
            buildWordIndexes(documentFileName);
        } else {
            throw new IllegalArgumentException();
        }


    }

    private void buildWordIndexes(String documentFileName) throws FileNotFoundException {
        int lineNumber = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(documentFileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                processLine(lineNumber, line);
            }
        } catch (IOException ex) {
            throw new FileNotFoundException(documentFileName);
        }

    }

    private void processLine(int lineNumber, String line) {

        if (line.trim().length() == 0) {
            return;
        }

        lines.put(lineNumber, line);
        Map<Integer, String> tokens = tokenizeString(line);

        for (Map.Entry<Integer, String> token: tokens.entrySet()) {
            int position = token.getKey();
            String word = token.getValue();

            wordIndexes.insert(token.getValue());
            WordIndex wordIndex = wordIndexes.getElement(word);
            if (wordIndex == null) {
                wordIndex = new WordIndex();
                wordIndexes.setElement(word, wordIndex);
            }
            wordIndex.addPosition(lineNumber, position);
        }
    }


    public int wordCount(String word) throws IllegalArgumentException {

        if (word == null || word.length() == 0) {
            throw new IllegalArgumentException();
        }

        WordIndex wordIndex = wordIndexes.getElement(word.toLowerCase().trim());
        if (wordIndex != null) {
            return wordIndex.size();
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

        WordIndex wordIndex = wordIndexes.getElement(firstWord);

        if (wordIndex == null) {
            return result;
        }

        for (Pair<Integer, Integer> position : wordIndex.getPositions()) {
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
        for (WordIndex wordIndex : wordIndexes.getDescendantElements(prefix.toLowerCase().trim())) {
            for (Pair<Integer, Integer> position: wordIndex.getPositions()) {
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
            WordIndex wordIndex = wordIndexes.getElement(word.toLowerCase().trim());
            if (wordIndex == null) {
                return new ProbeHashSet<>();
            } else {
                lineNumbers = wordIndex.getLines();
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
            WordIndex wordIndex = wordIndexes.getElement(word.toLowerCase().trim());
            if (wordIndex != null) {
                result.addAll(wordIndex.getLines());
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
            WordIndex wordIndex = wordIndexes.getElement(word.toLowerCase().trim());
            if (wordIndex != null) {
                result.removeAll(wordIndex.getLines());
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


    private static Map<String, Integer> getWorkIndexes(String indexFileName) throws FileNotFoundException {
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
