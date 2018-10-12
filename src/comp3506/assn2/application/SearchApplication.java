package comp3506.assn2.application;

import comp3506.assn2.adts.*;
import comp3506.assn2.utils.Pair;
import comp3506.assn2.utils.Triple;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * SearchApplication class that handle all the search task
 *
 * Space Complexity: O(n) with n is the number of word in the document
 *
 * @author Vu Anh LE <vuanh.le@uq.edu.au>
 */
public class SearchApplication {

    private Set<String> stopWords;
    private Map<String, Section> sections;
    private String[] allTitles;
    private Trie<Character> characterTrie;
    private Map<Character, String> characterMap;

    /**
     * Constructor
     *
     * Time complexity: O(n) with n is the number of word in the document
     *
     */
    public SearchApplication(String documentFileName, String indexFileName, String stopWordsFileName)
            throws FileNotFoundException, IllegalArgumentException {

        if (documentFileName == null || documentFileName.length() == 0) {
            throw new IllegalArgumentException();
        }

        characterMap = generateCharacterMap();
        characterTrie = generateCharacterTrie(characterMap);

        // load the index
        List<Pair<String, Integer>> indexes = loadIndexes(indexFileName);

        // load stop words
        stopWords = loadStopWords(stopWordsFileName);

        // load the whole document
        sections = loadDocument(documentFileName, indexes);

        // add all titles of the document to an array
        allTitles = new String[indexes.size()];
        for (int i = 0; i < allTitles.length; i++) {
            allTitles[i] = indexes.get(i).getLeftValue();
        }

    }

    /**
     * Determines the number of times the word appears in the document.
     *
     * Time Complexity: O(s) with s is the number of sections
     *
     * @param word The word to be counted in the document.
     * @return The number of occurrences of the word in the document.
     * @throws IllegalArgumentException if word is null or an empty String.
     */
    public int wordCount(String word) throws IllegalArgumentException {
        int result = 0;
        // loop through all sections in the document
        for (Section section : sections.values()) {
            // add result of each individual section to the whole result
            result += section.wordCount(word);
        }
        return result;
    }

    /**
     * Finds all occurrences of the phrase in the document.
     * A phrase may be a single word or a sequence of words.
     *
     * Time Complexity: O(nm) n is the number of occurrence of the first word in the phrase
     * and m is the number of characters in the phrase
     *
     * @param phrase The phrase to be found in the document.
     * @return List of pairs, where each pair indicates the line and column number of each occurrence of the phrase.
     *         Returns an empty list if the phrase is not found in the document.
     * @throws IllegalArgumentException if phrase is null or an empty String.
     */
    public Set<Pair<Integer, Integer>> phraseOccurrence(String phrase)
            throws IllegalArgumentException {
        Set<Pair<Integer, Integer>> result = new ProbeHashSet<>();
        // loop through all sections in the document
        for (Section section : sections.values()) {
            // add result of each individual section to the whole result
            result.addAll(section.phraseOccurrence(phrase));
        }
        return result;

    }

    /**
     * Finds all occurrences of the prefix in the document.
     * A prefix is the start of a word. It can also be the complete word.
     * For example, "obscure" would be a prefix for "obscure", "obscured", "obscures" and "obscurely".
     *
     * Time Complexity: O(n) with n is the number of occurrences of the word that matches the prefix
     *
     * @param prefix The prefix of a word that is to be found in the document.
     * @return List of pairs, where each pair indicates the line and column number of each occurrence of the prefix.
     *         Returns an empty list if the prefix is not found in the document.
     * @throws IllegalArgumentException if prefix is null or an empty String.
     */
    public Set<Pair<Integer, Integer>> prefixOccurrence(String prefix)
            throws IllegalArgumentException {
        Set<Pair<Integer, Integer>> result = new ProbeHashSet<>();
        // loop through all sections in the document
        for (Section section : sections.values()) {
            // add result of each individual section to the whole result
            result.addAll(section.prefixOccurrence(prefix));
        }
        return result;
    }

