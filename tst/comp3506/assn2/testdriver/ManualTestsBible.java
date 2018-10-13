package comp3506.assn2.testdriver;

import comp3506.assn2.application.AutoTester;
import comp3506.assn2.application.Search;
import comp3506.assn2.utils.Pair;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ManualTestsBible {

    private static Search searchApplication;

    @BeforeClass
    public static void before() throws Exception {
        searchApplication = new AutoTester("./files/bible.txt", "./files/bible-index.txt", "./files/stop-words.txt");
    }

    @Test
    public void testWordCount() {
//        assertEquals(searchApplication.wordCount("Testament"), 16);
        List<Pair<Integer, Integer>> phraseOccurrence;
        phraseOccurrence = searchApplication.phraseOccurrence("Testament");

        for (Pair<Integer, Integer> result: phraseOccurrence) {
            System.out.println(result.getLeftValue() + " " + result.getRightValue());

        }
        assertEquals(searchApplication.wordCount("Testament"), 16);
    }


}