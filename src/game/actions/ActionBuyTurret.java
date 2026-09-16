package game.actions;

import java.util.List;
import java.util.logging.Logger;

import game.Game;
import game.actions.templates.turrets.ActionBuyCanonMonkeyTurret;
import game.actions.templates.turrets.ActionBuyDartMonkeyTurret;
import game.actions.templates.turrets.ActionBuyFreezeMonkeyTurret;
import game.actions.templates.turrets.ActionBuyJunkyMonkeyTurret;
import game.actions.templates.turrets.ActionBuyNeedleMonkeyTurret;
import game.actions.templates.turrets.ActionBuySlowMonkeyTurret;
import game.actions.templates.turrets.ActionBuySniperMonkeyTurret;
import game.objects.turrets.Turret;
import utils.MyLogger;
import utils.coordsChooser.CoordsChooser;
import utils.datastructures.Tuple2;
import utils.listchooser.ListChooser;


/**
 * Action of buying a turret of unknown type
 */
public class ActionBuyTurret extends Action {

	private ListChooser<ActionBuyTurret> turretActionLC;

	protected CoordsChooser coordsChooser;

	protected Class<? extends Turret> turretClass;

	/**
	 * Class constructor
	 * @param turretActionListChooser List Chooser for the ActionBuyTurret list
	 * @param coordsChooser CoordsChooser to get coordinates on which to place turret
	 */
	public ActionBuyTurret(ListChooser<ActionBuyTurret> turretActionListChooser, CoordsChooser coordsChooser, Class<? extends Turret> turretClass)
	{
		super();
		this.turretActionLC = turretActionListChooser;
		this.coordsChooser = coordsChooser;
		this.turretClass = turretClass;
	}
	public ActionBuyTurret(ListChooser<ActionBuyTurret> turretActionListChooser, CoordsChooser coordsChooser)
	{ this(turretActionListChooser, coordsChooser, null); }

	protected Tuple2<Integer, Integer> getPlacementCoords()
	{
		boolean valid = false;

		Tuple2<Integer, Integer> xy = null;

		do {
			xy = this.coordsChooser.choose(
				"Which coordinates to place the new turret on ?",
				new Tuple2<Integer, Integer>(
					Game.instance().getBoard().getWidth()-1,
					Game.instance().getBoard().getHeight()-1
				)
			);

			valid = Game.instance().getBoard().getCell(xy.first(), xy.second()).canPlace(this.turretClass);

			if(!valid)
				System.err.println("The given coordinates are on a bloon path !");
		} while(!valid);

		return xy;
	}

	@Override
	public boolean isExecutable() {
		return true;
	}

	/**
	 * Buy a turret to shop
	 * Coords must be verified and valid
	 * @param xy coordinates of the turret to buy
	 */
	protected void doBuy(Tuple2<Integer, Integer> xy)
	{
		Game.instance().getShop().buyTurret(Game.instance().getBoard(), this.turretClass, xy);
	}

	/**
	 * Prompts for x and y coordinates of the turret to buy
	 * Keep reprompting until coords allowing buying the turret are given
	 */
	@Override
	public void execAction()
	{
		Logger logger = MyLogger.getInstance();

		List<ActionBuyTurret> ABTs = List.of(
			new ActionBuyDartMonkeyTurret  (coordsChooser),
			new ActionBuySlowMonkeyTurret  (coordsChooser),
			new ActionBuyCanonMonkeyTurret (coordsChooser),
			new ActionBuyJunkyMonkeyTurret (coordsChooser),
			new ActionBuyNeedleMonkeyTurret(coordsChooser),
			new ActionBuySniperMonkeyTurret(coordsChooser),
			new ActionBuyFreezeMonkeyTurret(coordsChooser)
		);

		List<ActionBuyTurret> affordableTurrets = ABTs
			.stream()
			.filter(abt -> abt.isExecutable())
			.toList();

		ActionBuyTurret abt = this.turretActionLC.choose(
				"Quelle turret voulez-vous acheter ? ",
				affordableTurrets
		);

		if (abt == null)
		{
			logger.warning("Requested ActionBuyTurret to use is NULL ! Skipping");
			return;
		}

		abt.execAction();
	}
	@Override
	public String toString()
	{
		if (this.turretClass == null)
			return "Acheter une turret";

		String name;
		switch (this.turretClass.getSimpleName())
		{
			case "DartMonkey":
				name = "Dart Monkey";
				break;
			case "SlowMonkey":
				name = "Slow Monkey";
				break;
			case "CanonMonkey":
				name = "Canon Monkey";
				break;
			case "JunkyMonkey":
				name = "Junky Monkey";
				break;
			case "NeedleMonkey":
				name = "Needle Monkey";
				break;
			case "SniperMonkey":
				name = "Sniper Monkey";
				break;
			case "FreezeMonkey":
				name = "Freeze Monkey";
				break;
			default:
				name = this.turretClass.getSimpleName();
				break;
		}

		try
		{
			int cost = Game.instance().getShop().getTurretCost(this.turretClass);
			return name + " (" + cost + " credits)";
		}
		catch (Exception e)
		{
			return name;
		}
	}
}