    /**
     * Searches the document for lines that contain all the words in the 'words' parameter.
     * Implements simple "and" logic when searching for the words.
     * The words do not need to be contiguous on the line.
     *
     * Time Complexity: O(nms) with n is the number of time the first word of the phrase occurs
     * m is the number of characters in the phrase and s is the number of section
     *
     * @param words Array of words to find on a single line in the document.
     * @return List of line numbers on which all the words appear in the document.
     *         Returns an empty list if the words do not appear in any line in the document.
     * @throws IllegalArgumentException if words is null or an empty array
     *                                  or any of the Strings in the array are null or empty.
     */
    public Set<Integer> wordsOnLine(String[] words) throws IllegalArgumentException {
        Set<Integer> result = new ProbeHashSet<>();
        // loop through all sections in the document
        for (Section section : sections.values()) {
            // add result of each individual section to the whole result
            result.addAll(section.wordsOnLine(words, stopWords));
        }
        return result;
    }

    /**
     * Searches the document for lines that contain any of the words in the 'words' parameter.
     * Implements simple "or" logic when searching for the words.
     * The words do not need to be contiguous on the line.
     *
     * Time Complexity: O(nms) with n is the number of time the first word of the phrase occurs
     * m is the number of characters in the phrase and s is the number of section
     *
     * @param words Array of words to find on a single line in the document.
     * @return List of line numbers on which any of the words appear in the document.
     *         Returns an empty list if none of the words appear in any line in the document.
     * @throws IllegalArgumentException if words is null or an empty array
     *                                  or any of the Strings in the array are null or empty.
     */
    public Set<Integer> someWordsOnLine(String[] words) throws IllegalArgumentException {
        Set<Integer> result = new ProbeHashSet<>();
        // loop through all sections in the document
        for (Section section : sections.values()) {
            // add result of each individual section to the whole result
            result.addAll(section.someWordsOnLine(words, stopWords));
        }
        return result;
    }

    /**
     * Searches the document for lines that contain all the words in the 'wordsRequired' parameter
     * and none of the words in the 'wordsExcluded' parameter.
     * Implements simple "not" logic when searching for the words.
     * The words do not need to be contiguous on the line.
     *
     * @param wordsRequired Array of words to find on a single line in the document.
     * @param wordsExcluded Array of words that must not be on the same line as 'wordsRequired'.
     * @return List of line numbers on which all the wordsRequired appear
     *         and none of the wordsExcluded appear in the document.
     *         Returns an empty list if no lines meet the search criteria.
     * @throws IllegalArgumentException if either of wordsRequired or wordsExcluded are null or an empty array
     *                                  or any of the Strings in either of the arrays are null or empty.
     */
    public Set<Integer> wordsNotOnLine(String[] wordsRequired, String[] wordsExcluded)
            throws IllegalArgumentException {
        Set<Integer> result = new ProbeHashSet<>();
        // loop through all sections in the document
        for (Section section : sections.values()) {
            // add result of each individual section to the whole result
            result.addAll(section.wordsNotOnLine(wordsRequired, wordsExcluded, stopWords));
        }
        return result;
    }

    /**
     * Searches the document for sections that contain all the words in the 'words' parameter.
     * Implements simple "and" logic when searching for the words.
     * The words do not need to be on the same lines.
     *
     * @param titles Array of titles of the sections to search within,
     *               the entire document is searched if titles is null or an empty array.
     * @param words Array of words to find within a defined section in the document.
     * @return List of triples, where each triple indicates the line and column number and word found,
     *         for each occurrence of one of the words.
     *         Returns an empty list if the words are not found in the indicated sections of the document,
     *         or all the indicated sections are not part of the document.
     * @throws IllegalArgumentException if words is null or an empty array
     *                                  or any of the Strings in either of the arrays are null or empty.
     */
    public Set<Triple<Integer, Integer, String>> simpleAndSearch(String[] titles, String[] words)
            throws IllegalArgumentException {

        // if title is null or empty then add use all titles list
        if (titles == null || titles.length == 0) {
            titles = allTitles;
        }

        Set<Triple<Integer, Integer, String>> result = new ProbeHashSet<>();
        // loop through each title
        for (String title : titles) {
            // for each section in the title list
            Section section = sections.get(title);
            if (section != null) {
                // add result of each individual section to the whole result
                result.addAll(section.simpleAndSearch(words, stopWords));
            }
        }
        return result;

    }

