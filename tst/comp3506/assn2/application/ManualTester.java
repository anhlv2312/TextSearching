package comp3506.assn2.application;

import comp3506.assn2.adts.ProbeHashSet;
import comp3506.assn2.adts.Set;
import comp3506.assn2.adts.Trie;
import comp3506.assn2.utils.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ManualTester {

    private AutoTester autoTester;

    @Before
    public void before() throws Exception {
        autoTester = new AutoTester("./files/shakespeare.txt",
                "./files/shakespeare-index.txt",
                "./files/stop-words.txt");
    }

    @After
    public void after() throws Exception {

    }

    @Test
    public void testWordCount() {
        assertEquals(autoTester.wordCount("sonnet"), 16);
    }

    @Test
    public void testPhraseOccurrence() {
        assertEquals(autoTester.phraseOccurrence("to be or not to be").size(), 1);
        Pair<Integer, Integer> phraseOccurrence;
        phraseOccurrence = autoTester.phraseOccurrence("own bud buriest").iterator().next();
        assertEquals((int)phraseOccurrence.getLeftValue(), 21);
        assertEquals((int)phraseOccurrence.getRightValue(), 14);
    }


    @Test
    public void testPrefixOccurrence() {
        assertEquals(autoTester.prefixOccurrence("obscure").size(), 4);
        Pair<Integer, Integer> prefixOccurrence;
        prefixOccurrence = autoTester.prefixOccurrence("villain").iterator().next();
        assertEquals((int)prefixOccurrence.getLeftValue(), 13);
        assertEquals((int)prefixOccurrence.getRightValue(), 48);
    }


    @Test
    public void testWordOnLine() {
        for (int line: autoTester.wordsOnLine(new String[]{"glass", "and"})) {
            System.out.println(line);
        }
        assertEquals(autoTester.wordsOnLine(new String[]{"flame", "with"}).size(), 1);
        assertEquals(autoTester.wordsOnLine(new String[]{"glass", "and"}).size(), 25);


    }

    @Test
    public void testSomeWordOnLine() {
        assertEquals(autoTester.someWordsOnLine(new String[]{"flame", "with"}).size(), 1);
    }

    @Test
    public void testWordsNotOnLine() {
        assertEquals(autoTester.wordsNotOnLine(new String[]{"flame", "with"}, new String[]{"with"}).size(), 0);
    }

}