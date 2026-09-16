package game.objects.projectiles.templates;

import game.objects.bloons.Bloon;
import game.objects.projectiles.Projectile;
import game.objects.turrets.Targetable;
import utils.config.ConfigReader;
import utils.plane2d.Vector2f;

/**
 * DartProjectile - simple dart for basic turrets
 * Deals 1 damage to a single bloon
 */
public class DartProjectile extends Projectile {

	private static final int DART_DAMAGE = ConfigReader.getInt("dart_projectile_damage");
	private static final float DART_SPEED = ConfigReader.getFloat("dart_projectile_speed");

	/**
	 * Create a new Dart projectile at a given set of coords, targetting a specific bloon.
	 * @param coords Spawning coords of projectile
	 * @param target Targetted bloon
	 */
	public DartProjectile(Vector2f coords, Targetable target) {
		super(
			"Dart",	// name
			coords,			// initial position
			target,
			DART_SPEED,		// speed
			DART_DAMAGE
		);
	}

	
	@Override
	protected void applyEffect(Bloon b) {
		b.takeDamage(DartProjectile.DART_DAMAGE);
	}
}
