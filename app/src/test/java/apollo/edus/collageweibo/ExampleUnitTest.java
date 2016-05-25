package apollo.edus.collageweibo;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testStringFormat(){
        String url ="api=%1s";
        System.out.println(String.format(url,"panda"));
        System.out.println(String.format(url,""));
    }
}