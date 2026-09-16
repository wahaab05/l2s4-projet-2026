package game.objects.turrets.templates;

import java.util.List;

import game.cells.TurretCell;
import game.objects.bloons.Bloon;
import game.objects.projectiles.Projectile;
import game.objects.projectiles.templates.SlowProjectile;
import game.objects.turrets.Targetable;
import game.objects.turrets.Turret;
import game.upgrade.UpgradeableAttribute;
import utils.config.ConfigReader;
import utils.datastructures.Tuple2;

/**
 * Turret shooting slow projectiles.
 */
public class SlowMonkey extends Turret
{

	public static final int SLOW_MONKEY_COST = ConfigReader.getInt("slow_monkey_cost");
	private static final float SLOW_MONKEY_INITIAL_RANGE = ConfigReader.getFloat("slow_monkey_initial_range");
	private static final int SLOW_MONKEY_INITIAL_COOLDOWN = ConfigReader.getInt("slow_monkey_initial_cooldown");
	private static final float SLOW_FACTOR = ConfigReader.getFloat("slow_projectile_factor");
	private static final int SLOW_DURATION_TICKS = ConfigReader.getInt("slow_projectile_duration_ticks");

	
	/**
	 * Creates a new SlowMonkey on the given turret cell
	 *
	 * @param c turret cell where the monkey is placed
	 */
	public SlowMonkey(TurretCell c)
	{
		super(
			c,
			SLOW_MONKEY_COST,
			SLOW_MONKEY_INITIAL_RANGE,
			SLOW_MONKEY_INITIAL_COOLDOWN,
			SlowProjectile.class
		);
	}
	/**
	 * Returns the list of upgradeable attributes of this turret.
	 * For SlowMonkey, there are no upgrades, so the list is empty.
	 *
	 * @return an empty list because SlowMonkey cannot be upgraded
	 */
	@Override
	public List<Tuple2<String, UpgradeableAttribute<?>>> getAllUpgradeableAttributes()
	{
		return List.of();
	}

	/**
	 * Applies the slow effect immediately to the selected targets.
	 * This turret does not create a moving projectile.
	 * It directly slows bloons that are in range.
	 *
	 * @return an empty list because no projectile is created
	 */
	@Override
	public List<Projectile> shoot()
	{
		List<Targetable> targets = this.chooseTarget();

		if(targets.isEmpty())
			return List.of();

		this.nbTicksSinceLastShot = 0;

		for(Targetable target : targets)
		{
			if(target instanceof Bloon bloon)
				bloon.applySlow(SLOW_FACTOR, SLOW_DURATION_TICKS);
		}

		return List.of();
	}
}