    /**
     * Searches the document for sections that contain any of the words in the 'words' parameter.
     * Implements simple "or" logic when searching for the words.
     * The words do not need to be on the same lines.
     *
     * @param titles Array of titles of the sections to search within,
     *               the entire document is searched if titles is null or an empty array.
     * @param words Array of words to find within a defined section in the document.
     * @return List of triples, where each triple indicates the line and column number and word found,
     *         for each occurrence of one of the words.
     *         Returns an empty list if the words are not found in the indicated sections of the document,
     *         or all the indicated sections are not part of the document.
     * @throws IllegalArgumentException if words is null or an empty array
     *                                  or any of the Strings in either of the arrays are null or empty.
     */
    public Set<Triple<Integer, Integer, String>> simpleOrSearch(String[] titles, String[] words)
            throws IllegalArgumentException {

        // if title is null or empty then add use all titles list
        if (titles == null || titles.length == 0) {
            titles = allTitles;
        }

        Set<Triple<Integer, Integer, String>> result = new ProbeHashSet<>();
        for (String title : titles) {
            // for each section in the title list
            Section section = sections.get(title);
            if (section != null) {
                // add result of each individual section to the whole result
                result.addAll(section.simpleOrSearch(words, stopWords));
            }
        }
        return result;

    }
    /**
     * Searches the document for sections that contain all the words in the 'wordsRequired' parameter
     * and none of the words in the 'wordsExcluded' parameter.
     * Implements simple "not" logic when searching for the words.
     * The words do not need to be on the same lines.
     *
     * @param titles Array of titles of the sections to search within,
     *               the entire document is searched if titles is null or an empty array.
     * @param wordsRequired Array of words to find within a defined section in the document.
     * @param wordsExcluded Array of words that must not be in the same section as 'wordsRequired'.
     * @return List of triples, where each triple indicates the line and column number and word found,
     *         for each occurrence of one of the required words.
     *         Returns an empty list if the words are not found in the indicated sections of the document,
     *         or all the indicated sections are not part of the document.
     * @throws IllegalArgumentException if wordsRequired is null or an empty array
     *                                  or any of the Strings in any of the arrays are null or empty.
     */
    public Set<Triple<Integer, Integer, String>> simpleNotSearch(String[] titles,
                                                                 String[] wordsRequired,
                                                                 String[] wordsExcluded)
            throws IllegalArgumentException {

        // if title is null or empty then add use all titles list
        if (titles == null || titles.length == 0) {
            titles = allTitles;
        }

        Set<Triple<Integer, Integer, String>> result = new ProbeHashSet<>();
        for (String title : titles) {
            // for each section in the title list
            Section section = sections.get(title);
            if (section != null) {
                // add result of each individual section to the whole result
                result.addAll(section.simpleNotSearch(wordsRequired, wordsExcluded, stopWords));
            }
        }
        return result;

    }

    /**
     * Searches the document for sections that contain all the words in the 'wordsRequired' parameter
     * and at least one of the words in the 'orWords' parameter.
     * Implements simple compound "and/or" logic when searching for the words.
     * The words do not need to be on the same lines.
     *
     * @param titles Array of titles of the sections to search within,
     *               the entire document is searched if titles is null or an empty array.
     * @param wordsRequired Array of words to find within a defined section in the document.
     * @param orWords Array of words, of which at least one, must be in the same section as 'wordsRequired'.
     * @return List of triples, where each triple indicates the line and column number and word found,
     *         for each occurrence of one of the words.
     *         Returns an empty list if the words are not found in the indicated sections of the document,
     *         or all the indicated sections are not part of the document.
     * @throws IllegalArgumentException if wordsRequired is null or an empty array
     *                                  or any of the Strings in any of the arrays are null or empty.
     */
    public Set<Triple<Integer, Integer, String>> compoundAndOrSearch(String[] titles,
                                                                     String[] wordsRequired,
                                                                     String[] orWords)
            throws IllegalArgumentException {

        // if title is null or empty then add use all titles list
        if (titles == null || titles.length == 0) {
            titles = allTitles;
        }

        Set<Triple<Integer, Integer, String>> result = new ProbeHashSet<>();
        for (String title : titles) {
            // for each section in the title list
            Section section = sections.get(title);
            if (section != null) {
                // add result of each individual section to the whole result
                result.addAll(section.compoundAndOrSearch(wordsRequired, orWords, stopWords));
            }
        }


        return result;
    }


