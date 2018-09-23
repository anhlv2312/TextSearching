package comp3506.assn2.testdriver;


import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;

import comp3506.assn2.application.AutoTester;
import comp3506.assn2.application.Search;
import comp3506.assn2.utils.Pair;
import comp3506.assn2.utils.Triple;


/**
 * Sample tests for each method in comp3506.assn2.application.Search
 * 
 * @author Richard Thomas <richard.thomas@uq.edu.au>
 *
 */
public class ProvidedTests {

	static Search searchApplication;
	
	@BeforeClass
	public static void openFiles() {
		try {
			searchApplication = new AutoTester("files/shakespeare.txt", "files/shakespeare-index.txt", "files/stop-words.txt");
			
			searchApplication.wordCount("obscure");
		} catch (FileNotFoundException | IllegalArgumentException e) {
			System.out.println("Opening files failed!");
			e.printStackTrace();
		}
	}

	@Test(timeout=500)
	public void testWordCount() {
		assertThat("Word count of 'obscure' should have been 12.", searchApplication.wordCount("obscure"), is(equalTo(12)));
	}

	@Test(timeout=500)
	public void testPhraseOccurrence() {
		List<Pair<Integer,Integer>> expected = Arrays.asList(new Pair<>(27960,25), new Pair<>(44217,19), new Pair<>(46802,12), 
                                                             new Pair<>(69473,10), new Pair<>(69674,29), new Pair<>(72415,41),
                                                             new Pair<>(78318,33), new Pair<>(92223,45), new Pair<>(100957,31),
                                                             new Pair<>(122883,31), new Pair<>(131918,38), new Pair<>(148012,31));
		List<Pair<Integer,Integer>> searchResult = searchApplication.phraseOccurrence("obscure");
		assertThat("Locations of 'obscure' were not expected.", searchResult, containsInAnyOrder(expected));
		assertThat("Search for 'obscure' returned wrong number of results.", searchResult, hasSize(12));
	}

	@Test(timeout=500)
	public void testPrefixOccurrence() {
		List<Pair<Integer,Integer>> expected = Arrays.asList(new Pair<>(27960,25), new Pair<>(44217,19), new Pair<>(46802,12),    // obscure
                                                             new Pair<>(69473,10), new Pair<>(69674,29), new Pair<>(72415,41),
                                                             new Pair<>(78318,33), new Pair<>(92223,45), new Pair<>(100957,31),
                                                             new Pair<>(122883,31), new Pair<>(131918,38), new Pair<>(148012,31), 
                                                             new Pair<>(12574,5), new Pair<>(12870,12), new Pair<>(64754, 7),     // obscured
                                                             new Pair<>(78736,5), new Pair<>(148585,19),                          // obscures
                                                             new Pair<>(58643,38), new Pair<>(146994,25));                        // obscurely
		List<Pair<Integer,Integer>> searchResult = searchApplication.prefixOccurrence("obscure");
		assertThat("Locations of 'obscure' prefix were not expected.", searchResult, containsInAnyOrder(expected));
		assertThat("Search for 'obscure' returned wrong number of results.", searchResult, hasSize(19));
	}

	@Test(timeout=500)
	public void testWordsOnLine() {
		String [] searchTerm = {"riper", "decease"};
		List<Integer> expected = Arrays.asList(13);
		assertThat("Location of 'riper' && 'decease' were not expected.", searchApplication.wordsOnLine(searchTerm), is(expected));
	}

	@Test(timeout=500)
	public void testSomeWordsOnLine() {
		String [] searchTerm = {"boggler", "carlot"};
		List<Integer> expected = Arrays.asList(8416, 11839);
		List<Integer> searchResult = searchApplication.someWordsOnLine(searchTerm);
		assertThat("Locations of 'boggler' || 'carlot' were not expected.", searchResult, containsInAnyOrder(expected));
		assertThat("Search for 'boggler' || 'carlot' returned wrong number of results.", searchResult, hasSize(2));
	}

	@Test(timeout=500)
	public void testWordsNotOnLine() {
		String [] requiredWords = {"riper"};
		String [] excludedWords = {"decease"};
		List<Integer> expected = Arrays.asList(1837, 11852);
		List<Integer> searchResult = searchApplication.wordsNotOnLine(requiredWords, excludedWords);
		assertThat("Locations of 'riper' were not expected.", searchResult, containsInAnyOrder(expected));
		assertThat("Search for 'riper' && !'decease' returned wrong number of results.", searchResult, hasSize(2));
	}

