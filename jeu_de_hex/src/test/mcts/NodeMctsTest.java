package test.mcts;

import static org.junit.Assert.*;
import java.util.HashSet;
import org.junit.Before;
import org.junit.Test;
import hex.mcts.*;
import hex.model.*;

public class NodeMctsTest {

    private State state;
    private NodeMcts node;
    private Position move1, move2;

    @Before
    public void setUp() {
        // Création d'une grille 3x3 pour initialiser l'état
        Color[][] grid = {
            {Color.NONE, Color.RED, Color.NONE},
            {Color.BLUE, Color.NONE, Color.RED},
            {Color.NONE, Color.BLUE, Color.NONE}
        };

        state = new State(grid);
        node = new NodeMcts(state);

        move1 = new Position(0, 0);
        move2 = new Position(2, 2);
    }

    @Test
    public void testConstructor() {
        // Vérifie que l'état du nœud est correctement initialisé
        assertNotNull(node.getState());
        assertEquals(0, node.getVictories());
        assertEquals(0, node.getVisites());
        assertNull(node.getParent());
        assertNull(node.getLastMove());
        assertNotNull(node.getMovesNotExplored());
        assertEquals(state.getPossibleMoves(), new HashSet<>(node.getMovesNotExplored()));
    }

    @Test
    public void testParentChildRelationship() {
        NodeMcts child = new NodeMcts(state);
        child.setParent(node);
        node.addChild(child);

        // Vérifie que le parent et l'enfant sont correctement liés
        assertEquals(node, child.getParent());
        assertTrue(node.getChildren().contains(child));
    }

    @Test
    public void testVisitsAndVictories() {
        node.visited();
        node.visited();
        node.victory();

        // Vérifie que les visites et victoires sont correctement mises à jour
        assertEquals(2, node.getVisites());
        assertEquals(1, node.getVictories());
    }

    @Test
    public void testGetValue() {
        node.visited();
        
        // Vérifie que la valeur du nœud est correcte après une visite sans victoire
        assertEquals(0.0, node.getValue(), 0.01);

        node.victory();
        
        // Vérifie que la valeur du nœud est correcte après une victoire
        assertEquals(1.0, node.getValue(), 0.01);
    }

    @Test
    public void testUcb() {
        NodeMcts parent = new NodeMcts(state);
        node.setParent(parent);

        // Vérifie que le score UCB est MAX_VALUE si le nœud n'a jamais été visité
        assertEquals(Double.MAX_VALUE, node.ucb(), 0.01);

        // Simule des visites et victoires pour le parent et le nœud
        parent.visited();
        parent.visited();
        node.visited();
        node.victory();

        // Calcule le score UCB attendu
        double expectedUcb = (1.0 / 1.0) + Math.sqrt(2) * Math.sqrt(Math.log(2) / 1);
        assertEquals(expectedUcb, node.ucb(), 0.01);
    }

    @Test
    public void testMovesNotExplored() {
        // Vérifie que le coup move1 est dans la liste des coups non explorés avant suppression
        assertTrue(node.getMovesNotExplored().contains(move1));

        node.removeExploredMove(move1);
        
        // Vérifie que le coup move1 a bien été retiré de la liste après suppression
        assertFalse(node.getMovesNotExplored().contains(move1));
    }

    @Test
    public void testEqualsAndHashCode() {
        NodeMcts node2 = new NodeMcts(state);

        // Vérifie que deux nœuds avec le même état sont égaux
        assertEquals(node, node2);

        // Vérifie que le hashCode des deux nœuds égaux est identique
        assertEquals(node.hashCode(), node2.hashCode());
    }
}
