package test.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import hex.model.*;

public class RobotMCTSTest {

    private Board board;
    private RobotMCTS robot;

    @Before
    public void setUp() {
        board = new Board(3); // Création d'un plateau 3x3
        robot = new RobotMCTS(Color.RED, 1000); // Robot MCTS avec un budget de 1000 simulations
    }

    @Test
    public void testPlay() {
        // Vérifie que le robot joue un coup valide sur un plateau vide
        Position move = robot.play(board);
        assertNotNull(move);
        assertTrue(board.isAvailable(move));

        // Place le pion et vérifie qu'il est bien positionné
        board.placePawn(Color.RED, move);
        assertEquals(Color.RED, board.getPawn(move));
    }
    
}
