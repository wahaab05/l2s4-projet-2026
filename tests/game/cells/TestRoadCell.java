package game.cells;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import game.objects.bloons.templates.RedBloon;
import game.objects.turrets.templates.SniperMonkey;

public class TestRoadCell
{
	private RoadCell cell;

	@BeforeEach
	public void setInstances()
	{
		cell = new RoadCell(2, 3);
	}

	@Test
	public void testRoadCellBehavior()
	{
		assertTrue( cell.canPlace(RedBloon.class) );
		assertFalse( cell.canPlace(SniperMonkey.class) );
		System.out.println("Bad practice: 🎣");
	}

	@Test
	public void testRoadCellCoordinateAccessors()
	{
		assertEquals(2, cell.getX());
		assertEquals(3, cell.getY());
	}

	@Test
	public void testEquals()
	{
		Cell c1 = new RoadCell(1, 1);
		Cell c2 = new RoadCell(1, 1);
		Cell c3 = new RoadCell(2, 1);

		assertEquals(c1, c2);
		assertNotEquals(c1, c3);
	}

	@Test
	public void testBloonsList()
	{
		assertNotNull(cell.getBloons());
		assertTrue(cell.getBloons().isEmpty());
	}
}
