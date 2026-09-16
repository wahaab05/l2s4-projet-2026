package game.actions.templates;

import java.util.List;

import game.Game;
import game.actions.Action;
import game.objects.turrets.Turret;
import utils.MyLogger;
import utils.listchooser.ListChooser;

/**
 * Action of selling a turret
 */
public class ActionSellTurret extends Action {

	/** list chooser used to prompt for what turret to sell */
	private ListChooser<Turret> listChooser;

	/**
	 * Class constructor
	 * @param listChooser instance of the list chooser used to promp for what turret to sell
	 */
	public ActionSellTurret(ListChooser<Turret> listChooser)
	{
		super();
		this.listChooser = listChooser;
	}

	@Override
	public boolean isExecutable()
	{
		return Game.instance().getBoard().getTurrets().size() > 0;
	}

	@Override
	public void execAction()
	{
		// TODO to test

		List<Turret> turrets = List.copyOf(Game.instance().getBoard().getTurrets());


		// prompt user (listchooser)
		Turret turret = listChooser.choose("What turret do you want to sell?", turrets);
		if(turret == null)
			return;

		MyLogger.getInstance().info("Player sold turret " + turret.toString());
		Game.instance().getShop().sellTurret(turret);
	}
}

//      |\    o
//     |  \    o
// |\ /    .\ o
// | |       (
// |/ \     /
//     |  /
//      |/
