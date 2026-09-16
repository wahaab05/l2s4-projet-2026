package game.upgrade;

import java.util.List;

import game.objects.projectiles.Projectile;
import game.objects.projectiles.templates.BombProjectile;
import game.objects.projectiles.templates.DartProjectile;
import game.objects.projectiles.templates.ExtraBombProjectile;
import game.objects.projectiles.templates.FreezeProjectile;
import game.objects.projectiles.templates.NeedleProjectile;
import game.objects.projectiles.templates.SharpDartProjectile;
import game.objects.projectiles.templates.SlowProjectile;
import game.objects.projectiles.templates.VerySharpDartProjectile;

/**
 * UpgradeableAttribute containing a Projectile class.
 *
 * An upgradeable projectile follows a pre-determined upgrade path.
 * Ex: DartProject -> SharpDartProjectile -> VerySharpDartProjectile.
 */
public class ProjectileUpgradeableAttribute extends UpgradeableAttribute<Class<? extends Projectile>>
{
	/**
	 * List of projectile upgrades, ordered from lowest level to highest.
	 */
	protected List<List<Class<? extends Projectile>>> upgrades =
	List.of(
			List.of(
				DartProjectile.class,
				SharpDartProjectile.class,
				VerySharpDartProjectile.class
			),
			List.of(
				BombProjectile.class,
				ExtraBombProjectile.class
			),
			List.of(
				NeedleProjectile.class
			),
				List.of(
					SlowProjectile.class
				),
				List.of(
					FreezeProjectile.class
				)
	);

	/**
	 * Current upgrade path the projectile is on.
	 * In other words, {@code upgrades.get(upgradePath)} gives the list the current projectile upgrade is from.
	 */
	protected int upgradePath;



	/**
	 * Create a new Projectile UpgradeableAttribute instance.
	 *
	 * @param initialValue The initial projectile class. Must be the first class in one of the possible upgrade paths.
	 * @param initialCost Cost to upgrade from level 0 to 1.
	 * @param costScalingFactor Factor by which the price will be multiplied each upgrade.
	 * @param maxLevel Maximum number of upgrades possible. Must be less than the size of chosen upgrade path.
	 *
	 * @throws IllegalArgumentException If the given {@code initialValue} argument is not an element of an upgrade path.
	 */
	public ProjectileUpgradeableAttribute(
		Class<? extends Projectile> initialValue,
		int initialCost,
		float costScalingFactor,
		int maxLevel
	) throws IllegalArgumentException
	{
		super(initialValue, initialCost, costScalingFactor, maxLevel);

		boolean found = false;

		// Check which path the initialValue is from
		for(upgradePath = 0; !found && upgradePath < upgrades.size(); upgradePath++)
		{
			for(level = 0; !found && level < upgrades.get(upgradePath).size(); level++)
			{
				if(initialValue == upgrades.get(upgradePath).get(level))
					found = true;
			}
		}

		if(!found)
		{
			throw new IllegalArgumentException(
					"The provided `initialValue` projectile class \""+
					initialValue +
					"\" is not found in upgrade paths."
			);
		}

		level--;
		upgradePath--;

		if(maxLevel >= upgrades.get(upgradePath).size())
		{
			throw new IllegalArgumentException(
					"`maxLevel` cannot be more than the number of projectile upgrades possible for the given initialValue"
			);
		}
	}


	/**
	 * Create a new Projectile UpgradeableAttribute instance.
	 *
	 * @param initialValue The initial projectile class. Must be the first class in one of the possible upgrade paths.
	 * @param initialCost Cost to upgrade from level 0 to 1.
	 * @param costScalingFactor Factor by which the price will be multiplied each upgrade.
	 *
	 * @throws IllegalArgumentException If the given {@code initialValue} argument is not the first element of an upgrade path.
	 */
	public ProjectileUpgradeableAttribute(
		Class<? extends Projectile> initialValue,
		int initialCost,
		float costScalingFactor
	) throws IllegalArgumentException
	{
		this(initialValue, initialCost, costScalingFactor, -1);
		this.maxLevel = this.upgrades.get(this.upgradePath).size()-1;
	}


	@Override
	protected void upgradeValue() {
		value = upgrades.get(upgradePath).get(level+1);
	}

	@Override
	protected void downgradeValue() {
		value = upgrades.get(upgradePath).get(level-1);
	}

}
