package utils.plane2d;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import utils.maths.Comparator;

public class TestVector2f {
    
    private Vector2f v1;
    private Vector2f v2;

    @Test
    public void testInstanciation() {
        v1 = new Vector2f(10, 0);
        assertEquals(0, Comparator.compFloat(v1.x(), 10.0));
        assertEquals(0, Comparator.compFloat(v1.y(), 0.0));

        v2 = new Vector2f(1.5, 5.123648);
        assertEquals(0, Comparator.compFloat(v2.x(), 1.5));
        assertEquals(0, Comparator.compFloat(v2.y(), 5.123648));
    }
    // |\    \ \ \ \ \ \ \      __
    // |  \    \ \ \ \ \ \ \   | O~-_
    // |   >----|-|-|-|-|-|-|--|  __/
    // |  /    / / / / / / /   |__\
    // |/     / / / / / / /
}
