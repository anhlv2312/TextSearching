package comp3506.assn2.application;

import comp3506.assn2.utils.Pair;
import comp3506.assn2.utils.Triple;

import java.util.List;
import java.util.ArrayList;
import java.io.FileNotFoundException;


/**
 * Hook class used by automated testing tool.
 * The testing tool will instantiate an object of this class to test the functionality of your assignment.
 * You must implement the constructor stub below and override the methods from the Search interface
 * so that they call the necessary code in your application.
 *
 * @author
 */
public class AutoTester implements Search {

    private SearchApplication searchApplication;

    /**
     * Create an object that performs search operations on a document.
     * If indexFileName or stopWordsFileName are null or an empty string the document should be loaded
     * and all searches will be across the entire document with no stop words.
     * All files are expected to be in the files sub-directory and
     * file names are to include the relative path to the files (e.g. "files\\shakespeare.txt").
     *
     * @param documentFileName  Name of the file containing the text of the document to be searched.
     * @param indexFileName     Name of the file containing the index of sections in the document.
     * @param stopWordsFileName Name of the file containing the stop words ignored by most searches.
     * @throws FileNotFoundException    if any of the files cannot be loaded.
     *                                  The name of the file(s) that could not be loaded should be passed
     *                                  to the FileNotFoundException's constructor.
     * @throws IllegalArgumentException if documentFileName is null or an empty string.
     */
    public AutoTester(String documentFileName, String indexFileName, String stopWordsFileName)
            throws FileNotFoundException, IllegalArgumentException {
        // TODO Implement constructor to load the data from these files and
        // TODO setup your data structures for the application.
        searchApplication = new SearchApplication(documentFileName, indexFileName, stopWordsFileName);
    }

    public int wordCount(String word) throws IllegalArgumentException {
        return searchApplication.wordCount(word);
    }

    public List<Pair<Integer, Integer>> prefixOccurrence(String phrase) throws IllegalArgumentException {
        List<Pair<Integer, Integer>> result = new ArrayList<>();
        for (Pair<Integer, Integer> pair: searchApplication.prefixOccurrence(phrase)) {
            result.add(pair);
        }
        return result;
    }

    public List<Pair<Integer, Integer>> phraseOccurrence(String phrase) throws IllegalArgumentException {
        List<Pair<Integer, Integer>> result = new ArrayList<>();
        for (Pair<Integer, Integer> pair: searchApplication.phraseOccurrence(phrase)) {
            result.add(pair);
        }
        return result;
    }

    public List<Integer> wordsOnLine(String[] words) throws IllegalArgumentException {
        List<Integer> result = new ArrayList<>();
        for (int lineNumber: searchApplication.wordsOnLine(words)) {
            result.add(lineNumber);
        }
        return result;
    }


    public List<Integer> someWordsOnLine(String[] words) throws IllegalArgumentException {
        List<Integer> result = new ArrayList<>();
        for (int lineNumber: searchApplication.someWordsOnLine(words)) {
            result.add(lineNumber);
        }
        return result;
    }

    public List<Integer> wordsNotOnLine(String[] wordsRequired, String[] wordsExcluded) throws IllegalArgumentException {
        List<Integer> result = new ArrayList<>();
        for (int lineNumber: searchApplication.wordsNotOnLine(wordsRequired, wordsExcluded)) {
            result.add(lineNumber);
        }
        return result;
    }

    public List<Triple<Integer,Integer,String>> simpleAndSearch(String[] titles, String[] words)
            throws IllegalArgumentException {
        List<Triple<Integer,Integer,String>> result = new ArrayList<>();
        for (Triple<Integer,Integer,String> triple: searchApplication.simpleAndSearch(titles, words)) {
            result.add(triple);
        }
        return result;
    }

    public List<Triple<Integer,Integer,String>> simpleOrSearch(String[] titles, String[] words)
            throws IllegalArgumentException {
        List<Triple<Integer,Integer,String>> result = new ArrayList<>();
        for (Triple<Integer,Integer,String> triple: searchApplication.simpleOrSearch(titles, words)) {
            result.add(triple);
        }
        return result;
    }

}
