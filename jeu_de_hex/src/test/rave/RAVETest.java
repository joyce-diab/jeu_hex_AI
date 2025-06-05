package test.rave;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import hex.model.*;
import hex.rave.*;

public class RAVETest {
    private RAVE rave;
    private Board board;
    private Random random;

    @Before
    public void setUp() {
        random = new Random(42); 
        rave = new RAVE(1000, Color.RED, random);
        board = new Board(5);
    }

    @Test
    public void testGetBestMove() {
        Position bestMove = rave.getBestMove(board);
        assertNotNull("Le meilleur mouvement ne doit pas être null", bestMove);
        assertTrue("Le meilleur mouvement doit être une position valide",
                   Board.isPositionValid(bestMove, board.getSize()));
    }
    
}
