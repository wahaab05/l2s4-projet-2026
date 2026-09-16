package game.objects.projectiles.templates;

import game.objects.bloons.Bloon;
import game.objects.projectiles.Projectile;
import game.objects.turrets.Targetable;
import utils.config.ConfigReader;
import utils.plane2d.Vector2f;

/**
 * VerySharpDartProjectile - elite dart
 * Deals 4 damage to a single bloon
 */
public class VerySharpDartProjectile extends Projectile {

	private static final int VERY_SHARP_DART_DAMAGE = ConfigReader.getInt("very_sharp_dart_projectile_damage");
	private static final float VERY_SHARP_DART_SPEED = ConfigReader.getFloat("very_sharp_dart_projectile_speed");
	
		/**
	 * Create a new VerySharpDartProjectile instance
	 * @param coords Spawning coordinates
	 * @param target Bloon to target
	 */
	public VerySharpDartProjectile(Vector2f coords, Targetable target) {
		super(
			"Very Sharp Dart",
			coords,
			target,
			VERY_SHARP_DART_SPEED,
			VERY_SHARP_DART_DAMAGE
		);
	}

	@Override
	protected void applyEffect(Bloon b) {
		b.takeDamage(VERY_SHARP_DART_DAMAGE);
	}
}
