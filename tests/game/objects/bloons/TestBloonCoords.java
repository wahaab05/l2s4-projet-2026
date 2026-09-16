package game.objects.bloons;

import org.junit.jupiter.api.*;

import game.cells.RoadCell;
import game.roads.RoadTile;
import game.roads.Road;
import game.objects.bloons.templates.*;
import utils.plane2d.Direction;

import static org.junit.jupiter.api.Assertions.*;

public class TestBloonCoords {

	private Bloon b;
	private RoadTile t;

	@BeforeEach
	public void before()
	{
		t = new RoadTile(new RoadCell(0, 0), 0, new Road(Direction.RIGHT));
		b = new BlueBloon(t);
	}

	@Test
	public void testBloonSpawnCoords()
	{
		// cell's coords = 0:0
		// bloon's coords should be 0:0.5
		assertEquals(0, b.x());
		assertEquals(0.5, b.y());
	}
}
