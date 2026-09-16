package game.objects.turrets.templates;

import java.util.List;

import game.cells.TurretCell;
import game.objects.projectiles.templates.NeedleProjectile;
import game.objects.turrets.Turret;
import game.upgrade.FloatUpgradeableAttribute;
import game.upgrade.IntUpgradeableAttribute;
import game.upgrade.UpgradeableAttribute;
import utils.config.ConfigReader;
import utils.datastructures.Tuple2;

/**
 * Template of turret
 */
public class NeedleMonkey extends Turret
{

	public static final int NEEDLE_MONKEY_COST = ConfigReader.getInt("needle_monkey_cost");
	private static final float NEEDLE_MONKEY_INITIAL_RANGE = ConfigReader.getFloat("needle_monkey_initial_range");
	private static final int NEEDLE_MONKEY_INITIAL_COOLDOWN = ConfigReader.getInt("needle_monkey_initial_cooldown");

	/**
	 * Class constructor
	 * @param c the turretcell where this turret is placed
	 */
	public NeedleMonkey(TurretCell c)
	{
			super(
					c,
					NEEDLE_MONKEY_COST,
					NEEDLE_MONKEY_INITIAL_RANGE,
					NEEDLE_MONKEY_INITIAL_COOLDOWN,
					NeedleProjectile.class
			);

			this.range = new FloatUpgradeableAttribute(
					NEEDLE_MONKEY_INITIAL_RANGE,
					1.20f,
					150,
					1f,
					1
			);

			this.shootingCooldown = new IntUpgradeableAttribute(
					NEEDLE_MONKEY_INITIAL_COOLDOWN,
					0.75f,
					200,
					1f,
					1
			);
	}
	@Override
	public List<Tuple2<String, UpgradeableAttribute<?>>> getAllUpgradeableAttributes()
	{
		return List.of(
				new Tuple2<String, UpgradeableAttribute<?>>("RANGE", this.range),
				new Tuple2<String, UpgradeableAttribute<?>>("SHOOTING_COOLDOWN", this.shootingCooldown)
		);
	}
}
