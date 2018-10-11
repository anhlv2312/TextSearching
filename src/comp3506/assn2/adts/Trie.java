package comp3506.assn2.adts;


/**
 * A standard trie storing a collection of words and the elements T associated with that words
 *
 * Space complexity: 0(n) total size of the distinct words in the document
 *
 * @author Vu Anh LE <vuanh.le@uq.edu.au>
 */

public class Trie<T> {

    private TrieNode<T> root;

    public Trie() {
        root = new TrieNode<>();
    }

    /**
     * Insert a word to the this trie
     *
     * Time complexity: O(n) with n is the number of characters in the word
     *
     * @param word the word to insert to the trie.
     */
    public void insert(String word) {
        // set the root of the trie as the current node, start traversing
        TrieNode<T> current = root;

        // for each character in the given word
        for (int i = 0; i < word.length(); i++) {

            // get all the child node of current node of the current char
            Map<Character, TrieNode<T>> children = current.getChildren();
            char c = word.charAt(i);

            // try no set current node to the node of current char
            current = children.get(c);

            // If there is no node for the current character
            if (current == null) {
                // initialize a new node
                TrieNode<T> temp = new TrieNode<>();
                children.put(c, temp);
                current = temp;
            }
        }

        // Set Word Flag is true for last character
        current.setWord();
    }

    /**
     * Find a trie node given a word
     *
     * Time complexity: O(n) with n is the number of characters in the word
     *
     * @param word the word to insert to the trie.
     * @return a trie node of the word
     */
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

    /**
     * A recursive method to get elements of all the descendants of the current node
     *
     * Time complexity: O(dn) with d is the depth of subtree from current node and n is the size of the alphabet
     *
     * @param current the word to insert to the trie.
     * @param elements a reference to a list of element
     */
    private void getChildElements(TrieNode<T> current, List<T> elements){
        // if current node is a word then add it to the given list
        if (current.isWord()) {
            elements.add(current.getElement());
        }
        // for each child of the current node
        for (TrieNode<T> child : current.getChildren().values()) {
            // recursively add the elements of the child
            getChildElements(child, elements);
        }
    }

    /**
     * Get all the descendants' element from a given node which represent the prefix
     *
     * Time complexity: O(dn) with d is the depth of subtree from current node and n is the size of the alphabet
     *
     * @param prefix the word to insert to the trie.
     * @return a trie node of the word
     */
    public List<T> getDescendantElements(String prefix) {
        List<T> elements = new ArrayList<>();

        // find the node represent the prefix
        TrieNode<T> current = findNode(prefix);

        // if found then call the recursive function to get all of it descendants' element
        if (current != null) {
            getChildElements(current, elements);
        }
        return elements;
    }

    /**
     * Set element for a word node
     *
     * Time complexity: O(n) with n is the number of characters in word
     *
     * @param word the word
     * @param element the element to be set
     */
    public void setElement(String word, T element) {
        // find the node
        TrieNode<T> current = findNode(word);
        // if found and the node is a word
        if (current != null && current.isWord()) {
            // set its element
            current.setElement(element);
        }
    }

    /**
     * get the element of a word node
     *
     * Time complexity: O(n) with n is the number of characters in word
     *
     * @param word the word that need to retrieve the element
     */
    public T getElement(String word) {
        // find the node
        TrieNode<T> current = findNode(word);
        // if found and the node is a word
        if (current != null && current.isWord()) {
            // return its element
            return current.getElement();
        } else {
            return null;
        }
    }


    /**
     * A nested class that represent the node of a trie
     */
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
