package test.model;

import static org.junit.Assert.*;
import org.junit.Test;
import hex.model.*;
public class BoardTest {

    @Test
    public void testBoardInit() {
        Board board = new Board(3); // Crée un plateau de taille 3x3

        // Vérifie que toutes les cases sont initialisées à NONE
        for (int i = 0; i < board.getSize(); i++) {
            for (int j = 0; j < board.getSize(); j++) {
                assertEquals(Color.NONE, board.getGrid()[i][j]);
            }
        }
    }

    @Test
    public void testIsPositionValid() {
        Board board = new Board(3);

        // Vérifie que les positions valides sont détectées
        assertTrue(Board.isPositionValid(new Position(0, 0),3));
        assertTrue(Board.isPositionValid(new Position(2, 2),3));

        // Vérifie que les positions invalides sont détectées
        assertFalse(Board.isPositionValid(new Position(-1, 0),3));
        assertFalse(Board.isPositionValid(new Position(3, 3),3));
    }

    @Test
    public void testIsAvailable() {
        Board board = new Board(3);
        Position pos = new Position(1, 1);

        // Vérifie qu'une position vide est disponible
        assertTrue(board.isAvailable(pos));

        // Place un pion sur cette position
        board.placePawn(Color.RED, pos);

        // Vérifie qu'elle n'est plus disponible
        assertFalse(board.isAvailable(pos));
    }

    @Test
    public void testPlacePawn() {
        Board board = new Board(3);
        Position pos = new Position(0, 0);

        // Place un pion rouge sur (0, 0)
        assertTrue(board.placePawn(Color.RED, pos));
        assertEquals(Color.RED, board.getGrid()[0][0]);

        // Tente de placer un pion bleu sur la même position
        assertFalse(board.placePawn(Color.BLUE, pos));
        assertEquals(Color.RED, board.getGrid()[0][0]); // La couleur reste RED
    }

    @Test
    public void testGetNeighbors() {
        Board board = new Board(3);
        Position pos = new Position(1, 1);
        var neighbors = Board.getNeighbors(pos,3);

        // Vérifie que les voisins incluent toutes les positions valides autour de (1, 1)
        assertTrue(neighbors.contains(new Position(0, 1)));
        assertTrue(neighbors.contains(new Position(2, 1)));
        assertTrue(neighbors.contains(new Position(1, 0)));
        assertTrue(neighbors.contains(new Position(1, 2)));

        // Vérifie que les positions hors du plateau ne sont pas incluses
        assertFalse(neighbors.contains(new Position(-1, 1)));
        assertFalse(neighbors.contains(new Position(3, 1)));
    }

    @Test
    public void testAreNeighbors() {
        Board board = new Board(3);

        // Vérifie que deux positions adjacentes sont voisines
        assertTrue(board.areNeighbors(new Position(0, 0), new Position(0, 1)));

        // Vérifie que deux positions non adjacentes ne sont pas voisines
        assertFalse(board.areNeighbors(new Position(0, 0), new Position(2, 2)));
    }

    @Test
    public void testIsAWin() {
        Board board = new Board(3);

        // Place des pions pour créer une condition de victoire pour RED
        board.placePawn(Color.RED, 0, 0);
        board.placePawn(Color.RED, 1, 0);
        board.placePawn(Color.RED, 2, 0);

        // Vérifie que RED gagne
        assertTrue(board.isAWin(Color.RED));

        // Vérifie que BLUE ne gagne pas
        assertFalse(board.isAWin(Color.BLUE));
    }
    
}
