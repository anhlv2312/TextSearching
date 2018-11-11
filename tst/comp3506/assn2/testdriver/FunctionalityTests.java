package comp3506.assn2.testdriver;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;

import static org.junit.Assert.*;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import comp3506.assn2.application.AutoTester;
import comp3506.assn2.application.Search;
import comp3506.assn2.utils.TestingTriple;
import comp3506.assn2.utils.TestingPair;
import comp3506.assn2.utils.Triple;
import comp3506.assn2.utils.Pair;


/**
 * Functional tests for each method in comp3506.assn2.application.Search
 * 
 * @author Richard Thomas <richard.thomas@uq.edu.au>
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FunctionalityTests {

	private static Search searchApplication;
	
	// All occurrences of the word "obscure" in shakespeare.txt.
	private final static List<TestingPair<Integer,Integer>> obscureOccurrences = 
			Arrays.asList(new TestingPair<>(27960,25), new TestingPair<>(44217,19), new TestingPair<>(46802,12), 
					      new TestingPair<>(69473,10), new TestingPair<>(69674,29), new TestingPair<>(72415,41),
                          new TestingPair<>(78318,33), new TestingPair<>(92223,45), new TestingPair<>(100957,31), 
                          new TestingPair<>(122883,31), new TestingPair<>(131918,38), new TestingPair<>(148012,31));

	// All occurrences of the word "quarrels" in shakespeare.txt.
	private final static List<TestingPair<Integer,Integer>> quarrelsOccurrences = 
			Arrays.asList(new TestingPair<>(12588,36), new TestingPair<>(37145,18), new TestingPair<>(39922,12), 
					      new TestingPair<>(40649,5), new TestingPair<>(40881,33), new TestingPair<>(52008,30),
                          new TestingPair<>(68023,14), new TestingPair<>(73580,5), new TestingPair<>(80013,46), 
                          new TestingPair<>(87578,55), new TestingPair<>(100064,17), new TestingPair<>(109142,24),
                          new TestingPair<>(122574,18), new TestingPair<>(122683,28), new TestingPair<>(122860,17),
                          new TestingPair<>(124669,11), new TestingPair<>(127128,6), new TestingPair<>(132848,14),
                          new TestingPair<>(137225,1));
	
	// All occurrences of the phrase "I have fought" in shakespeare.txt.
	private final static List<TestingPair<Integer,Integer>> haveFoughtOccurrences = 
			Arrays.asList(new TestingPair<>(15777,5), new TestingPair<>(16435,34), 
						  new TestingPair<>(50356,10), new TestingPair<>(106741,49));
	
	// All occurrences of the phrase "advice and" in shakespeare.txt.
	private final static List<TestingPair<Integer,Integer>> adviceAndOccurrences = 
			Arrays.asList(new TestingPair<>(35865,15), new TestingPair<>(45596,14),		// Phrase on single line 
					  // Phrase spans two lines, in occurrences below:
					  new TestingPair<>(24956,39), new TestingPair<>(105068,23), new TestingPair<>(126264,37));


	
	@BeforeClass
	public static void openFiles() {
		try {
			searchApplication = new AutoTester("files/shakespeare.txt", "files/shakespeare-index.txt", "files/stop-words.txt");
		} catch (FileNotFoundException | IllegalArgumentException e) {
			System.out.println("Opening files failed!");
			e.printStackTrace();
		}
	}

	/**
	 * Ensures that TestAPI object (tester) is set to null to allow the object to be reloaded.
	 * This means that the class loader can be garbage collected as there are no references to the objects it loaded.
	 * Must always run, so that if setUpBeforeClass throws an exception tester is still set to null.
	 */
	@AfterClass
	public static void cleanUpAfterClass() {
		searchApplication = null;
	}


	@Test(timeout=5000)
	public void testWordCount_NoEdgeCases() {
		assertThat("Word count of 'obscure' should have been 12.", searchApplication.wordCount("obscure"), is(equalTo(12)));
	}

	@Test(timeout=5000)
	public void testWordCount() {
		assertThat("Word count of 'quarrels' should have been 19.", searchApplication.wordCount("quarrels"), is(equalTo(19)));
	}

	@Test(timeout=5000)
	public void testPhraseOccurrence_Word_NoEdgeCases() {
		List<TestingPair<Integer, Integer>> searchResult = makeTestingPair(searchApplication.phraseOccurrence("obscure"));
		assertThat("Locations of 'obscure' were not expected.", searchResult, containsInAnyOrder(obscureOccurrences.toArray()));
		assertThat("Search for 'obscure' returned wrong number of results.", searchResult, hasSize(obscureOccurrences.size()));
	}

	@Test(timeout=5000)
	public void testPhraseOccurrence_Word() {
		List<TestingPair<Integer, Integer>> searchResult = makeTestingPair(searchApplication.phraseOccurrence("quarrels"));
		assertThat("Locations of 'quarrels' were not expected.", searchResult, containsInAnyOrder(quarrelsOccurrences.toArray()));
		assertThat("Search for 'quarrels' returned wrong number of results.", searchResult, hasSize(quarrelsOccurrences.size()));
	}

	@Test(timeout=5000)
	public void testPhraseOccurrence_Phrase_NoEdgeCases() {
		List<TestingPair<Integer,Integer>> expected = Arrays.asList(new TestingPair<>(25781,5));
		List<TestingPair<Integer, Integer>> searchResult = makeTestingPair(searchApplication.phraseOccurrence("slings and arrows"));
		assertThat("Phrase 'slings and arrows' was not found where expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'slings and arrows' returned wrong number of results.", searchResult, hasSize(expected.size()));
	}

	@Test(timeout=5000)
	public void testPhraseOccurrence_Phrase_SingleLine() {
		List<TestingPair<Integer, Integer>> searchResult = makeTestingPair(searchApplication.phraseOccurrence("i have fought"));
		assertThat("Phrase 'I have fought' was not found where expected.", searchResult, containsInAnyOrder(haveFoughtOccurrences.toArray()));
		assertThat("Search for 'I have fought' returned wrong number of results.", searchResult, hasSize(haveFoughtOccurrences.size()));
	}

	@Test(timeout=5000)
	public void testPhraseOccurrence_Phrase_MultiLine() {
		List<TestingPair<Integer, Integer>> searchResult = makeTestingPair(searchApplication.phraseOccurrence("advice and"));
		assertThat("Phrase 'advice and' was not found where expected.", searchResult, containsInAnyOrder(adviceAndOccurrences.toArray()));
		assertThat("Search for 'advice and' returned wrong number of results.", searchResult, hasSize(adviceAndOccurrences.size()));
	}

	@Test(timeout=5000)
	public void testPrefixOccurrence_ExceptWord() {
		List<TestingPair<Integer,Integer>> expected = new ArrayList<>();
		expected.addAll(Arrays.asList(new TestingPair<>(12574,5), new TestingPair<>(12870,12), new TestingPair<>(64754, 7),   // obscured 
					                  new TestingPair<>(78736,5), new TestingPair<>(148585,19),                               // obscures
					                  new TestingPair<>(58643,38), new TestingPair<>(146994,25)));                            // obscurely
		List<TestingPair<Integer,Integer>> searchResult = makeTestingPair(searchApplication.prefixOccurrence("obscure"));
		for(TestingPair<Integer,Integer> obscureSuffix : expected) {
			assertThat("Locations of 'obscure' prefix were not expected.", searchResult, hasItem(obscureSuffix));
		}
		assertTrue("Search for 'obscure' returned wrong number of results.", 
				   searchResult.size() == expected.size() || searchResult.size() == expected.size() + obscureOccurrences.size());
	}

	@Test(timeout=5000)
	public void testPrefixOccurrence() {
		List<TestingPair<Integer,Integer>> expected = new ArrayList<>(obscureOccurrences);                                    // obscure
		expected.addAll(Arrays.asList(new TestingPair<>(12574,5), new TestingPair<>(12870,12), new TestingPair<>(64754, 7),   // obscured 
					                  new TestingPair<>(78736,5), new TestingPair<>(148585,19),                               // obscures
					                  new TestingPair<>(58643,38), new TestingPair<>(146994,25)));                            // obscurely
		List<TestingPair<Integer,Integer>> searchResult = makeTestingPair(searchApplication.prefixOccurrence("obscure"));
		assertThat("Locations of 'obscure' prefix were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'obscure' returned wrong number of results.", searchResult, hasSize(expected.size()));
	}

	@Test(timeout=5000)
	public void testWordsOnLine_OnlySomeWordsFound() {
		String [] searchTerm = {"riper", "decease"};
		List<Integer> result = searchApplication.wordsOnLine(searchTerm);
		
		// Search works correctly.
		if (result.get(0) == 13 && result.size() == 1)
			return;
		
		// Search only found 'riper'.
		if (result.size() == 3 && result.contains(13) && result.contains(1837) && result.contains(11852))
			return;
		
		// Search only found 'decease'.
		if (result.size() == 7 && result.contains(13) && result.contains(233) && result.contains(1746) &&
			result.contains(42522) && result.contains(45884) && result.contains(48226) && result.contains(148904))
			return;
		
		fail("Neither 'riper' nor 'decease' were found.");
	}

	@Test(timeout=5000)
	public void testWordsOnLine_NoEdgeCases() {
		String [] searchTerm = {"bountifully", "commend", "hope"};
		List<Integer> expected = Arrays.asList(120382);
		assertThat("Line containing 'bountifully', 'commend' && 'hope' was not expected.", 
				   searchApplication.wordsOnLine(searchTerm), is(expected));
	}

	@Test(timeout=5000)
	public void testWordsOnLine() {
		String [] searchTerm = {"mute", "dumb"};
		List<Integer> expected = Arrays.asList(24948, 124848, 146849);
		assertThat("Lines containing 'mute' && 'dumb' were not expected.", searchApplication.wordsOnLine(searchTerm), 
																		   containsInAnyOrder(expected.toArray()));
	}

	@Test(timeout=5000)
	public void testSomeWordsOnLine_AtLeastOneWord() {
		String [] searchTerm = {"boggler", "carlot"};
		List<Integer> searchResult = searchApplication.someWordsOnLine(searchTerm);
		if (!searchResult.contains(8416) && !searchResult.contains(11839))
			fail("Search for 'boggler' || 'carlot' did not find either line of their occurrences");
	}

	@Test(timeout=5000)
	public void testSomeWordsOnLine_SomeWords() {
		String [] searchTerm = {"rusty", "beaver", "behoofe"};
		List<Integer> rusty = Arrays.asList(25300, 29883, 40073, 95975, 96140, 96160, 100683, 114286, 128170, 145336);
		List<Integer> beaver = Arrays.asList(23709, 33103, 40073, 48052, 106114, 126137);
		List<Integer> behoofe = Arrays.asList(136957);
		
		List<Integer> searchResult = searchApplication.someWordsOnLine(searchTerm);
		if (searchResult.containsAll(rusty) && searchResult.containsAll(beaver))
			return;
			
		if (searchResult.containsAll(rusty) && searchResult.containsAll(behoofe))
			return;
			
		if (searchResult.containsAll(beaver) && searchResult.containsAll(behoofe))
			return;
			
		fail("Search for 'rusty' || 'beaver' || ' behoofe' did not find the appropriate occurrences");
	}

	@Test(timeout=5000)
	public void testSomeWordsOnLine() {
		String [] searchTerm = {"decease", "riper", "hoof"};
		List<Integer> expected = Arrays.asList(13, 233, 1746, 42522, 45884, 48226, 148904,	// deceased
											   13, 1837, 11852, 							// riper
											   39518, 80537, 148047);						// hoof
		List<Integer> searchResult = searchApplication.someWordsOnLine(searchTerm);
		for(int lineNumber : expected) {
			assertThat("Did not find all lines containing 'decease' || 'riper' || 'hoof'.", searchResult, hasItem(lineNumber));
		}
		assertTrue("Search for 'decease' || 'riper' || 'hoof' returned wrong number of results.", 
				   searchResult.size() == expected.size() || searchResult.size() == expected.size() - 1);
	}

	@Test(timeout=5000)
	public void testWordsNotOnLine_AtLeastOne() {
		String [] requiredWords = {"beaver"};
		String [] excludedWords = {"rusty"};
		List<Integer> expected = Arrays.asList(23709, 33103, 48052, 106114, 126137);		// not 40073, includes 'rusty'
		
		List<Integer> searchResult = searchApplication.wordsNotOnLine(requiredWords, excludedWords);
		assertThat("Locations of 'beaver' were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'beaver' && !'rusty' returned wrong number of results.", searchResult, hasSize(expected.size()));
	}

	@Test(timeout=5000)
	public void testWordsNotOnLine_NoEdgeCases() {
		String [] requiredWords = {"beaver", "his"};
		String [] excludedWords = {"my", "up", "heavens"};
		List<Integer> expected = Arrays.asList(33103, 48052);								// not 23709, includes 'my' and 'up'
		
		List<Integer> searchResult = searchApplication.wordsNotOnLine(requiredWords, excludedWords);
		assertThat("Locations of 'beaver' && 'his' were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'beaver' && 'his' returned wrong number of results.", searchResult, hasSize(expected.size()));
	}

	@Test(timeout=5000)
	public void testWordsNotOnLine() {
		String [] requiredWords = {"quarrels", "love", "the"};								// 'the' is a stop word to be ignored
		String [] excludedWords = {"griefs", "broach", "heavens"};							// 'broach'd' is on the line that should match
		List<Integer> expected = Arrays.asList(122683);										// not 40881, includes 'griefs'
		
		List<Integer> searchResult = searchApplication.wordsNotOnLine(requiredWords, excludedWords);
		assertThat("Locations of 'quarrels' && 'love' were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'quarrels' && 'love' returned wrong number of results.", searchResult, hasSize(expected.size()));
	}
	
	@Test(timeout=5000)
	public void testSimpleAndSearch_OneSection() {
		String [] titles = {"THE LIFE OF KING HENRY THE FIFTH"};
		String [] requiredWords = {"quarrels", "hoof"};
		List<TestingTriple<Integer,Integer,String>> expected = Arrays.asList(new TestingTriple<>(39922,12,"quarrels"),
				                                                             new TestingTriple<>(40649,5,"quarrels"),
				                                                             new TestingTriple<>(40881,33,"quarrels"),
				                                                             new TestingTriple<>(39518,28,"hoof"));
		List<TestingTriple<Integer,Integer,String>> searchResult = makeTestingTriple(searchApplication.simpleAndSearch(titles, requiredWords));
		assertThat("Locations of 'quarrels' && 'hoof' were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'quarrels' && 'hoof' returned wrong number of results.", searchResult, hasSize(expected.size()));
	}
	
	@Test(timeout=5000)
	public void testSimpleAndSearch_NoEdgeCases() {
		String [] titles = {"CYMBELINE", "THE TRAGEDY OF HAMLET", "THE FIRST PART OF KING HENRY THE FOURTH", 
							"THE SECOND PART OF KING HENRY THE FOURTH", "THE LIFE OF KING HENRY THE FIFTH",
				            "THE SECOND PART OF KING HENRY THE SIXTH", "KING RICHARD THE SECOND", "VENUS AND ADONIS"};
		String [] requiredWords = {"quarrels", "hoofs"};
		List<TestingTriple<Integer,Integer,String>> expected = Arrays.asList(new TestingTriple<>(39922,12,"quarrels"),	// King Henry V
				                                                             new TestingTriple<>(40649,5,"quarrels"),
				                                                             new TestingTriple<>(40881,33,"quarrels"),
				                                                             new TestingTriple<>(37847,25,"hoofs"),
																	         new TestingTriple<>(100064,17,"quarrels"),	// King Richard II
																	         new TestingTriple<>(100566,49,"hoofs"));
		List<TestingTriple<Integer,Integer,String>> searchResult = makeTestingTriple(searchApplication.simpleAndSearch(titles, requiredWords));
		assertThat("Locations of 'quarrels' && 'hoofs' were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'quarrels' && 'hoofs' returned wrong number of results.", searchResult, hasSize(expected.size()));
	}
	
	@Test(timeout=5000)
	public void testSimpleAndSearch() {
		String [] titles = {"CYMBELINE", "THE TRAGEDY OF HAMLET", "THE FIRST PART OF KING HENRY THE FOURTH", 
							"THE LIFE OF KING HENRY THE FIFTH", "THE SECOND PART OF KING HENRY THE SIXTH", 
							"KING RICHARD THE SECOND", "VENUS AND ADONIS", "THE TRAGEDY OF TITUS ANDRONICUS"};
		String [] requiredWords = {"obscure", "rusty", "quarrels"};
		List<TestingTriple<Integer,Integer,String>> expected = Arrays.asList(new TestingTriple<>(100683,31,"rusty"),    // King Richard II
				                                                             new TestingTriple<>(100957,31,"obscure"),
				                                                             new TestingTriple<>(100064,17,"quarrels"));
		List<TestingTriple<Integer,Integer,String>> searchResult = makeTestingTriple(searchApplication.simpleAndSearch(titles, requiredWords));
		assertThat("Locations of 'obscure', 'rusty' && 'quarrels' were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'obscure', 'rusty' && 'quarrels' returned wrong number of results.", searchResult, hasSize(expected.size()));
	}

	@Test(timeout=5000)
	public void testSimpleOrSearch_OneSection() {
		String [] titles = {"KING RICHARD THE SECOND"};
		String [] words = {"quarrels", "hoofs"};
		List<TestingTriple<Integer,Integer,String>> expected = Arrays.asList(new TestingTriple<>(100566,49,"hoofs"), 
				                                                             new TestingTriple<>(100064,17,"quarrels"));
		List<TestingTriple<Integer,Integer,String>> searchResult = makeTestingTriple(searchApplication.simpleOrSearch(titles, words));
		assertThat("Locations of 'quarrels' || 'hoofs' were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'quarrels' || 'hoofs' returned wrong number of results.", searchResult, hasSize(expected.size()));
	}
	
	@Test(timeout=5000)
	public void testSimpleOrSearch_NoEdgeCases() {
		String [] titles = {"THE COMEDY OF ERRORS", "THE TRAGEDY OF HAMLET", "THE FIRST PART OF KING HENRY THE FOURTH", 
							"LOVE'S LABOUR'S LOST", "PERICLES PRINCE OF TYRE", "KING RICHARD THE SECOND", 
							"THE PASSIONATE PILGRIM", "VENUS AND ADONIS"};
		String [] words = {"obscure", "hoofs"};
		List<TestingTriple<Integer,Integer,String>> expected = Arrays.asList(new TestingTriple<>(27960,25,"obscure"),	// Hamlet
																			 new TestingTriple<>(29682,41,"hoofs"),		// King Henry IV, P1
																			 new TestingTriple<>(33964,11,"hoofs"),
				                                                             new TestingTriple<>(69473,10,"obscure"),	// Love's Labour's Lost
				                                                             new TestingTriple<>(69674,29,"obscure"),
				                                                             new TestingTriple<>(100566,49,"hoofs"),	// King Richard II
				                                                             new TestingTriple<>(100957,31,"obscure"),
				                                                             new TestingTriple<>(148012,31,"obscure"));	// Venus & Adonis
		List<TestingTriple<Integer,Integer,String>> searchResult = makeTestingTriple(searchApplication.simpleOrSearch(titles, words));
		assertThat("Locations of 'obscure' || 'hoofs' were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'obscure' || 'hoofs' returned wrong number of results.", searchResult, hasSize(expected.size()));
	}
	
	@Test(timeout=5000)
	public void testSimpleOrSearch() {
		String [] titles = {"THE SONNETS", "AS YOU LIKE IT", "CYMBELINE", "THE TRAGEDY OF HAMLET", 
							"THE SECOND PART OF KING HENRY THE SIXTH", "PERICLES PRINCE OF TYRE", 
							"A LOVER'S COMPLAINT", "THE PASSIONATE PILGRIM", "VENUS AND ADONIS"};
		String [] words = {"riper", "decease", "behoof", "foxship", "carlot"};
		List<TestingTriple<Integer,Integer,String>> expected = Arrays.asList(new TestingTriple<>(13,12,"riper"), 		// Sonnets
				                                                             new TestingTriple<>(13,33,"decease"),
				                                                             new TestingTriple<>(233,35,"decease"),
				                                                             new TestingTriple<>(1746,39,"decease"),
				                                                             new TestingTriple<>(1837,33,"riper"),
				                                                             new TestingTriple<>(11839,18,"carlot"),	// As You Like It
				                                                             new TestingTriple<>(11852,14,"riper"),
				                                                             new TestingTriple<>(45884,38,"decease"),	// King Henry VI, P2
				                                                             new TestingTriple<>(47308,14,"behoof"),
				                                                             new TestingTriple<>(144966,40,"behoof"),	// Lover's Complaint
				                                                             new TestingTriple<>(148904,39,"decease"));	// Venus & Adonis
		List<TestingTriple<Integer,Integer,String>> searchResult = makeTestingTriple(searchApplication.simpleOrSearch(titles, words));
		assertThat("Locations of 'riper' || 'decease' || 'behoof' || 'carlot' were not expected.",
				   searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'riper' || 'decease' || 'behoof' || 'carlot' returned wrong number of results.", 
				   searchResult, hasSize(expected.size()));
	}

	@Test(timeout=5000)
	public void testSimpleNotSearch_OneSection() {
		String [] titles = {"THE LIFE OF KING HENRY THE FIFTH"};
		String [] requiredWords = {"quarrels", "rusty"};
		String [] excludedWords = {"boggler"};
		List<TestingTriple<Integer,Integer,String>> expected = Arrays.asList(new TestingTriple<>(39922,12,"quarrels"),
                                                                             new TestingTriple<>(40073,27,"rusty"),
                                                                             new TestingTriple<>(40649,5,"quarrels"),
                                                                             new TestingTriple<>(40881,33,"quarrels"));
		List<TestingTriple<Integer,Integer,String>> searchResult = 
				makeTestingTriple(searchApplication.simpleNotSearch(titles, requiredWords, excludedWords));
		assertThat("Locations of 'quarrels' && 'rusty' were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'quarrels' && 'rusty' && !'boggler' returned wrong number of results.", searchResult, hasSize(expected.size()));
	}
	
	@Test(timeout=5000)
	public void testSimpleNotSearch_NoEdgeCases() {
		String [] titles = {"CYMBELINE", "THE TRAGEDY OF HAMLET", "THE FIRST PART OF KING HENRY THE FOURTH", 
	                        "THE SECOND PART OF KING HENRY THE SIXTH", "THE TRAGEDY OF MACBETH", 
	                        "KING RICHARD THE SECOND", "THE TRAGEDY OF ROMEO AND JULIET", 
	                        "THE TRAGEDY OF TITUS ANDRONICUS", "THE PASSIONATE PILGRIM", "VENUS AND ADONIS"};
		String [] requiredWords = {"quarrels", "obscure"};
		String [] excludedWords = {"rusty"};
		List<TestingTriple<Integer,Integer,String>> expected = Arrays.asList(new TestingTriple<>(72415,41,"obscure"),    // Macbeth
                                                                             new TestingTriple<>(73580,5,"quarrels"),
                                                                             new TestingTriple<>(122574,18,"quarrels"),	// Titus Andronicus
                                                                             new TestingTriple<>(122683,28,"quarrels"),
                                                                             new TestingTriple<>(122860,17,"quarrels"),
                                                                             new TestingTriple<>(122883,31,"obscure"),
                                                                             new TestingTriple<>(124669,11,"quarrels"));
		List<TestingTriple<Integer,Integer,String>> searchResult = 
				makeTestingTriple(searchApplication.simpleNotSearch(titles, requiredWords, excludedWords));
		assertThat("Locations of 'obscure' && 'quarrels' were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'obscure' && 'quarrels' && !'rusty' returned wrong number of results.", searchResult, hasSize(expected.size()));
	}
	
	@Test(timeout=6000)
	public void testSimpleNotSearch() {
		String [] titles = {"AS YOU LIKE IT", "CYMBELINE", "THE LIFE OF KING HENRY THE FIFTH", 
							"THE HISTORY OF TROILUS AND CRESSIDA"};
		String [] requiredWords = {"quarrels", "when", "the"};				// "the" - stop word to be ignored
		String [] excludedWords = {"beaver", "beavers", "predeceased"};
		List<TestingTriple<Integer,Integer,String>> expected = Arrays.asList(new TestingTriple<>(12588,36,"quarrels"),    // As You Like It
                                                                             new TestingTriple<>(10070,39,"when"),
                                                                             new TestingTriple<>(10175,23,"when"),
                                                                             new TestingTriple<>(10177,53,"when"),
                                                                             new TestingTriple<>(10200,14,"when"),
                                                                             new TestingTriple<>(10204,59,"when"),
                                                                             new TestingTriple<>(10325,66,"when"),
                                                                             new TestingTriple<>(10440,50,"when"),
                                                                             new TestingTriple<>(10492,22,"when"),
                                                                             new TestingTriple<>(10493,14,"when"),
                                                                             new TestingTriple<>(10515,5,"when"),
                                                                             new TestingTriple<>(10560,33,"when"),
                                                                             new TestingTriple<>(10588,11,"when"),
                                                                             new TestingTriple<>(10698,30,"when"),
                                                                             new TestingTriple<>(10726,5,"when"),
                                                                             new TestingTriple<>(10743,5,"when"),
                                                                             new TestingTriple<>(10779,55,"when"),
                                                                             new TestingTriple<>(10810,39,"when"),
                                                                             new TestingTriple<>(10894,9,"when"),
                                                                             new TestingTriple<>(10991,32,"when"),
                                                                             new TestingTriple<>(11039,5,"when"),
                                                                             new TestingTriple<>(11042,5,"when"),
                                                                             new TestingTriple<>(11414,17,"when"),
                                                                             new TestingTriple<>(11416,46,"when"),
                                                                             new TestingTriple<>(11427,48,"when"),
                                                                             new TestingTriple<>(11439,43,"when"),
                                                                             new TestingTriple<>(11454,3,"when"),
                                                                             new TestingTriple<>(11465,22,"when"),
                                                                             new TestingTriple<>(11576,15,"when"),
                                                                             new TestingTriple<>(11686,15,"when"),
                                                                             new TestingTriple<>(11694,66,"when"),
                                                                             new TestingTriple<>(11761,32,"when"),
                                                                             new TestingTriple<>(11790,10,"when"),
                                                                             new TestingTriple<>(11843,5,"when"),
                                                                             new TestingTriple<>(11940,51,"when"),
                                                                             new TestingTriple<>(11942,24,"when"),
                                                                             new TestingTriple<>(11990,21,"when"),
                                                                             new TestingTriple<>(12002,11,"when"),
                                                                             new TestingTriple<>(12002,35,"when"),
                                                                             new TestingTriple<>(12002,64,"when"),
                                                                             new TestingTriple<>(12003,41,"when"),
                                                                             new TestingTriple<>(12007,61,"when"),
                                                                             new TestingTriple<>(12008,66,"when"),
                                                                             new TestingTriple<>(12205,11,"when"),
                                                                             new TestingTriple<>(12223,5,"when"),
                                                                             new TestingTriple<>(12249,5,"when"),
                                                                             new TestingTriple<>(12269,27,"when"),
                                                                             new TestingTriple<>(12329,39,"when"),
                                                                             new TestingTriple<>(12330,32,"when"),
                                                                             new TestingTriple<>(12391,5,"when"),
                                                                             new TestingTriple<>(12421,13,"when"),
                                                                             new TestingTriple<>(12507,9,"when"),
                                                                             new TestingTriple<>(12548,43,"when"),
                                                                             new TestingTriple<>(12630,59,"when"),
                                                                             new TestingTriple<>(12631,47,"when"),
                                                                             new TestingTriple<>(12643,15,"when"),
                                                                             new TestingTriple<>(12759,48,"when"));
		List<TestingTriple<Integer,Integer,String>> searchResult = 
				makeTestingTriple(searchApplication.simpleNotSearch(titles, requiredWords, excludedWords));
		assertThat("Locations of 'quarrels' && 'when' were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for ('quarrels', 'when', 'the') && !('beaver', 'beavers', 'predeceased') returned wrong number of results.", 
				   searchResult, hasSize(expected.size()));
	}

	@Test(timeout=5000)
	public void testCompoundAndOrSearch_OneSection() {
		String [] titles = {"THE TRAGEDY OF HAMLET"};
		String [] requiredWords = {"obscure"};
		String [] orWords = {"beaver", "rusty", "boggler"};					// "boggler" - not in this section
		List<TestingTriple<Integer,Integer,String>> expected = Arrays.asList(new TestingTriple<>(23709,29,"beaver"),    // Hamlet
                                                                             new TestingTriple<>(25300,28,"rusty"),
                                                                             new TestingTriple<>(27960,25,"obscure"));
		List<TestingTriple<Integer,Integer,String>> searchResult = 
				makeTestingTriple(searchApplication.compoundAndOrSearch(titles, requiredWords, orWords));
		assertThat("Locations of 'obscure' && ('beaver' || 'rusty') were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'obscure' && ('beaver' || 'rusty' || 'boggler') returned wrong number of results.", 
				   searchResult, hasSize(expected.size()));
	}
	
	@Test(timeout=5000)
	public void testCompoundAndOrSearch_NoEdgeCases() {
		String [] titles = {"AS YOU LIKE IT", "CYMBELINE", "THE TRAGEDY OF HAMLET", "THE LIFE OF KING HENRY THE FIFTH", 
				            "THE FIRST PART OF HENRY THE SIXTH", "THE SECOND PART OF KING HENRY THE SIXTH", 
				            "KING RICHARD THE SECOND", "VENUS AND ADONIS"};
		String [] requiredWords = {"obscure"};
		String [] orWords = {"beaver", "hoof", "carlot"};
		List<TestingTriple<Integer,Integer,String>> expected = Arrays.asList(new TestingTriple<>(23709,29,"beaver"),    // Hamlet
                                                                             new TestingTriple<>(27960,25,"obscure"),
                                                                             new TestingTriple<>(148012,31,"obscure"),  // Venus and Adonis
                                                                             new TestingTriple<>(148047,33,"hoof"));
		List<TestingTriple<Integer,Integer,String>> searchResult = 
				makeTestingTriple(searchApplication.compoundAndOrSearch(titles, requiredWords, orWords));
		assertThat("Locations of 'obscure' && ('beaver' || 'hoof') were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'obscure' && ('beaver' || 'hoof') returned wrong number of results.", searchResult, hasSize(expected.size()));
	}
	
	@Test(timeout=5000)
	public void testCompoundAndOrSearch() {
		String [] titles = {"CYMBELINE", "THE TRAGEDY OF HAMLET", "THE LIFE OF KING HENRY THE FIFTH", 
				            "THE FIRST PART OF HENRY THE SIXTH", "THE SECOND PART OF KING HENRY THE SIXTH", 
				            "KING RICHARD THE SECOND", "THE HISTORY OF TROILUS AND CRESSIDA", "VENUS AND ADONIS"};
		String [] requiredWords = {"quarrels", "at"};						// "at" - stop word to be ignored
		String [] orWords = {"beaver", "hoof", "cludge"};					// "cludge" - not in text
		List<TestingTriple<Integer,Integer,String>> expected = Arrays.asList(new TestingTriple<>(40073,33,"beaver"),    // King Henry V
                                                                             new TestingTriple<>(39922,12,"quarrels"),
                                                                             new TestingTriple<>(40649,5,"quarrels"),
                                                                             new TestingTriple<>(40881,33,"quarrels"),
                                                                             new TestingTriple<>(39518,28,"hoof"),
                                                                             new TestingTriple<>(127128,6,"quarrels"),	// Troilus & Cressida
                                                                             new TestingTriple<>(126137,37,"beaver"));
		List<TestingTriple<Integer,Integer,String>> searchResult = 
				makeTestingTriple(searchApplication.compoundAndOrSearch(titles, requiredWords, orWords));
		assertThat("Locations of 'quarrels' && ('beaver' || 'hoof') were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for ('obscure' && 'at') && ('beaver' || 'hoof' || 'cludge') returned wrong number of results.", 
				   searchResult, hasSize(expected.size()));
	}

	
	/* Private methods to convert data for testing purposes. */
	
	
	/**
	 * @param data The list of Pairs to be converted to a list of TestingPairs.
	 */
	private List<TestingPair<Integer, Integer>> makeTestingPair(List<Pair<Integer, Integer>> data) {
		List<TestingPair<Integer, Integer>> result = new ArrayList<>(); 
		for (Pair<Integer, Integer> pair: data) {
			result.add(new TestingPair<Integer, Integer>(pair));
		}
		return result;
	}

	/**
	 * @param data The list of Triples to be converted to a list of TestingTriples.
	 */
	private List<TestingTriple<Integer, Integer, String>> makeTestingTriple(List<Triple<Integer, Integer, String>> data) {
		List<TestingTriple<Integer, Integer, String>> result = new ArrayList<>(); 
		for (Triple<Integer, Integer, String> triple: data) {
			result.add(new TestingTriple<Integer, Integer, String>(triple));
		}
		return result;
	}

}
