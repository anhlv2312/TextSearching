package comp3506.assn2.testdriver;

import comp3506.assn2.application.AutoTester;
import comp3506.assn2.application.Search;
import comp3506.assn2.utils.Pair;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class ManualTests {

    private static Search searchApplication;

    @BeforeClass
    public static void before() throws Exception {
        searchApplication = new AutoTester("./files/shakespeare.txt", "", "./files/stop-words.txt");
    }

    @Test
    public void testWordCount() {
        assertEquals(searchApplication.wordCount("sonnet"), 10);
        assertEquals(searchApplication.wordCount("glorious"), 49);
        assertEquals(searchApplication.wordCount("sweets"), 18);
    }

    @Test
    public void testPhraseOccurrence() {

        Pair<Integer, Integer> phraseOccurrence;
        phraseOccurrence = searchApplication.phraseOccurrence("beard.-Prythee say on. He's for a jig or a tale of bawdry, or he sleeps. Say on; come to Hecuba. FIRST").iterator().next();
        assertEquals((int)phraseOccurrence.getLeftValue(), 25521);
        assertEquals((int)phraseOccurrence.getRightValue(), 37);


        phraseOccurrence = searchApplication.phraseOccurrence("own bud buriest").iterator().next();
        assertEquals((int)phraseOccurrence.getLeftValue(), 21);
        assertEquals((int)phraseOccurrence.getRightValue(), 14);


        phraseOccurrence = searchApplication.phraseOccurrence("sonnets 1 From").iterator().next();
        assertEquals((int)phraseOccurrence.getLeftValue(), 7);
        assertEquals((int)phraseOccurrence.getRightValue(), 5);
    }


    @Test
    public void testPrefixOccurrence() {
        assertEquals(searchApplication.prefixOccurrence("obscure").size(), 19);
        Pair<Integer, Integer> prefixOccurrence;
        assertEquals(searchApplication.prefixOccurrence("niggarding").size(), 1);
        prefixOccurrence = searchApplication.prefixOccurrence("niggarding").iterator().next();
        assertEquals((int)prefixOccurrence.getLeftValue(), 22);
        assertEquals((int)prefixOccurrence.getRightValue(), 36);
    }


    @Test
    public void testWordOnLine() {
        assertEquals(searchApplication.wordsOnLine(new String[]{"hymns", "heaven's"}).size(), 1);

    }

    @Test
    public void testSomeWordOnLine() {
        assertEquals(searchApplication.someWordsOnLine(new String[]{"niggarding", "thriftless"}).size(), 5);


    }

    @Test
    public void testWordsNotOnLine() {
        assertEquals(searchApplication.wordsNotOnLine(new String[]{"hymns", "heaven's"}, new String[]{"sings"}).size(), 0);
    }

}