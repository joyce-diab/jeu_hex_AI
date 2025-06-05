package test.model;

import org.junit.Test;
import static org.junit.Assert.*;

import hex.model.*;

public class PositionTest {

    @Test
    public void testConstructor() {
        Position position = new Position(3, 4);

        assertEquals(3, position.getX());
        assertEquals(4, position.getY());
    }

    
    
}
