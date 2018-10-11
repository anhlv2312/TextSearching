package comp3506.assn2.adts;

import comp3506.assn2.utils.Pair;
import comp3506.assn2.utils.Triple;

/**
 * Section Class that represent the sections in the section
 *
 * @author Vu Anh LE <vuanh.le@uq.edu.au>
 */
public class Section {

    // a map that store all the line number and text of that line
    private Map<Integer, String> lines;
    // a trie that store all the position map (reversed index table) of each word
    private Trie<IndexTable> positionTrie;

    public Section() {
        positionTrie = new Trie<>();
        lines = new ProbeHashMap<>();
    }

    /**
     * Add a line content into the internal map that store all the line in the section
     *
     * @param lineNumber the index of the line
     * @param text the text
     */
    public void addLine(int lineNumber, String text) {
        if (text.trim().length() == 0) {
            return;
        }
        // put the lineNumber and text in to the map
        lines.put(lineNumber, text);

        // generate the token to add to the trie
        Map<Integer, String> tokens = tokenizeString(text);

        // for each token in the line
        for (Map.Entry<Integer, String> token : tokens.entrySet()) {
            int position = token.getKey();
            String word = token.getValue();

            positionTrie.insert(token.getValue());

            // try to get the position map from the trie
            IndexTable indexTable = positionTrie.getElement(word);

            // if not exist then initialize a new position map
            if (indexTable == null) {
                indexTable = new IndexTable(word);
                positionTrie.setElement(word, indexTable);
            }

            // add the position to position map
            indexTable.addPosition(lineNumber, position);
        }
    }

    /**
     * Determines the number of times the word appears in the section.
     *
     * @param word The word to be counted in the section.
     * @return The number of occurrences of the word in the section.
     * @throws IllegalArgumentException if word is null or an empty String.
     */
    public int wordCount(String word) throws IllegalArgumentException {

        if (word == null || word.length() == 0) {
            throw new IllegalArgumentException();
        }

        // get the reversed index of the word
        IndexTable indexTable = positionTrie.getElement(word.toLowerCase().trim());
        if (indexTable != null) {
            // return the number of position in index table
            return indexTable.size();
        } else {
            return 0;
        }
    }

    /**
     * Finds all occurrences of the phrase in the section.
     * A phrase may be a single word or a sequence of words.
     *
     * @param phrase The phrase to be found in the section.
     * @return List of pairs, where each pair indicates the line and column number of each occurrence of the phrase.
     *         Returns an empty list if the phrase is not found in the section.
     * @throws IllegalArgumentException if phrase is null or an empty String.
     */
    public Set<Pair<Integer, Integer>> phraseOccurrence(String phrase) throws IllegalArgumentException {
        if (phrase == null || phrase.length() == 0) {
            throw new IllegalArgumentException();
        }

        Set<Pair<Integer, Integer>> result = new ProbeHashSet<>();

        // Tokenize the phrase, remove all special characters
        // add a space at the end to mark the end of the phrase
        String pattern = sanitizeString(phrase) + " ";

        // Remove continuous spaces from the pattern
        pattern = replaceContinuousSpaces(pattern);

        // Extract the first word of the phrase
        String firstWord = pattern.split(" ")[0];

        // Get the position table of the first word
        IndexTable indexTable = positionTrie.getElement(firstWord);

        if (indexTable != null) {

            for (Pair<Integer, Integer> position : indexTable.getPositionPairs()) {

                // get the line number of each position
                int lineNumber = position.getLeftValue();

                // get the string of the line that contains the first word
                String text = lines.get(lineNumber);

                // Remove the previous part of the text, only consider the text from the position of the word
                text = text.substring(position.getRightValue() - 1);

                // keep adding next lines text to the text if the length of text is less than the pattern
                StringBuilder sb = new StringBuilder(text);
                while (sb.length() < pattern.length()) {
                    lineNumber++;
                    String nextLine = lines.get(lineNumber);
                    if (nextLine != null) {
                        sb.append(" ");
                        sb.append(nextLine.trim());
                    }
                }
                text = sb.toString();

                // Sanitize the text string
                text = sanitizeString(text + " ");
                text = replaceContinuousSpaces(text);

                // Comparing the pattern with the text char by char
                boolean match = true;
                for (int i = 0; i < pattern.length(); i++) {
                    if (text.charAt(i) != pattern.charAt(i)) {
                        match = false;
                        break;
                    }
                }

                // if they are match add the position of the first word to the result list
                if (match) {
                    result.add(position);
                }

            }
        }
        return result;
    }

