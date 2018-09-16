package comp3506.assn2.application;

import static org.junit.Assert.*;

import comp3506.assn2.utils.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class AutoTesterTest {

    AutoTester autoTester;

    @Before
    public void before() throws Exception {
//		autoTester = new AutoTester("./files/shakespeare.txt",
        autoTester = new AutoTester("./files/shakespeare-test.txt",
                "./files/shakespeare-index.txt",
                "./files/stop-words.txt");
    }

    @Test
    public void testSanitizeString() {
        String string = "    That thereby- ?beauty's rose-adline \"might\" never die,";
        System.out.println(string);
        System.out.println(Utility.sanitizeString(string));
    }


    @Test
    public void testIndexTable() {
        Trie<IndexTable> wordIndexes = new Trie<>();
        wordIndexes.insert("test");
        IndexTable indexTable = new IndexTable("test");
        indexTable.addPosition(3, 2);
        indexTable.addPosition(4, 10);

        assertEquals(indexTable.getPositions().size(), 2);
        assertEquals(indexTable.getPositions().get(0).getLine(), 3);
        assertEquals(indexTable.getPositions().get(1).getLine(), 4);

    }

    @Test
    public void testWordCount() {
        String[] words = {"That's", "world ", "test", "to", "queen ", "substantial", "beauty's"};

        for (String word: words) {
            System.out.println(word + ": " + autoTester.wordCount(word));
        }
    }

    @Test
    public void testPhraseOccurrence() {
        String phrase = "own bud buri";
        List<Pair<Integer, Integer>> phraseOccurrences = autoTester.phraseOccurrence(phrase);
        for (Pair<Integer, Integer> phraseOccurrence: phraseOccurrences) {
            System.out.println(phraseOccurrence.getLeftValue() + " " + phraseOccurrence.getRightValue());
        }
    }


//    @Test
//    public void testPrefixOccurrence() {
//        String prefix = "hera";
//        List<Pair<Integer, Integer>> prefixOccurrences = autoTester.prefixOccurrence(prefix);
//
//        for (Pair<Integer, Integer> prefixOccurrence: prefixOccurrences) {
//            System.out.println(prefixOccurrence.getLeftValue() + " " + prefixOccurrence.getRightValue());
//        }
//    }

}