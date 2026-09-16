package game.objects.turrets.templates;

import java.util.List;

import game.cells.TurretCell;
import game.objects.projectiles.templates.SharpDartProjectile;
import game.objects.turrets.Turret;
import game.upgrade.FloatUpgradeableAttribute;
import game.upgrade.IntUpgradeableAttribute;
import game.upgrade.UpgradeableAttribute;
import utils.config.ConfigReader;
import utils.datastructures.Tuple2;

/**
 * Template of turret
 */
public class JunkyMonkey extends Turret
{
	// TODO: Change wrong turret name
	public static final int JUNKY_MONKEY_COST = ConfigReader.getInt("junky_monkey_cost");
	private static final float JUNKY_MONKEY_INITIAL_RANGE = ConfigReader.getFloat("junky_monkey_initial_range");
	private static final int JUNKY_MONKEY_INITIAL_COOLDOWN = ConfigReader.getInt("junky_monkey_initial_cooldown");

	/**
	 * Class constructor
	 * @param c the turretcell where this turret is placed
	 */
	public JunkyMonkey(TurretCell c)
	{
			super(
					c,
					JUNKY_MONKEY_COST,
					JUNKY_MONKEY_INITIAL_RANGE,
					JUNKY_MONKEY_INITIAL_COOLDOWN,
					SharpDartProjectile.class
			);

			this.range = new FloatUpgradeableAttribute(
					JUNKY_MONKEY_INITIAL_RANGE,
					1.50f,
					400,
					1f,
					1
			);

			this.shootingCooldown = new IntUpgradeableAttribute(
					JUNKY_MONKEY_INITIAL_COOLDOWN,
					1f / 3f,
					1000,
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