    /**
     * Finds all occurrences of the prefix in the section.
     * A prefix is the start of a word. It can also be the complete word.
     * For example, "obscure" would be a prefix for "obscure", "obscured", "obscures" and "obscurely".
     *
     * @param prefix The prefix of a word that is to be found in the section.
     * @return List of pairs, where each pair indicates the line and column number of each occurrence of the prefix.
     *         Returns an empty list if the prefix is not found in the section.
     * @throws IllegalArgumentException if prefix is null or an empty String.
     */
    public Set<Pair<Integer, Integer>> prefixOccurrence(String prefix)
            throws IllegalArgumentException {
        if (prefix == null || prefix.length() == 0) {
            throw new IllegalArgumentException();
        }
        Set<Pair<Integer, Integer>> result = new ProbeHashSet<>();
        for (IndexTable indexTable : positionTrie.getDescendantElements(prefix.toLowerCase().trim())) {
            for (Pair<Integer, Integer> position : indexTable.getPositionPairs()) {
                result.add(position);
            }
        }
        return result;
    }

    /**
     * Searches the section for lines that contain all the words in the 'words' parameter.
     * Implements simple "and" logic when searching for the words.
     * The words do not need to be contiguous on the line.
     *
     * @param words Array of words to find on a single line in the section.
     * @param stopWords Set of stopWords to be ignored.
     * @return List of line numbers on which all the words appear in the section.
     *         Returns an empty list if the words do not appear in any line in the section.
     * @throws IllegalArgumentException if words is null or an empty array
     *                                  or any of the Strings in the array are null or empty.
     */
    public Set<Integer> wordsOnLine(String[] words, Set<String> stopWords)
            throws IllegalArgumentException {
        if (words == null || words.length == 0) {
            throw new IllegalArgumentException();
        }

        Set<Integer> result = new ProbeHashSet<>();

        for (String word : words) {
            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }

            if (stopWords.contains(word.toLowerCase().trim())) {
                continue;
            }

            Set<Integer> lineNumbers;
            IndexTable indexTable = positionTrie.getElement(word.toLowerCase().trim());
            if (indexTable == null) {
                return new ProbeHashSet<>();
            } else {
                lineNumbers = indexTable.getLineNumbers();
                if (result.size() == 0) {
                    result = lineNumbers;
                } else {
                    result.retainAll(lineNumbers);
                }
            }

        }
        return result;
    }

    /**
     * Searches the section for lines that contain any of the words in the 'words' parameter.
     * Implements simple "or" logic when searching for the words.
     * The words do not need to be contiguous on the line.
     *
     * @param words Array of words to find on a single line in the section.
     * @param stopWords Set of stopWords to be ignored.
     * @return List of line numbers on which any of the words appear in the section.
     *         Returns an empty list if none of the words appear in any line in the section.
     * @throws IllegalArgumentException if words is null or an empty array
     *                                  or any of the Strings in the array are null or empty.
     */
    public Set<Integer> someWordsOnLine(String[] words, Set<String> stopWords)
            throws IllegalArgumentException {
        if (words == null || words.length == 0) {
            throw new IllegalArgumentException();
        }

        Set<Integer> result = new ProbeHashSet<>();
        for (String word : words) {

            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }

            if (stopWords.contains(word.toLowerCase().trim())) {
                continue;
            }

            IndexTable indexTable = positionTrie.getElement(word.toLowerCase().trim());
            if (indexTable != null) {
                result.addAll(indexTable.getLineNumbers());
            }
        }

        return result;
    }

    /**
     * Searches the section for lines that contain all the words in the 'wordsRequired' parameter
     * and none of the words in the 'wordsExcluded' parameter.
     * Implements simple "not" logic when searching for the words.
     * The words do not need to be contiguous on the line.
     *
     * @param wordsRequired Array of words to find on a single line in the section.
     * @param wordsExcluded Array of words that must not be on the same line as 'wordsRequired'.
     * @param stopWords Set of stopWords to be ignored.
     * @return List of line numbers on which all the wordsRequired appear
     *         and none of the wordsExcluded appear in the section.
     *         Returns an empty list if no lines meet the search criteria.
     * @throws IllegalArgumentException if either of wordsRequired or wordsExcluded are null or an empty array
     *                                  or any of the Strings in either of the arrays are null or empty.
     */
    public Set<Integer> wordsNotOnLine(String[] wordsRequired, String[] wordsExcluded, Set<String> stopWords)
            throws IllegalArgumentException {
        if (wordsRequired == null || wordsRequired.length == 0
                || wordsExcluded == null || wordsExcluded.length == 0) {
            throw new IllegalArgumentException();
        }

        for (String word : wordsRequired) {
            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }
        }

        Set<Integer> result = wordsOnLine(wordsRequired, stopWords);

        for (String word : wordsExcluded) {

            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }

            if (stopWords.contains(word.toLowerCase().trim())) {
                continue;
            }


            IndexTable indexTable = positionTrie.getElement(word.toLowerCase().trim());
            if (indexTable != null) {
                result.removeAll(indexTable.getLineNumbers());
            }
        }

        return result;
    }

    /**
     * Searches the section for sections that contain all the words in the 'words' parameter.
     * Implements simple "and" logic when searching for the words.
     * The words do not need to be on the same lines.
     *
     * @param words Array of words to find within a defined section in the section.
     * @param stopWords Set of stopWords to be ignored.
     * @return List of triples, where each triple indicates the line and column number and word found,
     *         for each occurrence of one of the words.
     *         Returns an empty list if the words are not found in the indicated sections of the section,
     *         or all the indicated sections are not part of the section.
     * @throws IllegalArgumentException if words is null or an empty array
     *                                  or any of the Strings in either of the arrays are null or empty.
     */
    public Set<Triple<Integer, Integer, String>> simpleAndSearch(String[] words, Set<String> stopWords)
            throws IllegalArgumentException {

        if (words == null || words.length == 0) {
            throw new IllegalArgumentException();
        }

        Set<Triple<Integer, Integer, String>> result = new ProbeHashSet<>();
        for (String word : words) {
            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }

            if (stopWords.contains(word.toLowerCase().trim())) {
                continue;
            }

            IndexTable indexTable = positionTrie.getElement(word.toLowerCase().trim());
            if (indexTable != null) {
                result.addAll(indexTable.getPositionTriples());
            } else {
                return new ProbeHashSet<>();
            }
        }
        return result;
    }

    /**
     * Searches the section for sections that contain any of the words in the 'words' parameter.
     * Implements simple "or" logic when searching for the words.
     * The words do not need to be on the same lines.
     *
     * @param words Array of words to find within a defined section in the section.
     * @param stopWords Set of stopWords to be ignored.
     * @return List of triples, where each triple indicates the line and column number and word found,
     *         for each occurrence of one of the words.
     *         Returns an empty list if the words are not found in the indicated sections of the section,
     *         or all the indicated sections are not part of the section.
     * @throws IllegalArgumentException if words is null or an empty array
     *                                  or any of the Strings in either of the arrays are null or empty.
     */
    public Set<Triple<Integer, Integer, String>> simpleOrSearch(String[] words, Set<String> stopWords)
            throws IllegalArgumentException {

        if (words == null || words.length == 0) {
            throw new IllegalArgumentException();
        }

        Set<Triple<Integer, Integer, String>> result = new ProbeHashSet<>();

        for (String word : words) {
            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }


            if (stopWords.contains(word.toLowerCase().trim())) {
                continue;
            }

            IndexTable indexTable = positionTrie.getElement(word.toLowerCase().trim());
            if (indexTable != null) {
                result.addAll(indexTable.getPositionTriples());
            }
        }
        return result;
    }

    /**
     * Searches the section for sections that contain all the words in the 'wordsRequired' parameter
     * and none of the words in the 'wordsExcluded' parameter.
     * Implements simple "not" logic when searching for the words.
     * The words do not need to be on the same lines.
     *
     * @param wordsRequired Array of words to find within a defined section in the section.
     * @param wordsExcluded Array of words that must not be in the same section as 'wordsRequired'.
     * @param stopWords Set of stopWords to be ignored.
     * @return List of triples, where each triple indicates the line and column number and word found,
     *         for each occurrence of one of the required words.
     *         Returns an empty list if the words are not found in the indicated sections of the section,
     *         or all the indicated sections are not part of the section.
     * @throws IllegalArgumentException if wordsRequired is null or an empty array
     *                                  or any of the Strings in any of the arrays are null or empty.
     */
    public Set<Triple<Integer, Integer, String>> simpleNotSearch(String[] wordsRequired,
                                                                 String[] wordsExcluded,
                                                                 Set<String> stopWords)
            throws IllegalArgumentException {

        if (wordsRequired == null || wordsRequired.length == 0 || wordsExcluded == null || wordsExcluded.length == 0) {
            throw new IllegalArgumentException();
        }

        for (String word : wordsRequired) {
            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }
        }

        for (String word : wordsExcluded) {

            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }

            if (stopWords.contains(word.toLowerCase().trim())) {
                continue;
            }

            IndexTable indexTable = positionTrie.getElement(word.toLowerCase().trim());
            if (indexTable != null) {
                return new ProbeHashSet<>();
            }
        }

        return simpleAndSearch(wordsRequired, stopWords);
    }

    /**
     * Searches the section for sections that contain all the words in the 'wordsRequired' parameter
     * and at least one of the words in the 'orWords' parameter.
     * Implements simple compound "and/or" logic when searching for the words.
     * The words do not need to be on the same lines.
     *
     * @param wordsRequired Array of words to find within a defined section in the section.
     * @param orWords Array of words, of which at least one, must be in the same section as 'wordsRequired'.
     * @param stopWords Set of stopWords to be ignored.
     * @return List of triples, where each triple indicates the line and column number and word found,
     *         for each occurrence of one of the words.
     *         Returns an empty list if the words are not found in the indicated sections of the section,
     *         or all the indicated sections are not part of the section.
     * @throws IllegalArgumentException if wordsRequired is null or an empty array
     *                                  or any of the Strings in any of the arrays are null or empty.
     */
    public Set<Triple<Integer, Integer, String>> compoundAndOrSearch(String[] wordsRequired,
                                                                     String[] orWords,
                                                                     Set<String> stopWords)
            throws IllegalArgumentException {

        if (wordsRequired == null || wordsRequired.length == 0 || orWords == null || orWords.length == 0) {
            throw new IllegalArgumentException();
        }

        for (String word : wordsRequired) {
            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }
        }

        Set<Triple<Integer, Integer, String>> result = simpleAndSearch(wordsRequired, stopWords);

        if (result.size() == 0) {
            return new ProbeHashSet<>();
        }

        Set<Triple<Integer, Integer, String>> orResult = new ProbeHashSet<>();
        for (String word : orWords) {

            if (word == null || word.length() == 0) {
                throw new IllegalArgumentException();
            }

            if (stopWords.contains(word.toLowerCase().trim())) {
                continue;
            }

            IndexTable indexTable = positionTrie.getElement(word.toLowerCase().trim());
            if (indexTable != null) {
                orResult.addAll(simpleOrSearch(orWords, stopWords));

            }
        }

        if (orResult.size() == 0) {
            return new ProbeHashSet<>();
        }

        result.addAll(orResult);

        return result;
    }

    // Split the string into word tokens
    private static Map<Integer, String> tokenizeString(String string) {
        string = sanitizeString(string);
        StringBuilder sb = new StringBuilder();
        Map<Integer, String> tokens = new ProbeHashMap<>();

        // iterate through the string
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) != ' ') {
                // append current char into the string builder until the char is a space
                sb.append(string.charAt(i));
            } else {
                // if it is a space and the previous string builder contains a word
                if (sb.length() > 0) {
                    // add the previous word to tokens list
                    tokens.put(i - sb.length() + 1, sb.toString());
                    // initialize a new string builder
                    sb = new StringBuilder();
                }
            }
            // if there is a word at the end of string
            if ((i == string.length() - 1) && (sb.length() > 0)) {
                // add the last word to the token list
                tokens.put((i + 1) - sb.length() + 1, sb.toString());
            }
        }
        return tokens;
    }

    // return the string that contain only alphanumerical letter and apostrophe
    private static String sanitizeString(String string) {
        return string.toLowerCase().replaceAll("[^0-9a-z ']", " ").replaceAll("' | '", "  ");
    }

    // return the string that has no contiguous space
    private static String replaceContinuousSpaces(String string) {
        return string.toLowerCase().replaceAll(" +", " ").trim();
    }

}
