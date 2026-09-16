package game.objects.projectiles.templates;

import game.objects.bloons.Bloon;
import game.objects.projectiles.AreaProjectile;
import game.objects.turrets.Targetable;
import utils.config.ConfigReader;
import utils.plane2d.Vector2f;

/**
 * BombProjectile - deals area damage
 * Damage = 2 and affects all bloons in range
 */
public class BombProjectile extends AreaProjectile {

	private static final int BOMB_DAMAGE = ConfigReader.getInt("bomb_projectile_damage");
	private static final float BOMB_SPEED = ConfigReader.getFloat("bomb_projectile_speed");
	private static final float BOMB_RANGE = ConfigReader.getFloat("bomb_projectile_range");


	/**
	 * Create a new Bomb project at a given set of coordinates, targetting a bloon.
	 * @param coords Spawning coords of the bloon
	 * @param target Targetted bloon
	 */
	public BombProjectile(Vector2f coords, Targetable target) {
		super(
			"Bomb",		// name
			coords,		// initial position
			target,
			BOMB_SPEED,			 // speed
			BOMB_RANGE,		// explosion range
			BOMB_DAMAGE
		);
	}


	@Override
	protected void applyEffect(Bloon b) {
		b.takeDamage(BOMB_DAMAGE);
	}


}

