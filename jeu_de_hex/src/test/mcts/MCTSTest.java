package test.mcts;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import java.util.*;

import hex.mcts.*;
import hex.model.*;

public class MCTSTest {

    private MCTS mcts;
    private Board board;
    private Random fixedRandom;

    @Before
    public void setUp() {
        fixedRandom = new Random(42); // Initialisation d'un générateur de nombres aléatoires avec une seed fixe
        mcts = new MCTS(1000, Color.BLUE, fixedRandom); // Création de l'instance MCTS avec un budget de 1000 simulations et couleur BLUE
        board = new Board(5); // Plateau de jeu de taille 5x5
    }

    @Test
    public void testGetBestMove() {
        // Vérifie que le meilleur coup renvoyé par MCTS est valide
        Position bestMove = mcts.getBestMove(board);
        assertNotNull(bestMove); // Vérifie que le meilleur coup n'est pas null
        assertTrue(board.isAvailable(bestMove)); // Vérifie que le coup est dans une position disponible sur le plateau
    }

    @Test
    public void testGetBestPosition() {
        // Vérifie que le meilleur coup est celui du nœud le plus visité
        NodeMcts rootNode = new NodeMcts(new State(Board.cloneGrid(board.getGrid())));

        NodeMcts child1 = new NodeMcts(new State(Board.cloneGrid(board.getGrid())));
        NodeMcts child2 = new NodeMcts(new State(Board.cloneGrid(board.getGrid())));

        child1.setLastMove(new Position(1, 1));
        child2.setLastMove(new Position(2, 2));

        for (int i = 0; i < 3; i++) child1.visited(); 
        for (int i = 0; i < 5; i++) child2.visited(); 

        rootNode.addChild(child1); // Ajoute les enfants au nœud racine
        rootNode.addChild(child2);

        Position bestMove = mcts.getBestPosition(rootNode); 
        assertEquals(new Position(2, 2), bestMove); // Le meilleur coup doit être celui du nœud avec le plus de visites
    }



    @Test
    public void testGetBestRatio() {
        // Vérifie que le nœud avec le meilleur ratio victoire/visites est correctement sélectionné
        NodeMcts rootNode = new NodeMcts(new State(Board.cloneGrid(board.getGrid())));

        NodeMcts child1 = new NodeMcts(new State(Board.cloneGrid(board.getGrid())));
        NodeMcts child2 = new NodeMcts(new State(Board.cloneGrid(board.getGrid())));

        child1.visited(); child1.victory(); // Child1 a une victoire
        child2.visited(); child2.visited(); child2.visited(); child2.victory(); 

        rootNode.addChild(child1);
        rootNode.addChild(child2);

        //NodeMcts bestRatioNode = mcts.getBestRatio(rootNode); // Récupère le meilleur nœud basé sur le ratio victoire/visites
        //assertNotNull(bestRatioNode); // Vérifie que le nœud avec le meilleur ratio n'est pas null
        //assertEquals(child1, bestRatioNode); // Le nœud avec le meilleur ratio devrait être celui avec le plus de victoires
    }

    @Test
    public void testSelectAndExpand() {
        // Vérifie la méthode selectAndExpand qui sélectionne et étend un nœud
        NodeMcts rootNode = new NodeMcts(new State(Board.cloneGrid(board.getGrid())));

        mcts.setRed_ends(board.getRedEnds());
        mcts.setBlue_ends(board.getBlueEnds());
        NodeMcts expandedNode = mcts.selectAndExpand(rootNode); 

        assertNotNull(expandedNode); 
    }

    @Test
    public void testCreateChild() {
        // Vérifie que la création d'un enfant d'un nœud est correcte
        NodeMcts parent = new NodeMcts(new State(Board.cloneGrid(board.getGrid())));
        Position move = new Position(4, 4);

        NodeMcts child = mcts.createChild(parent, move); 
        assertNotNull(child); 
        assertEquals(move, child.getLastMove());
        assertEquals(parent, child.getParent()); // Vérifie que le parent de l'enfant est bien celui spécifié
    }
    public void testIsAWinWithGrid() {
        Color[][] testGrid = new Color[5][5];
    
        // Initialisation de la grille avec NONE
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                testGrid[i][j] = Color.NONE;
            }
        }
    
        // Création d'un chemin gagnant rouge (de haut en bas)
        testGrid[0][2] = Color.RED;
        testGrid[1][2] = Color.RED;
        testGrid[2][2] = Color.RED;
        testGrid[3][2] = Color.RED;
        testGrid[4][2] = Color.RED;
    
        boolean result = mcts.isAWin(Color.RED, testGrid);
        assertTrue(result);
    
        boolean resultBlue = mcts.isAWin(Color.BLUE, testGrid);
        assertFalse(resultBlue);
    }

    @Test
    public void testGetBudget() {
        // Vérifie que le budget est bien celui défini à l'initialisation
        assertEquals(1000, mcts.getBudget());
    }

    @Test
    public void testGetPlayerColor() {
        // Vérifie que la couleur du joueur est correcte
        assertEquals(Color.BLUE, mcts.getPlayerColor());
    }

    @Test
    public void testGetMovesNotExplored() {
        // Vérifie que la méthode getMovesNotExplored retourne bien les mouvements non explorés
        Board board = new Board(3);  // Plateau de taille 3x3
        NodeMcts rootNode = new NodeMcts(new State(Board.cloneGrid(board.getGrid())));

        List<Position> moves = rootNode.getMovesNotExplored();

        assertNotNull(moves); 
        assertFalse(moves.isEmpty());
    }
    
}
