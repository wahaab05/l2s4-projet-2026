package game.objects.projectiles.templates;

import game.objects.bloons.Bloon;
import game.objects.projectiles.AreaProjectile;
import game.objects.turrets.Targetable;
import utils.config.ConfigReader;
import utils.plane2d.Vector2f;

/**
 * ExtraBombProjectile - bomb with larger blast radius
 * Damage = 2, area effect
 */
public class ExtraBombProjectile extends AreaProjectile {

	private static final int EXTRA_BOMB_DAMAGE = ConfigReader.getInt("extra_bomb_projectile_damage");
	private static final float EXTRA_BOMB_SPEED = ConfigReader.getFloat("extra_bomb_projectile_speed");
	private static final float EXTRA_BOMB_RANGE = ConfigReader.getFloat("extra_bomb_projectile_range");

	/**
	 * Create a new ExtraBombProjectile, a special type of {@link BombProjectile}.
	 * @param coords Spawning coordinates 🪼
	 * @param target Bloon to target
	 */
	public ExtraBombProjectile(Vector2f coords, Targetable target) {
		super(
			"Extra Bomb", // name
			coords,		  // initial position
			target,
			EXTRA_BOMB_SPEED,			 // speed
			EXTRA_BOMB_RANGE,		  // blast range
			EXTRA_BOMB_DAMAGE
		);
	}

	@Override
	protected void applyEffect(Bloon b) {
		b.takeDamage(EXTRA_BOMB_DAMAGE);
	}

}
