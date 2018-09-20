package comp3506.assn2.application;

import static org.junit.Assert.*;

import comp3506.assn2.adts.Trie;
import comp3506.assn2.utils.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ManualTester {

    Searcher searcher1, searcher2;

    @Before
    public void before() throws Exception {
        searcher1 = new Searcher("./files/test-input-1.txt", null, null);
        searcher2 = new Searcher("./files/test-input-2.txt", null, null);
    }

    @After
    public void after() throws Exception {

    }

    @Test
    public void testSanitizeString() {
        String string1 = "    That thereby- ?beauty's rose-adline \"might\" never die,";
        assertEquals(Utility.sanitizeString(string1), "    that thereby   beauty's rose adline  might  never die ");

        String string2 = "THE SONNETS";
        assertEquals(Utility.sanitizeString(string2), "the sonnets");
    }

    @Test
    public void testTokenizeString() {
        assertEquals(Utility.tokenizeString("From fairest I creatures A we desire increase,").size(), 8);
//        for (String token : Utility.tokenizeString("From fairest I creatures A we desire increase,").values()) {
//            System.out.println(token);
//        }
//        for (String token : Utility.tokenizeString("THE SONNETS").values()) {
//            System.out.println(token);
//        }
        assertEquals(Utility.tokenizeString("THE SONNETS ").size(), 2);
        assertEquals(Utility.tokenizeString("THE SONNETS").size(), 2);
    }

    @Test
    public void testIndexTable() {
        Trie<IndexTable> wordIndexes = new Trie<>();
        wordIndexes.insert("test");
        IndexTable indexTable = new IndexTable("test");
        indexTable.addPosition(3, 2);
        indexTable.addPosition(4, 10);

        assertEquals(indexTable.getPositions().size(), 2);
        assertEquals((int)indexTable.getPositions().get(0).getLeftValue(), 3);
        assertEquals((int)indexTable.getPositions().get(0).getRightValue(), 2);
        assertEquals((int)indexTable.getPositions().get(1).getLeftValue(), 4);
        assertEquals((int)indexTable.getPositions().get(1).getRightValue(), 10);

    }

    @Test
    public void testWordCount() {
//        String[] words = {"That's", "world ", "test", "to", "queen ", "substantial", "beauty's", "the"};
//        for (String word: words) {
//            System.out.println(word + ": " + searcher.wordCount(word));
//        }
        assertEquals(searcher1.wordCount("the"), 8);
        assertEquals(searcher1.wordCount("beauty's"), 1);
        assertEquals(searcher1.wordCount("world"), 1);
        assertEquals(searcher1.wordCount("queen"), 0);
        assertEquals(searcher1.wordCount("SONNETS"), 1);
    }

    @Test
    public void testPhraseOccurrence() {
        assertEquals(searcher1.phraseOccurrence("own bud buri").size(), 0);
        assertEquals(searcher1.phraseOccurrence("own bud buriest").size(), 1);
        assertEquals(searcher1.phraseOccurrence("   own bud buriest").size(), 1);
        assertEquals(searcher1.phraseOccurrence("Own bud     buriest").size(), 1);
        assertEquals(searcher1.phraseOccurrence("with self-substantial").size(), 1);
        assertEquals(searcher1.phraseOccurrence("with self-substantial").size(), 1);
        assertEquals(searcher1.phraseOccurrence("the ").size(), 8);

        Pair<Integer, Integer> phraseOccurrence;
        phraseOccurrence = searcher1.phraseOccurrence("own bud buriest").iterator().next();
        assertEquals((int)phraseOccurrence.getLeftValue(), 21);
        assertEquals((int)phraseOccurrence.getRightValue(), 14);


        phraseOccurrence = searcher1.phraseOccurrence("   own bud    buriest").iterator().next();
        assertEquals((int)phraseOccurrence.getLeftValue(), 21);
        assertEquals((int)phraseOccurrence.getRightValue(), 14);

    }


    @Test
    public void testPrefixOccurrence() {
        assertEquals(searcher2.prefixOccurrence("obscure").size(), 4);

        Pair<Integer, Integer> prefixOccurrence;
        prefixOccurrence = searcher2.prefixOccurrence("villain").iterator().next();

        assertEquals((int)prefixOccurrence.getLeftValue(), 13);
        assertEquals((int)prefixOccurrence.getRightValue(), 48);

        assertEquals(searcher2.prefixOccurrence("s").size(), 13);
    }

}