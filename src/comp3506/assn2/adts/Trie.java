package comp3506.assn2.adts;

/**
 * Adapted from:
 * https://www.geeksforgeeks.org/longest-prefix-matching-a-trie-based-solution-in-java/
 * https://www.baeldung.com/trie-java
 */
public class Trie<T> {

    private TrieNode<T> root;

    public Trie() {
        root = new TrieNode<>();
    }

    public void insert(String word) {
        // Find length of the given word
        TrieNode<T> current = root;

        // Traverse through all characters of given word
        for (int i = 0; i < word.length(); i++) {
            Map<Character, TrieNode<T>> children = current.getChildren();
            char ch = word.charAt(i);
            // If there is already a child for current character of given word
            current = children.get(ch);
            if (current == null) {
                TrieNode<T> temp = new TrieNode<>();
                children.put(ch, temp);
                current = temp;
            }
        }

        // Set Word Flag is true for last character
        current.setWord();
    }

    private TrieNode<T> findNode(String word) {
        TrieNode<T> current = root;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            if (current != null) {
                current = current.getChildren().get(ch);
            }
        }
        return current;
    }

    public List<T> getDescendantElements(String prefix) {
        List<T> elements = new ArrayList<>();
        TrieNode<T> current = findNode(prefix);
        getChildElements(current, elements);
        return elements;
    }

    private void getChildElements(TrieNode<T> current, List<T> elements){
        if (current.isWord()) {
            elements.add(current.getElement());
        }
        for (TrieNode<T> child : current.getChildren().values()) {
            getChildElements(child, elements);
        }
    }

    public void setElement(String word, T element) {
        TrieNode<T> current = findNode(word);
        if (current != null && current.isWord()) {
            current.setElement(element);
        }
    }

    public T getElement(String word) {
        TrieNode<T> current = findNode(word);
        if (current != null && current.isWord()) {
            return current.getElement();
        } else {
            return null;
        }
    }

    private class TrieNode<E> {
        private Map<Character, TrieNode<E>> children;
        private E element;
        private boolean flag;

        private TrieNode() {
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
