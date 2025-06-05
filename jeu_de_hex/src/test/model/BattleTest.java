package test.model;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import hex.model.*;

public class BattleTest {
    @Test
    public void testGetCurrentPlayer() {
        Board board = new Board(3);
        Player player1 = new PlayerTerminal(Color.RED);
        Player player2 = new PlayerTerminal(Color.BLUE);
        List<Player> players = Arrays.asList(player1, player2);

        Battle battle = new Battle(board, players);

        // Vérifie que le joueur courant est initialement Player 1
        assertEquals(player1, battle.getCurrentPlayer());
    }

    @Test
    public void testNext() {
        Board board = new Board(3);
        Player player1 = new PlayerTerminal(Color.RED);
        Player player2 = new PlayerTerminal(Color.BLUE);
        List<Player> players = Arrays.asList(player1, player2);

        Battle battle = new Battle(board, players);

        // Passe au joueur suivant
        battle.next();
        assertEquals(player2, battle.getCurrentPlayer());

        // Passe encore au joueur suivant
        battle.next();
        assertEquals(player1, battle.getCurrentPlayer());
    }

    @Test
    public void testGameFinished() {
        Board board = new Board(3);
        Player player1 = new PlayerTerminal(Color.RED);
        Player player2 = new PlayerTerminal(Color.BLUE);
        List<Player> players = Arrays.asList(player1, player2);

        Battle battle = new Battle(board, players);

        // Simule une condition de victoire pour le joueur RED
        board.placePawn(Color.RED, 0, 0);
        board.placePawn(Color.RED, 1, 0);
        board.placePawn(Color.RED, 2, 0);

        // Vérifie que RED gagne
        assertTrue(battle.gameFinished());

        // Réinitialise le plateau et place des pions sans condition de victoire
        board = new Board(3);
        battle = new Battle(board, players);
        board.placePawn(Color.RED, 0, 0);
        board.placePawn(Color.BLUE, 1, 0);

        // Vérifie que la partie n'est pas terminée
        assertFalse(battle.gameFinished());
    }
    @Test
    public void testGetBoard() {
        Board board = new Board(3);
        Player player1 = new PlayerTerminal(Color.RED);
        Player player2 = new PlayerTerminal(Color.BLUE);
        List<Player> players = Arrays.asList(player1, player2);

        Battle battle = new Battle(board, players);

        // Vérifie que le plateau renvoyé est celui utilisé dans la bataille
        assertEquals(board, battle.getBoard());
    }
    
}