	@Test(timeout=500)
	public void testSimpleAndSearch() {
		String [] titles = {"CYMBELINE", "THE TRAGEDY OF HAMLET", "THE FIRST PART OF KING HENRY THE FOURTH", 
				            "THE SECOND PART OF KING HENRY THE SIXTH", "KING RICHARD THE SECOND", "VENUS AND ADONIS"};
		String [] requiredWords = {"obscure", "rusty"};
		List<Triple<Integer,Integer,String>> expected = Arrays.asList(new Triple<>(25300,28,"rusty"),     // Hamlet
				                                                      new Triple<>(27960,25,"obscure"),
				                                                      new Triple<>(100683,31,"rusty"),    // King Richard the Second
				                                                      new Triple<>(100957,31,"obscure"));
		List<Triple<Integer,Integer,String>> searchResult = searchApplication.simpleAndSearch(titles, requiredWords);
		assertThat("Locations of 'obscure' && 'rusty' were not expected.", searchResult, containsInAnyOrder(expected));
		assertThat("Search for 'obscure' && 'rusty' returned wrong number of results.", searchResult, hasSize(4));
	}

	@Test(timeout=500)
	public void testSimpleOrSearch() {
		String [] titles = {"AS YOU LIKE IT", "CYMBELINE", "THE TRAGEDY OF HAMLET", 
				            "PERICLES PRINCE OF TYRE", "THE PASSIONATE PILGRIM"};
		String [] words = {"riper", "rusty"};
		List<Triple<Integer,Integer,String>> expected = Arrays.asList(new Triple<>(11852,14,"riper"), 
				                                                      new Triple<>(25300,28,"rusty"),
				                                                      new Triple<>(95975,28,"rusty"),
				                                                      new Triple<>(96140,40,"rusty"),
				                                                      new Triple<>(96160,12,"rusty"),
				                                                      new Triple<>(145336,36,"rusty"));
		List<Triple<Integer,Integer,String>> searchResult = searchApplication.simpleOrSearch(titles, words);
		assertThat("Locations of 'riper' || 'rusty' were not expected.", searchResult, containsInAnyOrder(expected));
		assertThat("Search for 'riper' || 'rusty' returned wrong number of results.", searchResult, hasSize(6));
	}

	@Test(timeout=500)
	public void testSimpleNotSearch() {
		String [] titles = {"CYMBELINE", "THE TRAGEDY OF HAMLET", "THE FIRST PART OF KING HENRY THE FOURTH", 
	                        "THE SECOND PART OF KING HENRY THE SIXTH", "KING RICHARD THE SECOND", 
	                        "THE TRAGEDY OF ROMEO AND JULIET"};
		String [] requiredWords = {"obscure", "rusty"};
		String [] excludedWords = {"beaver"};
		List<Triple<Integer,Integer,String>> expected = Arrays.asList(new Triple<>(100683,31,"rusty"),    // King Richard the Second
                                                                      new Triple<>(100957,31,"obscure"));
		List<Triple<Integer,Integer,String>> searchResult = searchApplication.simpleNotSearch(titles, requiredWords, excludedWords);
		assertThat("Locations of 'obscure' && 'rusty' were not expected.", searchResult, containsInAnyOrder(expected));
		assertThat("Search for 'obscure' && 'rusty' && !'beaver' returned wrong number of results.", searchResult, hasSize(2));
	}

	@Test(timeout=500)
	public void testCompoundAndOrSearch() {
		String [] titles = {"CYMBELINE", "THE TRAGEDY OF HAMLET", "THE LIFE OF KING HENRY THE FIFTH", 
				            "THE FIRST PART OF KING HENRY THE SIXTH", "THE SECOND PART OF KING HENRY THE SIXTH", 
				            "KING RICHARD THE SECOND", "VENUS AND ADONIS"};
		String [] requiredWords = {"obscure"};
		String [] orWords = {"beaver", "hoof"};
		List<Triple<Integer,Integer,String>> expected = Arrays.asList(new Triple<>(23709,29,"beaver"),    // Hamlet
                                                                      new Triple<>(27960,25,"obscure"),
                                                                      new Triple<>(148012,31,"obscure"),  // Venus and Adonis
                                                                      new Triple<>(148047,33,"hoof"));
		List<Triple<Integer,Integer,String>> searchResult = searchApplication.compoundAndOrSearch(titles, requiredWords, orWords);
		assertThat("Locations of 'obscure' && ('beaver' || 'hoof') were not expected.", searchResult, containsInAnyOrder(expected));
		assertThat("Search for 'obscure' && ('beaver' || 'hoof') returned wrong number of results.", searchResult, hasSize(4));
	}

}