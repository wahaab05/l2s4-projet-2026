package game.cells;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;


import game.objects.turrets.Turret;
import game.objects.bloons.templates.RedBloon;

import game.roads.Road;
import game.roads.RoadTile;

public class TestTurretCell
{
	private TurretCell cell;
	private Turret t;
	private RoadTile rt;

	@BeforeEach
	public void setInstances()
	{
		cell = new TurretCell(1, 2);
		t = new game.objects.turrets.templates.SniperMonkey(cell);
		cell.addTurret(t);
		rt = new RoadTile(cell, 0, new Road());
	}
		
	@Test
	public void turretCellTurretGetterWorksTest()
	{
		assertNotNull(cell.getTurrets());
		assertFalse(cell.getTurrets().isEmpty());
		assertEquals(t, cell.getTurrets().get(0));
	}

	@Test
	public void turretCoordGetterWorksTest()
	{
		assertEquals(1, cell.getX());
		assertEquals(2, cell.getY());

	}

	@Test
	public void turretCellAcceptsTurretsAndBloonsTest()
	{
		assertTrue( cell.canPlace(new RedBloon(rt)) );
		assertTrue( cell.canPlace(t) );

	}
}
