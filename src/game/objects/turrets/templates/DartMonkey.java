package game.objects.turrets.templates;

import java.util.List;

import game.cells.TurretCell;
import game.objects.projectiles.templates.DartProjectile;
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
public class DartMonkey extends Turret
{

	public static final int DART_MONKEY_COST = ConfigReader.getInt("dart_monkey_cost");
	private static final float DART_MONKEY_INITIAL_RANGE = ConfigReader.getFloat("dart_monkey_initial_range");
	private static final int DART_MONKEY_INITIAL_COOLDOWN = ConfigReader.getInt("dart_monkey_initial_cooldown");

	/**
	 * Class constructor
	 * @param c the turretcell where this turret is placed
	 */
	public DartMonkey(TurretCell c)
	{
			super(
					c,
					DART_MONKEY_COST,
					DART_MONKEY_INITIAL_RANGE,
					DART_MONKEY_INITIAL_COOLDOWN,
					DartProjectile.class
			);

			this.range = new FloatUpgradeableAttribute(
					DART_MONKEY_INITIAL_RANGE,
					1.25f,
					100,
					1f,
					1
			);

			this.shootingCooldown = new IntUpgradeableAttribute(
					DART_MONKEY_INITIAL_COOLDOWN,
					0.75f,
					150,
					1f,
					1
			);

			this.projectileType = new ProjectileUpgradeableAttribute(
					DartProjectile.class,
					250,
					1f,
					1
			);
	}
}
