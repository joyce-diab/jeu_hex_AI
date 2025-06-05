package test.rave;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import hex.model.*;
import hex.mcts.*;
import hex.rave.NodeRave;

public class NodeRaveTest {

    
    private State state;
    private NodeRave node;
    private Position move1, move2;

    @Before
    public void setUp() {
        // Initialisation d'une grille 3x3
        Color[][] grid = {
            {Color.NONE, Color.RED, Color.NONE},
            {Color.BLUE, Color.NONE, Color.RED},
            {Color.NONE, Color.BLUE, Color.NONE}
        };

        state = new State(grid);
        node = new NodeRave(state);

        move1 = new Position(0, 0);
        move2 = new Position(2, 2);
    }

    @Test
    public void testConstructor() {
        // Vérification de l'initialisation correcte du nœud
        assertNotNull(node.getState());
        assertEquals(0, node.getVictories());
        assertEquals(0, node.getVisits());
        assertNotNull(node.getMovesNotExplored());
        assertTrue(node.getMovesNotExplored().contains(move1));
        assertTrue(node.getMovesNotExplored().contains(move2));
    }

    @Test
    public void testVisitedAndVictory() {
        // Vérification des mises à jour des visites et des victoires
        node.visited();
        node.visited();
        node.victory();

        assertEquals(2, node.getVisits());
        assertEquals(1, node.getVictories());
    }

    @Test
    public void testGetValue() {
        // Vérification de la valeur du nœud basée sur les visites et victoires
        node.visited();
        node.visited();
        assertEquals(0.0, node.getValue(), 0.01);

        node.victory();
        assertEquals(0.5, node.getValue(), 0.01); 
    }

    @Test
    public void testRaveStats() {
        // Mise à jour des statistiques RAVE pour les deux positions
        node.updateRaveStats(move1, true);
        node.updateRaveStats(move2, false);
        node.updateRaveStats(move1, true);

        assertEquals(2, node.getRaveVisits(move1));  // Nombre de visites pour move1
        assertEquals(2, node.getRaveVictories(move1), 0.01);  // Nombre de victoires pour move1
        assertEquals(1, node.getRaveVisits(move2));  // Nombre de visites pour move2
        assertEquals(0, node.getRaveVictories(move2), 0.01);  // Nombre de victoires pour move2
    }

    @Test
    public void testGetRaveValue() {
        // Vérification de la valeur RAVE d'un coup donné
        node.updateRaveStats(move1, true);
        node.updateRaveStats(move1, false);
        assertEquals(0.5, node.getRaveValue(move1), 0.01);
    }

    @Test
    public void testUcb() {
        NodeRave parent = new NodeRave(state);
        node.setParent(parent);

        // Vérifie que le score UCB est MAX_VALUE si le nœud n'a jamais été visité
        assertEquals(Double.MAX_VALUE, node.ucb(), 0.01);

        // Simule des visites et victoires pour le parent et le nœud
        parent.visited();
        parent.visited();
        node.visited();
        node.victory();

        // Calcule le score UCB attendu
        double expectedUcb = Math.sqrt(2) * Math.sqrt(Math.log(2) / 1);
        assertEquals(expectedUcb, node.ucb(), 0.01);
    }

    @Test
    public void testRaveFormula() {
        double k = 0.5;
        node.updateRaveStats(move1, true);
        node.updateRaveStats(move2, false);
        
        // Calcule la formule RAVE avec les valeurs actuelles
        double formulaValue = node.getRaveFormula(k);

        // Vérifie que la formule RAVE renvoie une valeur non nulle
        assertNotNull(formulaValue);
    }

    @Test
    public void testEqualsAndHashCode() {
        
        NodeRave node2 = new NodeRave(state);
        assertEquals(node, node2); 
        assertEquals(node.hashCode(), node2.hashCode());
        Color[][] newGrid = {
            {Color.NONE, Color.RED, Color.NONE},
            {Color.BLUE, Color.NONE, Color.RED},
            {Color.NONE, Color.RED, Color.NONE}
        };
        State newState = new State(newGrid);
        NodeRave differentNode = new NodeRave(newState);
        assertNotEquals(node, differentNode);  
        assertNotEquals(node.hashCode(), differentNode.hashCode()); 
    }
}
