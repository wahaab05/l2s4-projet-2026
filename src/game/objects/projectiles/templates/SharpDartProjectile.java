package game.objects.projectiles.templates;

import game.objects.bloons.Bloon;
import game.objects.projectiles.Projectile;
import game.objects.turrets.Targetable;
import utils.config.ConfigReader;
import utils.plane2d.Vector2f;

/**
 * SharpDartProjectile - stronger dart
 * Deals 2 damage to a single bloon
 */
public class SharpDartProjectile extends Projectile {

	private static final int SHARP_DART_DAMAGE = ConfigReader.getInt("sharp_dart_projectile_damage");
	private static final float SHARP_DART_SPEED = ConfigReader.getFloat("sharp_dart_projectile_speed");



	/**
	 * Create a new SharpDartProjectile instance
	 * @param coords Spawning coordinates
	 * @param target Bloon to target
	 */
	public SharpDartProjectile(Vector2f coords, Targetable target) {
		super("Sharp Dart", coords, target, SHARP_DART_SPEED, SHARP_DART_DAMAGE);
	}

	@Override
	protected void applyEffect(Bloon b) {
		b.takeDamage(SHARP_DART_DAMAGE);
	}
}
