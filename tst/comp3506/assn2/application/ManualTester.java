package comp3506.assn2.application;

public class ManualTester {

	public static void main(String[] args) throws Exception {
		AutoTester at = new AutoTester("./files/shakespeare.txt",
				"./files/shakespeare-index.txt",
				"./files/stop-words.txt");

        at.wordCount("their");

	}
}