package test.model;

import static org.junit.Assert.*;
import org.junit.Test;
import hex.model.Color;
public class ColorTest {

    @Test
    public void testGetOppositeColor() {
        // Vérifie les couleurs opposées
        assertEquals(Color.BLUE, Color.RED.getOppositeColor());
        assertEquals(Color.RED, Color.BLUE.getOppositeColor());
        assertEquals(Color.RED, Color.NONE.getOppositeColor()); 
    }

    @Test
    public void testGetColorText() {
        // Vérifie les symboles associés à chaque couleur
        assertEquals("R", Color.RED.getColorText());
        assertEquals("B", Color.BLUE.getColorText());
        assertEquals(".", Color.NONE.getColorText());
    }
    
    
}
