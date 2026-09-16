package utils.maths;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestComparator {

    @Test
    public void testEquals()
    {
        assertEquals(0, Comparator.compFloat(0.0, 0.0));
        assertEquals(0, Comparator.compFloat(0.0, 0.000001));
    }

    @Test
    public void testGreater()
    {
        assertEquals(1, Comparator.compFloat(1.0, 0.0));
        assertEquals(1, Comparator.compFloat(1.0, 0.000001));
    }
    //  __v_
    // (____\/{
    @Test
    public void testSmaller()
    {
        assertEquals(-1, Comparator.compFloat(0.0, 1.0));
        assertEquals(-1, Comparator.compFloat(0.0, 1.000001));
    }

    
}
