package game.actions.templates;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import game.Game;
import game.actions.Action;
import game.objects.turrets.Turret;
import game.upgrade.UpgradeableAttribute;
import utils.MyLogger;
import utils.datastructures.Tuple2;
import utils.listchooser.ListChooser;

/**
 * Action of downgrading a turret
 */
public class ActionDowngradeTurret extends Action {

	private ListChooser<Turret> turretListChooser;
	private ListChooser<String> downgradeListChooser;

	/**
	 * Class constructor
	 * @param turretListChooser the list chooser to use to prompt for turret choice
	 * @param downgradeListChooser the list chooser to use to prompt for what upgrade to remove
	 */
	public ActionDowngradeTurret(ListChooser<Turret> turretListChooser, ListChooser<String> downgradeListChooser)
	{
		super();
		this.turretListChooser = turretListChooser;
		this.downgradeListChooser = downgradeListChooser;
	}

	@Override
	public boolean isExecutable()
	{
		return Game.instance().getBoard().getTurrets().stream().anyMatch(t -> t.canBeDowngraded());
	}

	@Override
	public void execAction()
	{
		Logger logger = MyLogger.getInstance();
		List<Turret> turrets = Game.instance().getBoard().getTurrets().stream().filter(t -> t.canBeDowngraded()).toList();
		Turret turret = turretListChooser.choose("Quelle turret voulez-vous downgrader ? ", turrets);
		logger.fine("User requested to downgrade turret " + turret);
		if (turret == null)
			return;


		List<Tuple2<String, UpgradeableAttribute<?>>> downgradables = turret.getAllUpgradeableAttributesWhichCanBeDowngradedAtLeastOnceMore();
		// create a list of all downgrades names
		List<String> downgradablesNames = new ArrayList<>();
		downgradables.forEach(u -> {
			downgradablesNames.add(u.first());
		});
		String chosenDowngradeName = downgradeListChooser.choose("Quelle upgrade voulez-vous supprimer de cette turret ? ", downgradablesNames);
		logger.fine("User requested to downgrade upgrade " + chosenDowngradeName);
		if (chosenDowngradeName == null)
			return;
		// get upgradeableattribute from name (chosen by user)                                      and downgrade it
		UpgradeableAttribute<?> chosenDowngrade = downgradables.stream()
				.filter(u -> u.first().equals(chosenDowngradeName))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Downgrade not found: " + chosenDowngradeName))
				.second();


		Game.instance().getShop().sellUpgrade(chosenDowngrade);
		logger.info("Downgrading attribute " + chosenDowngradeName + " for turret " + turret);
	}
}
