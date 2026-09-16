package game.objects.projectiles.templates;

import game.objects.bloons.Bloon;
import game.objects.projectiles.Projectile;
import game.objects.turrets.Targetable;
import utils.config.ConfigReader;
import utils.plane2d.Vector2f;

/**
 * NeedleProjectile - used by needle turret
 * Deals 1 damage to a single bloon
 */
public class NeedleProjectile extends Projectile {

	private static final int NEEDLE_DAMAGE = ConfigReader.getInt("needle_projectile_damage");
	private static final float NEEDLE_SPEED = ConfigReader.getFloat("needle_projectile_speed");

	/**
	 * Create a new NeedleProjectile
	 * @param coords Spawning coordinates of the projectile
	 * @param target Bloon to target
	 */
	public NeedleProjectile(Vector2f coords, Targetable target) {
		super("Needle", coords, target, NEEDLE_SPEED, NEEDLE_DAMAGE);
	}


	@Override
	protected void applyEffect(Bloon b) {
		b.takeDamage(NEEDLE_DAMAGE);
	}
}
