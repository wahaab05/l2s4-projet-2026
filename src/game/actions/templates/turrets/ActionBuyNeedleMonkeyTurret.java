package game.actions.templates.turrets;

import game.Game;
import game.actions.ActionBuyTurret;
import game.objects.turrets.templates.NeedleMonkey;
import utils.coordsChooser.CoordsChooser;
import utils.datastructures.Tuple2;

public class ActionBuyNeedleMonkeyTurret extends ActionBuyTurret

{
	/**
	 * Creates a new buy action for a {@link NeedleMonkey}.
	 *
	 * @param coordsChooser object used to choose the placement coordinates
	 * for the turret
	 */

	public ActionBuyNeedleMonkeyTurret(CoordsChooser coordsChooser)
	{
		super(null, coordsChooser, NeedleMonkey.class);
	}


		/**
	 * Checks whether this action can be executed.
	 *
	 * The action is executable only if the shop has enough credits
	 * to buy a {@link NeedleMonkey}.
	 *
	 * @return {@code true} if the player has enough credits,
	 * {@code false} otherwise
	 */

	@Override
	public boolean isExecutable()
	{
		return Game.instance().getShop().getCredits() >= NeedleMonkey.NEEDLE_MONKEY_COST;
	}

	/**
	 * Executes the buy action.
	 *
	 * This method gets placement coordinates from the chooser
	 * and then buys and places the turret at that position.
	 */

	@Override
	public void execAction() {
		Tuple2<Integer, Integer> xy = this.getPlacementCoords();
		this.doBuy(xy);
	}
}
