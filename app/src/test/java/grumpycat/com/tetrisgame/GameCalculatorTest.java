package grumpycat.com.tetrisgame;

import com.grumpycat.tetrisgame.core.GameCalculator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class GameCalculatorTest {
    @Test
    public void calculate_score_isCorrect() {
        int[] lines1 = {1,2,3};
        assertEquals(9, GameCalculator.calculateScore(lines1, 1));

        int[] lines11 = {1,2,3,4,10};
        assertEquals(17, GameCalculator.calculateScore(lines11, 1));

        int[] lines2 = {1,2,4};
        assertEquals(5, GameCalculator.calculateScore(lines2, 1));

        int[] lines3 = {1,3,5};
        assertEquals(3, GameCalculator.calculateScore(lines3, 1));

        int[] lines4 = {1,5,9};
        assertEquals(3, GameCalculator.calculateScore(lines4, 1));
    }
}