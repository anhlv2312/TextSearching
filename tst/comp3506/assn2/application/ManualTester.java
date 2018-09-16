package comp3506.assn2.application;

import comp3506.assn2.utils.Pair;

public class ManualTester {

	public static void main(String[] args) throws Exception {
//		AutoTester at = new AutoTester("./files/shakespeare.txt",
		AutoTester at = new AutoTester("./files/shakespeare-test.txt",
 				"./files/shakespeare-index.txt",
				"./files/stop-words.txt");

		String[] words = {"That's", "world ", "test", "to", "queen ", "substantial", "beauty's"};

		for (String word: words) {
            System.out.println(word + ": " + at.wordCount(word));
        }


        String phrase = "own bud buri";

        java.util.List<Pair<Integer, Integer>> occurrences = at.phraseOccurrence(phrase);

		for (Pair<Integer, Integer> occurrence: occurrences) {
            System.out.println(occurrence.getLeftValue() + " " + occurrence.getRightValue());
        }


//        Trie<List<Integer>> trie = new Trie<>();
//        trie.insert("hello");
//		trie.insert("I");
//
//        List<Integer> a = new ArrayList<>();
//        trie.setElement("I", a);
//
//        a.add(a.size(), 1);
//
//        a.add(a.size(), 2);
//
//        a.add(a.size(), 3);
//
//
//
//        System.out.println(trie.getElement("I").size());


//		System.out.println(Utility.sanitizeString("    That thereby- ?beauty's rose-adline \"might\" never die,"));


	}
}