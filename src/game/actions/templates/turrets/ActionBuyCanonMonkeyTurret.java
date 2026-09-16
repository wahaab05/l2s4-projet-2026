package game.actions.templates.turrets;

import game.Game;
import game.actions.ActionBuyTurret;
import game.objects.turrets.templates.CanonMonkey;
import utils.coordsChooser.CoordsChooser;
import utils.datastructures.Tuple2;

public class ActionBuyCanonMonkeyTurret extends ActionBuyTurret

{

		/**
			 * Creates a new buy action for a {@link CanonMonkey}.
			 *
			 * @param coordsChooser object used to choose the placement coordinates
			 * for the turret
			 */
	public ActionBuyCanonMonkeyTurret(CoordsChooser coordsChooser)
	{
		super(null, coordsChooser, CanonMonkey.class);
	}
	/**
		 * Checks whether this action can be executed.
		 *
		 * The action is executable only if the shop has enough credits
		 * to buy a {@link CanonMonkey}.
		 *
		 * @return {@code true} if the player has enough credits,
		 * {@code false} otherwise
		 */

	@Override
	public boolean isExecutable()
	{
		return Game.instance().getShop().getCredits() >= CanonMonkey.CANON_MONKEY_COST;
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
