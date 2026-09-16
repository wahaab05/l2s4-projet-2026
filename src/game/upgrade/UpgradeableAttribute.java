package game.upgrade;

import java.lang.Math;

/**
 * Class designed to contain a value, which can be upgraded for a certain (scaling) cost.
 * @param <T> type of value contained in the UpgradeableAttribute instance. 'Fish' is an acceptable type
 */
public abstract class UpgradeableAttribute<T>
{
	/**
	 * Cost to upgrade from current level to next level
	 */
	protected int cost;


	protected int initialCost;

	/**
	 * Factor by which the price is multiplied to upgrade from level n to n+1
	 */
	protected float costScalingFactor;


	/**
	 * Number of upgrades having been applied to this attribute
	 */
	protected int level;

	/**
	 * Maximum number of upgrades which can be applied to this attribute
	 */
	protected int maxLevel;

	/**
	 * Current contained value.
	 */
	protected T value;

	/**
	 * Create a new upgradeable attribute.
	 *
	 * @param initialValue Initial value of this attribute.
	 * @param initialCost Cost of the first upgrade, from level 0 to 1.
	 * @param costScalingFactor Factor by which the cost will be multiplied after each upgrade. A factor of 1 will leave the cost the same.
	 * @param maxLevel Maximum number of times this attribute can be upgraded.
	 */
	public UpgradeableAttribute(T initialValue, int initialCost, float costScalingFactor, int maxLevel)
	{
		this.value = initialValue;
		this.initialCost = initialCost;
		this.cost = initialCost;
		this.costScalingFactor = costScalingFactor;
		this.level = 0;
		this.maxLevel = maxLevel;
	}




	/* ================ *\
	 *					*
	 * 		GETTERS		*
	 * 					*
	\* ================ */


	/**
	 * Get the value contained in this attribute.
	 * @return The contained value.
	 */
	public T getValue()
	{ return this.value; }


	/**
	 * Get the cost to upgrade this attribute to the next level.
	 * @return The cost.
	 */
	public int getCost()
	{ return this.cost; }


	/**
	 * Get the cost of all the applied upgrades. according to the formula of : sum(n going from 1 to level){scale_factor to the power of n}
	 * @return Total cost
	 */
	public int getTotalCost()
	{
		if(this.costScalingFactor == 1.)
			return this.initialCost * (this.level);

		return (int)
			(this.initialCost * (Math.pow(this.costScalingFactor, this.level+1) - 1) /
			this.costScalingFactor - 1);
	}


	/**
	 * Get the factor by which the price is multiplied after each upgrade.
	* @return The cost scaling factor.
	 */
	public float getCostScalingFactor()
	{ return this.costScalingFactor; }


	/**
	 * Get the number of times this attribute has been upgraded.
	 * @return Upgrade level.
	 */
	public int getLevel()
	{ return this.level; }


	/**
	 * Get the maximum number of times this attribute can be upgraded.
	 * @return Maximum upgrade level.
	 */
	public int getMaxLevel()
	{ return this.maxLevel; }


	/**
	 * Check if this attribute can be upgraded at least once more.
	 * @return True if it can safely be upgraded.
	 */
	public boolean canBeUpgraded()
	{ return this.level < this.maxLevel; }


	/**
	 * Check if this attribute can be downgraded at least once.
	 * @return True if it can be safely downgraded.
	 */
	public boolean canBeDowngraded()
	{ return this.level > 0; }


	/* ================ *\
	 *					*
	 * 		METHODS		*
	 * 					*
	\* ================ */


	/**
	 * Upgrade the contained value of this attribute.
	 */
	protected abstract void upgradeValue();

	/**
	 * Downgrade the contained value of this attribute.
	 */
	protected abstract void downgradeValue();


	/**
	 * Upgrade this attribute.
	 *
	 * @throws IllegalStateException if this attribute cannot be upgraded.
	 */
	public void upgrade() throws IllegalStateException
	{
		if(this.level == this.maxLevel)
			throw new IllegalStateException("This attribute \""+this+"\" has reached its maximum number of upgrades.");

		this.upgradeValue();

		this.level++;
		this.cost *= this.costScalingFactor;
	}


	/**
	 * Downgrade this attribute.
	 *
	 * @throws IllegalStateException if this attribute cannot be downgraded.
	 */
	public void downgrade() throws IllegalStateException
	{
		if(this.level == 0)
			throw new IllegalStateException("This attribute \""+this+"\" is at its lowest level, it cannot be downgraded.");

		this.downgradeValue();

		this.level--;
		this.cost /= this.costScalingFactor;
	}


	/**
	 * Get the string representation of this UpgradeableAttribute instance.
	 *
	 * @return The string representation of this instance.
	 */
	public String toString()
	{
		return String.format("[ %s ; value %s ; lvl %d ; cost to next lvl %d ]", this.getClass(), this.value, this.level, this.cost);
	}
}
