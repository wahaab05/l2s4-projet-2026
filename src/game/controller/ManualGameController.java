package game.controller;

import java.util.ArrayList;
import java.util.List;

import game.Game;
import game.actions.ActionBuyTurret;
import game.actions.templates.ActionDisplay;
import game.actions.templates.ActionDowngradeTurret;
import game.actions.templates.ActionSellTurret;
import game.actions.templates.ActionUpgradeTurret;
import utils.coordsChooser.InteractiveCoordsChooser;
import utils.listchooser.InteractiveListChooser;

/**
 * Manual controller used before each wave.
 *
 * The player stays in a preparation menu and can perform as many actions as he
 * wants before launching the next wave.
 */
public class ManualGameController implements GameController {

    private final InteractiveListChooser<String> menuChooser;
    private final ActionBuyTurret buyAction;
    private final ActionUpgradeTurret upgradeAction;
    private final ActionDowngradeTurret downgradeAction;
    private final ActionSellTurret sellAction;
    private final ActionDisplay displayAction;

    /**
     * Creates a manual controller using interactive choosers.
     */
    public ManualGameController() {
        this.menuChooser = new InteractiveListChooser<>();
        this.buyAction = new ActionBuyTurret(new InteractiveListChooser<>(), new InteractiveCoordsChooser());
        this.upgradeAction = new ActionUpgradeTurret(new InteractiveListChooser<>(), new InteractiveListChooser<>());
        this.downgradeAction = new ActionDowngradeTurret(new InteractiveListChooser<>(), new InteractiveListChooser<>());
        this.sellAction = new ActionSellTurret(new InteractiveListChooser<>());
        this.displayAction = new ActionDisplay();
    }

    @Override
    public void playPreparationPhase() {
        boolean launchWave = false;

        GameController.printPreparationHeader();
        GameController.printNextWavePreview();
        GameController.printTurretStates();
        this.displayAction.execAction();

        while (!launchWave && !Game.instance().hasGameEnded()) {
            List<String> choices = this.buildChoices();
            String choice = this.menuChooser.choose("Choose an action before the next wave:", choices);

            if (choice == null) {
                continue;
            }

            switch (choice) {
                case "Buy a turret":
                    if (GameController.runActionIfPossible(this.buyAction, "No turret can be bought right now."))
                        this.displayAction.execAction();
                    break;

                case "Upgrade a turret":
                    if (GameController.runActionIfPossible(this.upgradeAction, "No affordable upgrade is currently available."))
                        this.displayAction.execAction();
                    break;

                case "Downgrade a turret":
                    if (GameController.runActionIfPossible(this.downgradeAction, "No turret can be downgraded right now."))
                        this.displayAction.execAction();
                    break;

                case "Sell a turret":
                    if (GameController.runActionIfPossible(this.sellAction, "No turret can be sold right now."))
                        this.displayAction.execAction();
                    break;

                case "Display the board again":
                    this.displayAction.execAction();
                    break;

                case "Show current status":
                    GameController.printPreparationHeader();
                    GameController.printNextWavePreview();
                    GameController.printTurretStates();
                    break;

                case "Launch next wave":
                    launchWave = true;
                    break;

                default:
                    throw new IllegalStateException("Unexpected menu choice: " + choice);
            }
        }
    }

    /**
     * Builds the list of currently relevant menu entries.
     *
     * @return ordered list of actions shown to the player
     */
    private List<String> buildChoices() {
        List<String> choices = new ArrayList<>();
        choices.add("Show current status");
        choices.add("Display the board again");
        if (this.canBuyAnyTurret())
            choices.add("Buy a turret");

        if (this.upgradeAction.isExecutable())
            choices.add("Upgrade a turret");
        if (this.downgradeAction.isExecutable())
            choices.add("Downgrade a turret");
        if (this.sellAction.isExecutable())
            choices.add("Sell a turret");

        choices.add("Launch next wave");
        return choices;
    }

    /**
     * Checks whether at least one turret from the shop catalog is affordable.
     *
     * @return true if the player can currently buy at least one turret
     */
    private boolean canBuyAnyTurret() {
        return Game.instance().getShop().getCatalog().stream()
            .anyMatch(turretClass -> Game.instance().getShop().canBuy(turretClass));
    }
}