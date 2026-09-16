package game.roads;

import game.cells.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestRoad {

	private Road road;
	private Cell c1;
	private Cell c2;

	@BeforeEach
	public void before()
	{
		this.road = new Road();
		this.c1 = new RoadCell(0, 0);
		this.c2 = new TurretCell(0, 1);
	}

	@Test
	public void testCellInsertion()
	{
		assertEquals(null, road.getHead()); // empty list
		assertEquals(0, road.getDistToEnd()); // no cells so no distance
		assertFalse(road.contains(c1)); // c1 not in road

		road.addCell(c1);
		assertTrue(road.contains(c1)); // c1 in road
		assertEquals(new RoadTile(c1, 0, new Road()), road.getHead()); // c1 is head
		assertEquals(new RoadTile(c1, 0, new Road()), road.getTail()); // c1 is tail
		assertEquals(1, road.getDistToEnd());
		RoadTile t1 = road.getHead();

		road.addCell(c2);
		assertEquals(new RoadTile(c2, 0, new Road()), road.getHead()); // c2 is head
		assertEquals(2, road.getDistToEnd());
		assertNotEquals("🦈", c2);
		RoadTile t2 = road.getHead();

		// the list here is:
		// head -> t2 -> t1 -> tail

		assertEquals(t2,    t1.previous());
		assertEquals(null,  t1.next());
		assertEquals(null,  t2.previous());
		assertEquals(t1,    t2.next());
	}
}
