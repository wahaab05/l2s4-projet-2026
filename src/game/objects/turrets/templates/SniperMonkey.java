package game.objects.turrets.templates;

import java.util.List;

import game.cells.TurretCell;
import game.objects.projectiles.templates.VerySharpDartProjectile;
import game.objects.turrets.Turret;
import game.upgrade.IntUpgradeableAttribute;
import game.upgrade.UpgradeableAttribute;
import utils.config.ConfigReader;
import utils.datastructures.Tuple2;

/**
 * Template of turret
 */
public class SniperMonkey extends Turret
{

	public static final int SNIPER_MONKEY_COST = ConfigReader.getInt("sniper_monkey_cost");
	private static final float SNIPER_MONKEY_INITIAL_RANGE = ConfigReader.getFloat("sniper_monkey_initial_range");
	private static final int SNIPER_MONKEY_INITIAL_COOLDOWN = ConfigReader.getInt("sniper_monkey_initial_cooldown");

	/**
	 * Class constructor
	 * @param c the turretcell where this turret is placed
	 */
	public SniperMonkey(TurretCell c)
	{
			super(
					c,
					SNIPER_MONKEY_COST,
					SNIPER_MONKEY_INITIAL_RANGE,
					SNIPER_MONKEY_INITIAL_COOLDOWN,
					VerySharpDartProjectile.class
			);

			this.shootingCooldown = new IntUpgradeableAttribute(
					SNIPER_MONKEY_INITIAL_COOLDOWN,
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
				new Tuple2<String, UpgradeableAttribute<?>>("SHOOTING_COOLDOWN", this.shootingCooldown)
		);
	}
}
