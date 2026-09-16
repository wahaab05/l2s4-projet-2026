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
 * Action of upgrading a turret
 */
public class ActionUpgradeTurret extends Action {

	private ListChooser<Turret> turretListChooser;
	private ListChooser<String> upgradeListChooser;

	/**
	 * Class constructor
	 * @param turretListChooser the list chooser to use to prompt for turret choice
	 * @param upgradeListChooser the list chooser to use to prompt for what upgrade to apply
	 */
	public ActionUpgradeTurret(ListChooser<Turret> turretListChooser, ListChooser<String> upgradeListChooser)
	{
		super();
		this.turretListChooser = turretListChooser;
		this.upgradeListChooser = upgradeListChooser;
	}

	@Override
	public boolean isExecutable()
	{
		return Game.instance().getBoard().getTurrets().stream()
    .anyMatch(t -> t.getAllUpgradeableAttributesWhichCanBeUpgradedAtLeastOnceMore()
        .stream()
        .anyMatch(u -> Game.instance().getShop().canUpgrade(u.second())));
	}

	@Override
	public void execAction()
	{
		Logger logger = MyLogger.getInstance();

		List<Turret> turrets = Game.instance()
    .getBoard()
    .getTurrets()
    .stream()
    .filter(t -> t.getAllUpgradeableAttributesWhichCanBeUpgradedAtLeastOnceMore()
    .stream()
    .anyMatch(u -> Game.instance().getShop().canUpgrade(u.second())))
    .toList();

		Turret turret = turretListChooser.choose("Quelle turret voulez-vous upgrader ? ", turrets);
		logger.fine("User requested to upgrade turret " + turret);

		if (turret == null)
		{
			logger.warning("Requested turret to upgrade is NULL ! Skipping");
			return;
		}

		List<Tuple2<String, UpgradeableAttribute<?>>> upgradables = turret
    .getAllUpgradeableAttributesWhichCanBeUpgradedAtLeastOnceMore()
    .stream()
    .filter(u -> Game.instance().getShop().canUpgrade(u.second()))
    .toList();

		if (upgradables.isEmpty())
		{
				logger.warning("No affordable upgrade available for turret " + turret);
				return;
		}

		// create a list of all upgrades names
		List<String> upgradablesNames = new ArrayList<>();
		upgradables.forEach(u -> {
			upgradablesNames.add(u.first());
		});


		String chosenUpgradeName = upgradeListChooser.choose("Quelle upgrade voulez-vous appliquer à cette turret ? ", upgradablesNames);
		logger.fine("User requested to upgrade with upgrade " + chosenUpgradeName);

		if (chosenUpgradeName == null)
		{
			logger.warning("Requested turret upgrade name is NULL ! Skipping");
			return;
		}

		UpgradeableAttribute<?> chosenUpgrade = upgradables.stream()
				.filter(u -> u.first().equals(chosenUpgradeName))
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("Upgrade not found: " + chosenUpgradeName))
				.second();

		Game.instance().getShop().buyUpgrade(chosenUpgrade);
		logger.info("Upgrading attribute " + chosenUpgradeName + " for turret " + turret);
	}
}
