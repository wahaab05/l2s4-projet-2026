package game.upgrade;

/**
 * UpgradeableAttribute containing a Float value.
 */
public class FloatUpgradeableAttribute extends UpgradeableAttribute<Float>
{
	/**
	 * Factor by which the value is multiplied at each upgrade.
	 */
	protected float valueScaling;

	/**
	 * Flat value increase at each upgrade.
	 */
	protected int valueAddition;



	/* ====================	*\
	 * 						*
	 *		CONSTRUCTORS	*
	 * 						*
	\* ====================	*/

	/**
	 * Create a new UpgradeableAttribute containing a number value.
	 * @param initialValue Initial value of this attribute.
	 * @param valueScaling Factor by which the value is multiplied at each upgrade.
	 * @param valueAddition Flat number added to the value each upgrade.
	 * @param initialCost Cost for the fist upgrade, from level 0 to 1.
	 * @param costScaling Factor by which the price is multiplied after each upgrade.
	 * @param maxLevel Maximum number of times this attribute can be upgraded.
	 */
	public FloatUpgradeableAttribute(
			float initialValue,
			float valueScaling,
			int valueAddition,
			int initialCost,
			float costScaling,
			int maxLevel
	){
		super(initialValue, initialCost, costScaling, maxLevel);
		this.valueScaling = valueScaling;
		this.valueAddition = valueAddition;
	}


	/**
	 * Create a new UpgradeableAttribute containing a number value.
	 * @param initialValue Initial value of this attribute.
	 * @param valueAddition Flat number added to the value each upgrade.
	 * @param initialCost Cost for the fist upgrade, from level 0 to 1.
	 * @param costScaling Factor by which the price is multiplied after each upgrade.
	 * @param maxLevel Maximum number of times this attribute can be upgraded.
	 */
	public FloatUpgradeableAttribute(
			float initialValue,
			int valueAddition,
			int initialCost,
			float costScaling,
			int maxLevel
	)
	{
		this(
				initialValue,
				1.f,
				valueAddition,
				initialCost,
				costScaling,
				maxLevel
		);
	}


	/**
	 * Create a new UpgradeableAttribute containing a number value.
	 * @param initialValue Initial value of this attribute.
	 * @param valueScaling Factor by which the value is multiplied at each upgrade.
	 * @param initialCost Cost for the fist upgrade, from level 0 to 1.
	 * @param costScaling Factor by which the price is multiplied after each upgrade.
	 * @param maxLevel Maximum number of times this attribute can be upgraded.
	 */
	public FloatUpgradeableAttribute(
			float initialValue,
			float valueScaling,
			int initialCost,
			float costScaling,
			int maxLevel
	)
	{
		this(
				initialValue,
				valueScaling,
				0,
				initialCost,
				costScaling,
				maxLevel
		);
	}


	@Override
	protected void upgradeValue() {
		this.value *= this.valueScaling;
		this.value += this.valueAddition;
	}


	@Override
	protected void downgradeValue() {
		this.value -= this.valueAddition;
		this.value /= this.valueScaling;
	}

}
