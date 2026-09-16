package game.upgrade;

/**
 * UpgradeableAttribute containing an Integer value.
 */
public class IntUpgradeableAttribute extends UpgradeableAttribute<Integer>
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
	public IntUpgradeableAttribute(
			int initialValue,
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
	public IntUpgradeableAttribute(
			int initialValue,
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
	public IntUpgradeableAttribute(
			int initialValue,
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
		this.value = (int)(this.value * this.valueScaling);
		this.value += this.valueAddition;
	}

//                           .
//                           A       ;
//                 |   ,--,-/ \---,-/|  ,
//                _|\,'. /|      /|   `/|-.
//            \`.'    /|      ,            `;.
//           ,'\   A     A         A   A _ /| `.;
//         ,/  _              A       _  / _   /|  ;
//        /\  / \   ,  ,           A  /    /     `/|
//       /_| | _ \         ,     ,             ,/  \
//      // | |/ `.\  ,-      ,       ,   ,/ ,/      \/
//      / @| |@  / /'   \  \      ,              >  /|    ,--.
//     |\_/   \_/ /      |  |           ,  ,/        \  ./' __:..
//     |  __ __  |       |  | .--.  ,         >  >   |-'   /     `
//   ,/| /  '  \ |       |  |     \      ,           |    /
//  /  |<--.__,->|       |  | .    `.        >  >    /   (
// /_,' \\  ^  /  \     /  /   `.    >--            /^\   |
//       \\___/    \   /  /      \__'     \   \   \/   \  |
//        `.   |/          ,  ,                  /`\    \  )
//          \  '  |/    ,       V    \          /        `-\
//           `|/  '  V      V           \    \.'            \_
//            '`-.       V       V        \./'\
//                `|/-.      \ /   \ /,---`\
//                 /   `._____V_____V'
//                            '     '

	@Override
	protected void downgradeValue() {
		this.value -= this.valueAddition;
		this.value = (int)(this.value / this.valueScaling);
	}

}
