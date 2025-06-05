package test.mcts;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import hex.mcts.CacheMap;

public class CacheMapTest {

    private CacheMap<Integer, String> cache;

    @Before
    public void setUp() {
        cache = new CacheMap<>(3); // Cache avec une capacité de 3 entrées
    }

    @Test
    public void testRemoveEldestEntry() {
        // Vérifie que l'entrée la plus ancienne est supprimée
        cache.put(1, "One");
        cache.put(2, "Two");
        cache.put(3, "Three");
        assertEquals(3, cache.size());

        cache.put(4, "Four");
        assertEquals(3, cache.size());
        assertFalse(cache.containsKey(1));
        assertTrue(cache.containsKey(2));
        assertTrue(cache.containsKey(3));
        assertTrue(cache.containsKey(4));

        // Vérifie que l'accès met à jour l'ordre des entrées
        cache.get(2);
        cache.put(5, "Five");
        assertFalse(cache.containsKey(3));
        assertTrue(cache.containsKey(2));
        assertTrue(cache.containsKey(4));
        assertTrue(cache.containsKey(5));
    }

    
}
