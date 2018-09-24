package comp3506.assn2.testdriver;

import static org.junit.Assert.*;

import comp3506.assn2.adts.Map;
import comp3506.assn2.adts.ProbeHashSet;
import comp3506.assn2.adts.Trie;
import comp3506.assn2.adts.Set;
import comp3506.assn2.application.IndexTable;
import comp3506.assn2.application.Searcher;
import comp3506.assn2.application.Utility;
import comp3506.assn2.utils.Pair;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UnitTests {

    private Searcher searcher1, searcher2;

    @Before
    public void before() throws Exception {
        searcher1 = new Searcher("./files/test-input-1.txt", null, null);
        searcher2 = new Searcher("./files/test-input-2.txt", null, null);
    }

    @After
    public void after() throws Exception {

    }

    @Test
    public void testProbeHashSet() {
        Set<Integer> set = new ProbeHashSet<>();
        set.add(1);
        set.add(1);
        assertEquals(set.size(), 1);
        set.add(2);
        assertEquals(set.size(), 2);
        assertTrue(set.contains(2));
        set.remove(1);
        assertFalse(set.contains(1));
    }

    @Test
    public void testProbeHashSetAll() {
        Set<Integer> set1 = new ProbeHashSet<>();
        Set<Integer> set2 = new ProbeHashSet<>();
        set1.add(1);
        set1.add(2);
        set1.add(3);
        set2.add(2);
        set2.add(3);
        set2.add(4);
        assertEquals(set1.size(),3);
        set1.removeAll(set2);
        assertEquals(set1.size(),1);
        assertTrue(set1.contains(1));
        assertFalse(set1.contains(2));
        assertFalse(set1.contains(3));
        assertTrue(set2.contains(3));
        set1.addAll(set2);
        assertEquals(set1.size(),4);
        assertTrue(set1.contains(3));
        assertTrue(set1.contains(4));
        set2.remove(4);
        set1.retainAll(set2);
        assertEquals(set1.size(),2);
        assertTrue(set1.contains(2));
        assertTrue(set1.contains(3));
    }


    @Test
    public void testSanitizeString() {
        String string1 = "    That thereby- ?beauty's rose-adline \"might\" never die,";
        Assert.assertEquals(Utility.sanitizeString(string1), "    that thereby   beauty's rose adline  might  never die ");

        String string2 = "THE SONNETS";
        assertEquals(Utility.sanitizeString(string2), "the sonnets");
    }

    @Test
    public void testTokenizeString() {
        assertEquals(Utility.tokenizeString("From fairest I creatures A we desire increase,").size(), 8);

        Map<Integer, String> tokens = Utility.tokenizeString("That Rome holds of his name; wherein obscurely");
        assertEquals(tokens.size(), 8);
        assertEquals(tokens.get(1), "that");
        assertEquals(tokens.get(30), "wherein");
        assertEquals(tokens.get(38), "obscurely");

        tokens = Utility.tokenizeString("    Of Arthur, whom they say is kill'd to-night");
        assertEquals(tokens.size(), 9);
        assertEquals(tokens.get(1), null);
        assertEquals(tokens.get(5), "of");
        assertEquals(tokens.get(33), "kill'd");
        assertEquals(tokens.get(40), "to");
        assertEquals(tokens.get(43), "night");

        assertEquals(Utility.tokenizeString("THE SONNETS ").size(), 2);
        assertEquals(Utility.tokenizeString("THE SONNETS").size(), 2);
    }

    @Test
    public void testIndexTable() {
        Trie<IndexTable> wordIndexes = new Trie<>();
        wordIndexes.insert("test");
        IndexTable indexTable = new IndexTable();
        indexTable.addPosition(3, 2);
        assertEquals(indexTable.getPositions().size(), 1);
        assertEquals((int)indexTable.getPositions().get(0).getLeftValue(), 3);
        assertEquals((int)indexTable.getPositions().get(0).getRightValue(), 2);
        indexTable.addPosition(4, 10);
        assertEquals(indexTable.getPositions().size(), 2);
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
        assertEquals(searcher1.wordCount("and"), 3);
        assertEquals(searcher1.wordCount("SONNETS"), 1);
    }

    @Test
    public void testPhraseOccurrence() {
        assertEquals(searcher1.phraseOccurrence("something bud buri").size(), 0);
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


    @Test
    public void testWordOnLine() {
        assertEquals(searcher1.wordsOnLine(new String[]{"flame", "with"}).size(), 1);
        assertEquals(searcher1.wordsOnLine(new String[]{"test"}).size(), 0);
        assertEquals(searcher1.wordsOnLine(new String[]{"test", "the"}).size(), 0);
        assertEquals(searcher1.wordsOnLine(new String[]{"the"}).size(), 7);
        assertEquals(searcher1.wordsOnLine(new String[]{"the", "world's"}).size(), 2);
    }

    @Test
    public void testSomeWordOnLine() {
        assertEquals(searcher1.someWordsOnLine(new String[]{"flame", "with"}).size(), 1);
        assertEquals(searcher1.someWordsOnLine(new String[]{"test"}).size(), 0);
        assertEquals(searcher1.someWordsOnLine(new String[]{"test", "the"}).size(), 7);
        assertEquals(searcher1.someWordsOnLine(new String[]{"the"}).size(), 7);
        assertEquals(searcher1.someWordsOnLine(new String[]{"the", "and", "light's"}).size(), 9);
        assertTrue(searcher1.someWordsOnLine(new String[]{"the", "and", "light's"}).contains(1));
        assertTrue(searcher1.someWordsOnLine(new String[]{"the", "and", "light's"}).contains(22));
        assertTrue(searcher1.someWordsOnLine(new String[]{"the", "and", "light's"}).contains(24));
    }

    @Test
    public void testWordsNotOnLine() {
        assertEquals(searcher1.wordsNotOnLine(new String[]{"flame", "with"}, new String[]{"with"}).size(), 0);
        assertTrue(searcher1.wordsNotOnLine(new String[]{"flame", "with"}, new String[]{"tester"}).contains(16));
        assertEquals(searcher1.wordsNotOnLine(new String[]{"the", "world's"}, new String[]{"art"}).size(), 1);
        assertTrue(searcher1.wordsNotOnLine(new String[]{"the", "world's"}, new String[]{"art"}).contains(24));
        assertEquals(searcher1.wordsNotOnLine(new String[]{"the"}, new String[]{"and"}).size(), 5);
        assertTrue(searcher1.wordsNotOnLine(new String[]{"the"}, new String[]{"and"}).contains(23));
        assertTrue(searcher1.wordsNotOnLine(new String[]{"the"}, new String[]{"and"}).contains(1));
        assertFalse(searcher1.wordsNotOnLine(new String[]{"the"}, new String[]{"and"}).contains(20));
    }

}