package comp3506.assn2.application;

import comp3506.assn2.adts.*;
import comp3506.assn2.utils.Pair;
import comp3506.assn2.utils.Triple;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SearchApplication {

    private List<Pair<String, Integer>> workIndexes;
    private List<String> stopWords;
    private Map<String, Work> works;

    public SearchApplication(String documentFileName, String indexFileName, String stopWordsFileName)
            throws FileNotFoundException, IllegalArgumentException {

        workIndexes = new ArrayList<>();
        stopWords = new ArrayList<>();
        works = new ProbeHashMap<>();

        getWorkIndexes(indexFileName);

        if (documentFileName != null && documentFileName.length() > 0) {
            buildWordIndexes(documentFileName);
        } else {
            throw new IllegalArgumentException();
        }
        getStopWords(stopWordsFileName);

    }

    public int wordCount(String word) throws IllegalArgumentException {
        int result = 0;
        for (Work work: works.values()) {
            result += work.wordCount(word);
        }
        return result;
    }

    public Set<Pair<Integer, Integer>> phraseOccurrence(String phrase) throws IllegalArgumentException {
        Set<Pair<Integer, Integer>> result = new ProbeHashSet<>();
        for (Work work: works.values()) {
            result.addAll(work.phraseOccurrence(phrase));
        }
        return result;
    }

    public Set<Pair<Integer,Integer>> prefixOccurrence(String prefix) throws IllegalArgumentException {
        Set<Pair<Integer,Integer>> result = new ProbeHashSet<>();
        for (Work work: works.values()) {
            result.addAll(work.prefixOccurrence(prefix));
        }
        return result;
    }

    public Set<Integer> wordsOnLine(String[] words) throws IllegalArgumentException {
        Set<Integer> result = new ProbeHashSet<>();
        for (Work work: works.values()) {
            result.addAll(work.wordsOnLine(words));
        }
        return result;
    }

    public Set<Integer> someWordsOnLine(String[] words) throws IllegalArgumentException {
        Set<Integer> result = new ProbeHashSet<>();
        for (Work work: works.values()) {
            result.addAll(work.someWordsOnLine(words));
        }
        return result;
    }


    public Set<Integer> wordsNotOnLine(String[] wordsRequired, String[] wordsExcluded) throws IllegalArgumentException {
        Set<Integer> result = new ProbeHashSet<>();
        for (Work work: works.values()) {
            result.addAll(work.wordsNotOnLine(wordsRequired, wordsExcluded));
        }
        return result;
    }


//    public Set<Triple<Integer,Integer,String>> simpleAndSearch(String[] titles, String[] words)
//            throws IllegalArgumentException {
//
//        Set<Triple<Integer, Integer, String>> result = new ProbeHashSet<>();
//        for (Work work: works.values()) {
//            result.addAll(work.wordsOnLine(words));
//        }
//        return result;
//
//    }

    private void buildWordIndexes(String documentFileName) throws FileNotFoundException {
        int lineNumber = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(documentFileName))) {
            String line;
            int lastLine;
            for (int i = 0; i < workIndexes.size(); i++) {
                String title = workIndexes.get(i).getLeftValue();
                if (i + 1 == workIndexes.size()) {
                    lastLine = -1;
                } else {
                    lastLine = workIndexes.get(i + 1).getRightValue() - 1;
                }
                Work work = new Work(title);
                while ((lastLine < 0 || lineNumber < lastLine-1)) {
                    line = br.readLine();
                    if (line != null) {
                        lineNumber++;
                        work.addLine(lineNumber, line);
                    } else {
                        break;
                    }

                }
                works.put(title, work);
            }
        } catch (IOException ex) {
            throw new FileNotFoundException(documentFileName);
        }
    }

    private void getWorkIndexes(String indexFileName) throws FileNotFoundException {
        workIndexes.add(new Pair<>("", 0));
        if (indexFileName != null && indexFileName.length() > 0) {
            try (BufferedReader br = new BufferedReader(new FileReader(indexFileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String title = line.split(",")[0].trim();
                    int startLine = Integer.parseInt(line.split(",")[1].trim());
                    workIndexes.add(new Pair<>(title, startLine));
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
