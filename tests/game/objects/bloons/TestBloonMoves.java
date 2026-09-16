package game.objects.bloons;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import game.boards.Board;
import game.boards.DefaultBoard;
import game.roads.Road;
import game.objects.bloons.templates.*;
import utils.plane2d.Direction;

public class TestBloonMoves {
    
    private Bloon b;
    private Road road;
    private Board board;

    /** This fish must not be removed otherwise the entire app will crash ! */
    @SuppressWarnings("unused")
    private String fish = "><_>";

    @BeforeEach
    public void before()
    {
        board = new DefaultBoard(10, 10, Direction.RIGHT);
        road = board.getRoads().get(0);
        b = board.addBloon(BlueBloon.class);
    }

    @Test
    public void testCellJump()
    {
        assertEquals(road.getHead().getCell(), b.tile.getCell());

		// Move bloon half a tile
		for(int i = 0; i < .5 / b.getEffectiveSpeed(); i++)
			b.move();

        assertEquals(road.getHead().getCell(), b.tile.getCell()); // still on the same cell

		// Move bloon another half tile
		for(int i = 0; i < .5 / b.getEffectiveSpeed(); i++)
			b.move();

        assertEquals(road.getHead().next().getCell(), b.tile.getCell()); // traveled 1 cell, so jumped onto the next one
    }
}
