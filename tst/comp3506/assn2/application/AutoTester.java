package comp3506.assn2.application;

import comp3506.assn2.utils.Pair;

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

    private Map<String, Integer> indexes;
    private Map<Integer, String> lines;
    private List<String> stopWords;
    private Trie<IndexTable> wordIndexes;

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


//        for (String word: stopWords) {
//            System.out.println(word);
//        }
//
//        for (String line: lines.values()) {
//            System.out.println(line);
//        }
//
//        for (String work: works.keySet()) {
//            System.out.println(work);
//        }


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


    public java.util.List<Pair<Integer, Integer>> phraseOccurrence(String phrase) throws IllegalArgumentException {
        if (phrase == null || phrase.length() == 0) {
            throw new IllegalArgumentException();
        }

        java.util.List<Pair<Integer, Integer>> result = new java.util.ArrayList<>();

        String pattern = Utility.sanitizeString(phrase);
        String firstWord = pattern.split(" ")[0];

        IndexTable wordIndex = wordIndexes.getElement(firstWord);
        if (wordIndex != null) {
            for (IndexTable.Position position : wordIndex.getPositions()) {

                String text = lines.get(position.getLine()).substring(position.getColumn());

                int match = Utility.findKMP(text.toCharArray(), pattern.toCharArray());
                if (match >= 0) {
                    result.add(new Pair<>(position.getLine(), match + position.getColumn() + 1));
                }
            }
        } else {
            System.out.println(pattern);
            System.out.println(firstWord);
        }

        return result;

    }
}
