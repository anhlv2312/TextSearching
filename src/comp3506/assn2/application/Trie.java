package comp3506.assn2.application;

/**
 * Adapted from:
 * https://www.geeksforgeeks.org/longest-prefix-matching-a-trie-based-solution-in-java/
 * https://www.baeldung.com/trie-java
 */
public class Trie<T> {

    private TrieNode<T> root;

    public Trie() {
        root = new TrieNode<>((char) 0);
    }

    public void insert(String word) {

        // Find length of the given word
        int length = word.length();
        TrieNode<T> crawl = root;

        // Traverse through all characters of given word
        for (int level = 0; level < length; level++) {
            Map<Character, TrieNode<T>> child = crawl.getChildren();
            char ch = word.charAt(level);

            // If there is already a child for current character of given word
            crawl = child.get(ch);
            if (crawl == null) {
                TrieNode<T> temp = new TrieNode<T>(ch);
                child.put(ch, temp);
                crawl = temp;
            }
        }

        // Set bIsEnd true for last character
        crawl.setWord();
    }

    private TrieNode<T> findNode(String word) {
        TrieNode<T> current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (current != null) {
                current = current.getChildren().get(ch);
            }
        }

        if (current != null && current.isWord()) {
            return current;
        }

        return null;

    }

    public void setElement(String word, T element) {
        TrieNode<T> current = findNode(word);
        if (current != null) {
            current.setElement(element);
        }
    }

    public T getElement(String word) {
        TrieNode<T> current = findNode(word);
        if (current != null) {
            return current.getElement();
        } else {
            return null;
        }
    }


    private class TrieNode<E> {
        private Map<Character, TrieNode<E>> children;
        private E element;
        private boolean flag;

        private TrieNode(char ch) {
            children = new ProbeHashMap<>();
            flag = false;
            element = null;
        }

        private Map<Character, TrieNode<E>> getChildren() {
            return children;
        }

        private void setWord() {
            flag = true;
        }

        private boolean isWord() {
            return flag;
        }

        private E getElement() {
            return element;
        }

        private void setElement(E element) {
            this.element = element;
        }


    }


}
