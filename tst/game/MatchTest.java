package game;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class MatchTest {

    @Test
    public void testIsCross() {
        Match match = new Match();
        match.add(new Orb(Color.H, 1, 1));
        match.add(new Orb(Color.H, 1, 0));
        match.add(new Orb(Color.H, 1, 2));
        match.add(new Orb(Color.H, 0, 1));
        match.add(new Orb(Color.H, 2, 1));

        System.out.println(match.toPrettyString());

        assertTrue(match.isCross());
    }

}
