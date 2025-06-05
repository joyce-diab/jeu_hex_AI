package test.model;

import static org.junit.Assert.*;
import org.junit.Test;
import hex.model.*;
public class RobotTest {

    @Test
    public void testRobotInitialization() {
        // Vérifie que la couleur du robot est correctement initialisée
        assertEquals(Color.RED, new Robot(Color.RED).getColor());
    }

    @Test
    public void testRobotPlay() {
        // Vérifie que le robot joue une position valide sur un plateau vide
        Board board = new Board(3);
        Robot robot = new Robot(Color.RED);
        Position position = robot.play(board);
        assertTrue(Board.isPositionValid(position, 3));
        assertTrue(board.isAvailable(position));

        // Vérifie que le robot joue uniquement sur une position disponible
        board.placePawn(Color.RED, 0, 0);
        board.placePawn(Color.RED, 1, 1);
        position = new Robot(Color.BLUE).play(board);
        assertTrue(Board.isPositionValid(position, 3));
        assertTrue(board.isAvailable(position));
        assertNotEquals(new Position(0, 0), position);
        assertNotEquals(new Position(1, 1), position);
    }
}
