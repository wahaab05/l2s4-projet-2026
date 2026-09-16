package game.objects.turrets.templates;

import java.util.List;

import game.Game;
import game.cells.TurretCell;
import game.objects.projectiles.templates.FreezeProjectile;
import game.objects.turrets.Targetable;
import game.objects.turrets.Turret;
import game.upgrade.UpgradeableAttribute;
import utils.config.ConfigReader;
import utils.datastructures.Tuple2;

/**
 * Turret shooting freeze projectiles.
 */
public class FreezeMonkey extends Turret
{


	public static final int FREEZE_MONKEY_COST = ConfigReader.getInt("freeze_monkey_cost");
	private static final float FREEZE_MONKEY_INITIAL_RANGE = ConfigReader.getFloat("freeze_monkey_initial_range");
	private static final int FREEZE_MONKEY_INITIAL_COOLDOWN = ConfigReader.getInt("freeze_monkey_initial_cooldown");


	/**
	 * Creates a new FreezeMonkey on the given turret cell.
	 *
	 * @param c turret cell where the monkey is placed
	 */
	public FreezeMonkey(TurretCell c)
	{
		super(
			c,
			FREEZE_MONKEY_COST,
			FREEZE_MONKEY_INITIAL_RANGE,
			FREEZE_MONKEY_INITIAL_COOLDOWN,
			FreezeProjectile.class
		);
	}
		/**
	 * Returns the list of upgradeable attributes of this turret.
	 * For FreezeMonkey, there are no upgrades, so the list is empty.
	 *
	 * @return an empty list because FreezeMonkey cannot be upgraded
	 */
	@Override
	public List<Tuple2<String, UpgradeableAttribute<?>>> getAllUpgradeableAttributes()
	{
		return List.of();
	}

	@Override
	public boolean canShoot() {

		boolean hasBloonsInRange =
			Game.instance()
				.getBoard()
				.getBloonsInRange(this.getRange(), this.coords.x(), this.coords.y())
				.size() > 0;

		return super.canShoot() && hasBloonsInRange;
	}

	@Override
	public List<Targetable> chooseTarget() {
		return List.of(this);
	}
}
