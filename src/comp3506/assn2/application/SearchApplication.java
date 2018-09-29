package comp3506.assn2.application;

import comp3506.assn2.adts.*;
import comp3506.assn2.utils.Pair;
import comp3506.assn2.utils.Triple;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SearchApplication {

    private List<Pair<String, Integer>> indexes;
    private Set<String> stopWords;
    private Map<String, Section> sections;

    public SearchApplication(String documentFileName, String indexFileName, String stopWordsFileName)
            throws FileNotFoundException, IllegalArgumentException {

        if (documentFileName == null || documentFileName.length() == 0) {
            throw new IllegalArgumentException();
        }

        indexes = new ArrayList<>();
        stopWords = new ProbeHashSet<>();
        sections = new ProbeHashMap<>();

        getStopWords(stopWordsFileName);
        getWorkIndexes(indexFileName);
        buildWordIndexes(documentFileName);

    }

    public int wordCount(String word) throws IllegalArgumentException {
        int result = 0;
        for (Section section : sections.values()) {
            result += section.wordCount(word);
        }
        return result;
    }

    public Set<Pair<Integer, Integer>> phraseOccurrence(String phrase) throws IllegalArgumentException {
        Set<Pair<Integer, Integer>> result = new ProbeHashSet<>();
        for (Section section : sections.values()) {
            result.addAll(section.phraseOccurrence(phrase));
        }
        return result;
    }

    public Set<Pair<Integer, Integer>> prefixOccurrence(String prefix) throws IllegalArgumentException {
        Set<Pair<Integer, Integer>> result = new ProbeHashSet<>();
        for (Section section : sections.values()) {
            result.addAll(section.prefixOccurrence(prefix));
        }
        return result;
    }

    public Set<Integer> wordsOnLine(String[] words) throws IllegalArgumentException {
        Set<Integer> result = new ProbeHashSet<>();
        for (Section section : sections.values()) {
            result.addAll(section.wordsOnLine(words, stopWords));
        }
        return result;
    }

    public Set<Integer> someWordsOnLine(String[] words) throws IllegalArgumentException {
        Set<Integer> result = new ProbeHashSet<>();
        for (Section section : sections.values()) {
            result.addAll(section.someWordsOnLine(words, stopWords));
        }
        return result;
    }


    public Set<Integer> wordsNotOnLine(String[] wordsRequired, String[] wordsExcluded) throws IllegalArgumentException {
        Set<Integer> result = new ProbeHashSet<>();
        for (Section section : sections.values()) {
            result.addAll(section.wordsNotOnLine(wordsRequired, wordsExcluded, stopWords));
        }
        return result;
    }


    public Set<Triple<Integer, Integer, String>> simpleAndSearch(String[] titles, String[] words)
            throws IllegalArgumentException {

        if (titles == null || titles.length == 0) {
            titles = new String[indexes.size()];
            for (int i = 0; i < titles.length; i++) {
                titles[i] = indexes.get(i).getLeftValue();
            }
        }

        Set<Triple<Integer, Integer, String>> result = new ProbeHashSet<>();
        for (String title : titles) {
            Section section = sections.get(title);
            if (section != null) {
                result.addAll(section.simpleAndSearch(words, stopWords));
            }
        }
        return result;

    }


    public Set<Triple<Integer, Integer, String>> simpleOrSearch(String[] titles, String[] words)
            throws IllegalArgumentException {

        if (titles == null || titles.length == 0) {
            titles = new String[indexes.size()];
            for (int i = 0; i < titles.length; i++) {
                titles[i] = indexes.get(i).getLeftValue();
            }
        }

        Set<Triple<Integer, Integer, String>> result = new ProbeHashSet<>();
        for (String title : titles) {
            Section section = sections.get(title);
            if (section != null) {
                result.addAll(section.simpleOrSearch(words, stopWords));
            }
        }
        return result;

    }

    public Set<Triple<Integer, Integer, String>> simpleNotSearch(String[] titles, String[] wordsRequired, String[] wordsExcluded)
            throws IllegalArgumentException {

        if (titles == null || titles.length == 0) {
            titles = new String[indexes.size()];
            for (int i = 0; i < titles.length; i++) {
                titles[i] = indexes.get(i).getLeftValue();
            }
        }

        Set<Triple<Integer, Integer, String>> result = new ProbeHashSet<>();
        for (String title : titles) {
            Section section = sections.get(title);
            if (section != null) {
                result.addAll(section.simpleNotSearch(wordsRequired, wordsExcluded, stopWords));
            }
        }
        return result;

    }

    public Set<Triple<Integer, Integer, String>> compoundAndOrSearch(String[] titles, String[] wordsRequired, String[] orWords)
            throws IllegalArgumentException {

        if (titles == null || titles.length == 0) {
            titles = new String[indexes.size()];
            for (int i = 0; i < titles.length; i++) {
                titles[i] = indexes.get(i).getLeftValue();
            }
        }

        Set<Triple<Integer, Integer, String>> result = new ProbeHashSet<>();
        for (String title : titles) {
            Section section = sections.get(title);
            if (section != null) {
                result.addAll(section.compoundAndOrSearch(wordsRequired, orWords, stopWords));
            }
        }


        return result;
    }

    private void buildWordIndexes(String documentFileName) throws FileNotFoundException {
        int lineNumber = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(documentFileName))) {
            String line;
            int lastLine;
            for (int i = 0; i < indexes.size(); i++) {
                String title = indexes.get(i).getLeftValue();
                if (i + 1 == indexes.size()) {
                    lastLine = -1;
                } else {
                    lastLine = indexes.get(i + 1).getRightValue() - 1;
                }
                Section section = new Section();
                while ((lastLine < 0 || lineNumber < lastLine - 1)) {
                    line = br.readLine();
                    if (line != null) {
                        lineNumber++;
                        section.addLine(lineNumber, line);
                    } else {
                        break;
                    }

                }
                sections.put(title, section);
            }
        } catch (IOException ex) {
            throw new FileNotFoundException(documentFileName);
        }
    }

    private void getWorkIndexes(String indexFileName) throws FileNotFoundException {
        indexes.add(new Pair<>("", 0));
        if (indexFileName != null && indexFileName.length() > 0) {
            try (BufferedReader br = new BufferedReader(new FileReader(indexFileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String title = line.split(",")[0].trim();
                    int startLine = Integer.parseInt(line.split(",")[1].trim());
                    indexes.add(new Pair<>(title, startLine));
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
                    stopWords.add(line.toLowerCase().trim());
                }
            } catch (IOException ex) {
                throw new FileNotFoundException(stopWordsFileName);
            }
        }
    }


}
