package comp3506.assn2.application;

import comp3506.assn2.adts.*;
import comp3506.assn2.utils.Pair;
import comp3506.assn2.utils.Triple;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SearchApplication {

    private Map<Integer, String> workIndexes;
    private List<String> stopWords;
    private Map<Integer, String> lines;
    private Trie<IndexTable> indexTables;

    public SearchApplication(String documentFileName, String indexFileName, String stopWordsFileName)
            throws FileNotFoundException, IllegalArgumentException {

        lines = new ProbeHashMap<>();
        indexTables = new Trie<>();
        workIndexes = new ProbeHashMap<>();
        stopWords = new ArrayList<>();

        getWorkIndexes(indexFileName);

        for (int a : workIndexes.keySet()){
            System.out.println(a);
        }

        if (documentFileName != null && documentFileName.length() > 0) {
            buildWordIndexes(documentFileName);
        } else {
            throw new IllegalArgumentException();
        }


        getStopWords(stopWordsFileName);

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
        for (IndexTable indexTable : indexTables.getDescendantElements(prefix.toLowerCase().trim())) {
            for (Pair<Integer, Integer> position: indexTable.getPositions()) {
                result.add(position);
            }
        }
        return result;
    }


    // TODO: refine this function, can find for the lines contains first word then check every line
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


//    public List<Triple<Integer,Integer,String>> simpleAndSearch(String[] titles, String[] words)
//            throws IllegalArgumentException {
//        if (titles != null && titles.length workIndexes.get(titles) == null) {
//
//        }
//    }






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

    private static String removeContinuousSpaces(String string) {
        return string.toLowerCase().replaceAll(" +", " ").trim();
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

            indexTables.insert(token.getValue());
            IndexTable indexTable = indexTables.getElement(word);
            if (indexTable == null) {
                indexTable = new IndexTable();
                indexTables.setElement(word, indexTable);
            }
            indexTable.addPosition(lineNumber, position);
        }
    }

    private void getWorkIndexes(String indexFileName) throws FileNotFoundException {
        if (indexFileName != null && indexFileName.length() > 0) {
            try (BufferedReader br = new BufferedReader(new FileReader(indexFileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    int startLine = Integer.parseInt(line.split(",")[1].trim());
                    workIndexes.put(startLine, line.split(",")[0].trim());
                }
            } catch (IOException ex) {
                throw new FileNotFoundException(indexFileName);
            }
        }
    }

    private void getStopWords(String stopWordsFileName) throws FileNotFoundException {
        if (stopWordsFileName != null && stopWordsFileName.length() > 0) {
            try (BufferedReader br = new BufferedReader(new FileReader(stopWordsFileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    stopWords.add(line.trim());
                }
            } catch (IOException ex) {
                throw new FileNotFoundException(stopWordsFileName);
            }
        }
    }

}
