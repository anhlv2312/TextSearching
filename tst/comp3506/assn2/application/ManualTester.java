package comp3506.assn2.application;

public class ManualTester {

	public static void main(String[] args) throws Exception {
		AutoTester at = new AutoTester("./files/shakespeare.txt",
				"./files/shakespeare-index.txt",
				"./files/stop-words.txt");

        System.out.println(at.wordCount("their"));
        System.out.println(at.phraseOccurrence("to be or not to be"));

	}
}