    /** load the whole documents in to the data structure */
    private Map<String, Section> loadDocument(String documentFileName, List<Pair<String, Integer>> indexes)
            throws FileNotFoundException {
        Map<String, Section> sections = new ProbeHashMap<>();

        int lineNumber = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(documentFileName))) {
            String line;
            int lastLine;

            // for each section in the index
            for (int i = 0; i < indexes.size(); i++) {

                String title = indexes.get(i).getLeftValue();

                // determine the last line of the current section
                if (i + 1 < indexes.size()) {
                    // if has next section, the last line start line of the next section -1
                    lastLine = indexes.get(i + 1).getRightValue() - 1;
                } else {
                    // else last line = -1
                    lastLine = -1;
                }

                Section section = new Section(characterMap, characterTrie);

                // keep adding line to section object until reaching the last line
                while ((lastLine < 0 || lineNumber < lastLine - 1)) {
                    line = br.readLine();
                    if (line != null) {
                        lineNumber++;
                        section.addLine(lineNumber, line);
                    } else {
                        break;
                    }
                }

                // add the current section to the map
                sections.put(title, section);
            }
        } catch (IOException ex) {
            throw new FileNotFoundException(documentFileName);
        }
        return sections;
    }


    /** load the index file data in to a list of Pair<title, line number> */
    private List<Pair<String, Integer>> loadIndexes(String indexFileName) throws FileNotFoundException {

        // We use arrayList because it reserve the ordering of titles when they are read from file
        List<Pair<String, Integer>> indexes = new ArrayList<>();
        // add the initial title (represent the whole document)
        indexes.add(new Pair<>("", 0));

        if (indexFileName != null && indexFileName.length() > 0) {
            try (BufferedReader br = new BufferedReader(new FileReader(indexFileName))) {
                String line;

                // Read each line
                while ((line = br.readLine()) != null) {

                    // get the index of the last comma in line
                    int i = line.lastIndexOf(",");

                    // Set first part as the title
                    String title = line.substring(0, i).trim();

                    // Get the line number
                    int startLine = Integer.parseInt(line.substring(i+1).trim());

                    // Add the pair into the list
                    indexes.add(new Pair<>(title, startLine));
                }
            } catch (IOException ex) {
                throw new FileNotFoundException(indexFileName);
            }
        }
        return indexes;
    }

    /** load the stop words in to a set */
    private Set<String> loadStopWords(String stopWordsFileName) throws FileNotFoundException {
        Set<String> stopWords = new ProbeHashSet<>();
        if (stopWordsFileName != null && stopWordsFileName.length() > 0) {
            // initialize  the file reader
            try (BufferedReader br = new BufferedReader(new FileReader(stopWordsFileName))) {
                String line;
                // for each line, add a word into the set
                while ((line = br.readLine()) != null) {
                    // add the stop word to the set
                    stopWords.add(line.toLowerCase().trim());
                }
            } catch (IOException ex) {
                throw new FileNotFoundException(stopWordsFileName);
            }
        }
        return stopWords;
    }

    private static Map<Character, String> generateCharacterMap() {
        Map<Character, String> characterMap = new ProbeHashMap<>();
        characterMap.put(' ',"0");
        characterMap.put('e',"1001");
        characterMap.put('t',"1010");
        characterMap.put('r',"11000");
        characterMap.put('s',"11001");
        characterMap.put('i',"11010");
        characterMap.put('n',"11011");
        characterMap.put('h',"10001");
        characterMap.put('o',"11101");
        characterMap.put('a',"11110");
        characterMap.put('c',"100000");
        characterMap.put('u',"100001");
        characterMap.put('l',"111001");
        characterMap.put('f',"101100");
        characterMap.put('m',"101101");
        characterMap.put('y',"101110");
        characterMap.put('d',"111111");
        characterMap.put('v',"1011110");
        characterMap.put('k',"1011111");
        characterMap.put('p',"1110000");
        characterMap.put('b',"1110001");
        characterMap.put('g',"1111100");
        characterMap.put('w',"1111101");
        characterMap.put('x',"101111100");
        characterMap.put('q',"1011111010");
        characterMap.put('j',"10111110110");
        characterMap.put('z',"10111110111");
        return characterMap;
    }


    private static Trie<Character> generateCharacterTrie(Map<Character, String> characterMap) {
        Trie<Character> characterTrie = new Trie<>();
        for (Map.Entry<Character, String> entry: characterMap.entrySet()) {
            characterTrie.insert(entry.getValue());
            characterTrie.setElement(entry.getValue(), entry.getKey());
        }
        return characterTrie;
    }

}
