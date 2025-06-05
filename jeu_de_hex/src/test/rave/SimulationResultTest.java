package test.rave;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.Test;
import hex.model.*;

import hex.rave.SimulationResult;

public class SimulationResultTest {
    @Test
    public void testConstructor() {
        // Vérifie que l'objet SimulationResult est correctement instancié
        List<Position> moves = Arrays.asList(new Position(0, 1), new Position(2, 3));
        boolean result = true;
        SimulationResult simulationResult = new SimulationResult(result, moves);
        assertNotNull(simulationResult);
    }
 
    @Test
    public void testGetResult() {
        // Vérifie que getResult() retourne la bonne valeur
        SimulationResult simulationResultTrue = new SimulationResult(true, List.of());
        SimulationResult simulationResultFalse = new SimulationResult(false, List.of());
        assertTrue(simulationResultTrue.getResult());
        assertFalse(simulationResultFalse.getResult());
    }

    @Test
    public void testGetMoves() {
        // Vérifie que getMoves() retourne la liste correcte de mouvements
        List<Position> moves = Arrays.asList(new Position(0, 1), new Position(2, 3));
        SimulationResult simulationResult = new SimulationResult(true, moves);
        assertEquals(moves, simulationResult.getMoves());
    }
}
