package game.objects.turrets.templates;

import java.util.List;

import game.cells.TurretCell;
import game.objects.projectiles.templates.BombProjectile;
import game.objects.turrets.Turret;
import game.upgrade.FloatUpgradeableAttribute;
import game.upgrade.IntUpgradeableAttribute;
import game.upgrade.ProjectileUpgradeableAttribute;
import game.upgrade.UpgradeableAttribute;
import utils.config.ConfigReader;
import utils.datastructures.Tuple2;

/**
 * Template of turret
 */
public class CanonMonkey extends Turret
{
	public static final int CANON_MONKEY_COST = ConfigReader.getInt("canon_monkey_cost");
	private static final float CANON_MONKEY_INITIAL_RANGE = ConfigReader.getFloat("canon_monkey_initial_range");
	private static final int CANON_MONKEY_INITIAL_COOLDOWN = ConfigReader.getInt("canon_monkey_initial_cooldown");



	/**
	 * Class constructor
	 * @param c the turretcell where this turret is placed
	 */
	public CanonMonkey(TurretCell c)
	{
			super(
					c,
					CANON_MONKEY_COST,
					CANON_MONKEY_INITIAL_RANGE,
					CANON_MONKEY_INITIAL_COOLDOWN,
					BombProjectile.class
			);

			this.range = new FloatUpgradeableAttribute(
					CANON_MONKEY_INITIAL_RANGE,
					1.50f,
					250,
					1f,
					1
			);

			this.shootingCooldown = new IntUpgradeableAttribute(
					CANON_MONKEY_INITIAL_COOLDOWN,
					0.75f,
					300,
					1f,
					1
			);

			this.projectileType = new ProjectileUpgradeableAttribute(
					BombProjectile.class,
					400,
					1f,
					1
			);
	}
}
