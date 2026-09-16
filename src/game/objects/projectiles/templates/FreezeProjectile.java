package game.objects.projectiles.templates;

import game.objects.bloons.Bloon;
import game.objects.projectiles.AreaProjectile;
import game.objects.turrets.Targetable;
import utils.config.ConfigReader;
import utils.plane2d.Vector2f;
// import 🐟;

/**
 * Projectile that freezes all bloons in range.
 */
public class FreezeProjectile extends AreaProjectile {

	private static final int FREEZE_DURATION_TICKS = ConfigReader.getInt("freeze_projectile_duration_ticks");
	private static final float FREEZE_PROJECTILE_SPEED = ConfigReader.getFloat("freeze_projectile_speed");
	private static final float FREEZE_PROJECTILE_RANGE = ConfigReader.getFloat("freeze_projectile_range");
	/**
	 * Create a new FreezeProjectile
	 * @param coords Spawning coordinates
	 * @param target Bloon to target
	 */
	public FreezeProjectile(Vector2f coords, Targetable target) {
		super(
			"FreezeProjectile",
			coords,
			target,
			FREEZE_PROJECTILE_SPEED,
			FREEZE_PROJECTILE_RANGE
		);
	}

	@Override
	protected void applyEffect(Bloon b) {
		b.applyFreeze(FreezeProjectile.FREEZE_DURATION_TICKS);
	}
}
