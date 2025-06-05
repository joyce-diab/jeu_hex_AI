package test.mcts;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import hex.model.*;

import hex.mcts.*;

public class StateTest {

    private Color[][] grid;
    private State state;

    @Before
    public void setUp() {
        // Initialisation d'une grille 3x3
        grid = new Color[][]{
            {Color.NONE, Color.RED, Color.NONE},
            {Color.BLUE, Color.NONE, Color.RED},
            {Color.NONE, Color.BLUE, Color.NONE}
        };

        state = new State(grid);
    }

    @Test
    public void testConstructor() {
        // Vérifie que la grille et son état sont correctement initialisés
        assertNotNull(state.getGrid());
        assertEquals(3, state.getGrid().length);

        Map<Position, Color> expectedState = new HashMap<>();
        expectedState.put(new Position(0, 1), Color.RED);
        expectedState.put(new Position(1, 0), Color.BLUE);
        expectedState.put(new Position(1, 2), Color.RED);
        expectedState.put(new Position(2, 1), Color.BLUE);

        assertEquals(expectedState, state.getState());
    }

    @Test
    public void testGetPossibleMoves() {
        // Vérifie que les coups possibles sont correctement récupérés
        Set<Position> expectedMoves = new HashSet<>();
        expectedMoves.add(new Position(0, 0));
        expectedMoves.add(new Position(0, 2));
        expectedMoves.add(new Position(1, 1));
        expectedMoves.add(new Position(2, 0));
        expectedMoves.add(new Position(2, 2));

        assertEquals(expectedMoves, state.getPossibleMoves());
    }

    @Test
    public void testEqualsAndHashCode() {
        State otherState = new State(grid);
        // Vérifie que deux états identiques sont égaux
        assertTrue(state.equals(otherState));
        assertEquals(state.hashCode(), otherState.hashCode());

        // Modifier une case pour voir si equals et hashCode deviennent incorrects
        Color[][] differentGrid = {
            {Color.NONE, Color.RED, Color.NONE},
            {Color.BLUE, Color.NONE, Color.RED},
            {Color.NONE, Color.NONE, Color.NONE} 
        };

        State differentState = new State(differentGrid);
        assertFalse(state.equals(differentState));
        assertNotEquals(state.hashCode(), differentState.hashCode());
    }

    @Test
    public void testCloneState() {
        // Vérifie que l'état cloné est identique mais indépendant de l'état original
        Map<Position, Color> clonedState = state.cloneState();
        assertEquals(state.getState(), clonedState);
        assertNotSame(state.getState(), clonedState); // Vérifie que c'est bien une copie indépendante
    }
    
    
}
