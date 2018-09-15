package comp3506.assn2.application;

import comp3506.assn2.utils.Pair;

public class ManualTester {

	public static void main(String[] args) throws Exception {
		AutoTester at = new AutoTester("./files/shakespeare.txt",
				"./files/shakespeare-index.txt",
				"./files/stop-words.txt");

		String[] words = {"That's", "is", "test", "go", "queen ", "queen", "i'll"};

		for (String word: words) {
            System.out.println(word + ": " + at.wordCount(word));
        }


        String phrase = "to be or not to be";
        java.util.List<Pair<Integer, Integer>> occurences = at.phraseOccurrence(phrase);

		for (Pair<Integer, Integer> occurence: occurences) {
            System.out.println(occurence.getLeftValue() + " " + occurence.getRightValue());
        }


//        Trie<List<Integer>> trie = new Trie<>();
//        trie.insert("hello");
//
//        List<Integer> a = new ArrayList<>();
//        trie.setElement("hello", a);
//
//        a.add(a.size(), 1);
//
//        a.add(a.size(), 2);
//
//        a.add(a.size(), 3);
//
//
//
//        System.out.println(trie.getElement("hello").size());


	}
}