package game.objects.projectiles;

import org.junit.jupiter.api.*;

import game.roads.Road;
import game.cells.RoadCell;
import game.objects.bloons.Bloon;
import game.objects.bloons.templates.BlueBloon;
import game.objects.projectiles.templates.DartProjectile;
import game.roads.RoadTile;
import utils.plane2d.Vector2f;

import static org.junit.jupiter.api.Assertions.*;

/**
 * There were a missing doc so here we go
 *   ;,//;,    ,;/
 *  o:::::::;;///
 * >::::::::;;\\\
 *   ''\\\\\'" ';\
 */
public class TestProjectile {

	@Test
	public void testProjectileFactory()
	{
		// Initial coords of the projectile
		Vector2f coords = new Vector2f(0, 0);
		// Target of the projectile
		Bloon target = new BlueBloon(
			new RoadTile(
				new RoadCell(0, 0), 0, new Road()
			)
		);

		Projectile p = Projectile.create(DartProjectile.class, coords, target);
		assertInstanceOf(DartProjectile.class, p);
	}
}
