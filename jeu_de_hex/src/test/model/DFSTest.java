package test.model;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import hex.model.*;

public class DFSTest {

    private DFS dfs;
    private Color[][] grid;
    private List<Set<Position>> ends;

    @Before
    public void setUp() {
        dfs = new DFS();
        int size = 3;
        grid = new Color[size][size];
        
        // Initialisation de la grille avec des couleurs fictives
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = Color.NONE; // Supposons une couleur est none
            }
        } 
        grid[0][0] = Color.RED;
        grid[0][1] = Color.RED;
        grid[1][1] = Color.RED;
        grid[2][1] = Color.RED;
        grid[2][2] = Color.RED;

        // Définition des extrémités
        Set<Position> start = new HashSet<>(Collections.singletonList(new Position(0, 0)));
        Set<Position> end = new HashSet<>(Collections.singletonList(new Position(2, 2)));
        
        ends = new ArrayList<>();
        ends.add(start);
        ends.add(end);
    }

    @Test
    public void testHasPath() {
        // Vérifie qu'un chemin rouge existe entre les extrémités
        assertTrue(dfs.hasPath(grid, ends, Color.RED));
        
        // Vérifie qu'il ne devrait pas y avoir de chemin bleu
        assertFalse(dfs.hasPath(grid, ends, Color.BLUE));
        
        Color[][] emptyGrid = new Color[3][3];
        for (int i = 0; i < 3; i++) {
            Arrays.fill(emptyGrid[i], Color.NONE);
        }
        
        // Vérifie qu'aucun chemin n'existe dans une grille vide
        assertFalse(dfs.hasPath(emptyGrid, ends, Color.RED));
        
        Color[][] singleCellGrid = new Color[1][1];
        singleCellGrid[0][0] = Color.RED;
        List<Set<Position>> singleEnds = new ArrayList<>();
        singleEnds.add(new HashSet<>(Collections.singletonList(new Position(0, 0))));
        singleEnds.add(new HashSet<>(Collections.singletonList(new Position(0, 0))));
        
        // Vérifie qu'un chemin existe dans une grille 1x1 si la couleur correspond
        assertTrue(dfs.hasPath(singleCellGrid, singleEnds, Color.RED));
    }
    
}
