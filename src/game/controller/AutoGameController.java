package game.controller;

import java.util.Random;

import game.Game;
import game.actions.ActionBuyTurret;
import game.actions.templates.ActionDisplay;
import game.actions.templates.ActionDowngradeTurret;
import game.actions.templates.ActionSellTurret;
import game.actions.templates.ActionUpgradeTurret;
import utils.coordsChooser.RandomCoordsChooser;
import utils.listchooser.RandomListChooser;

/**
 * Automatic controller used before each wave.
 *
 * The goal of this controller is not to be optimal. It simply provides a clean
 * and fully automatic way to play the preparation phase, which is useful for
 * the final livrable demo, quick tests, and autonomous runs.
 */
public class AutoGameController implements GameController {

    private final Random random;
    private final ActionBuyTurret buyAction;
    private final ActionUpgradeTurret upgradeAction;
    private final ActionDowngradeTurret downgradeAction;
    private final ActionSellTurret sellAction;
    private final ActionDisplay displayAction;

    /**
     * Creates an automatic controller using random choosers.
     */
    public AutoGameController() {
        this.random = new Random();
        this.buyAction = new ActionBuyTurret(new RandomListChooser<>(), new RandomCoordsChooser());
        this.upgradeAction = new ActionUpgradeTurret(new RandomListChooser<>(), new RandomListChooser<>());
        this.downgradeAction = new ActionDowngradeTurret(new RandomListChooser<>(), new RandomListChooser<>());
        this.sellAction = new ActionSellTurret(new RandomListChooser<>());
        this.displayAction = new ActionDisplay();
    }

    @Override
    public void playPreparationPhase() {
        GameController.printPreparationHeader();
        GameController.printNextWavePreview();
        GameController.printTurretStates();

        int maxDecisions = Math.min(8, 2 + Game.instance().getNbWave());
        int decisions = 0;

        while (decisions < maxDecisions && !Game.instance().hasGameEnded()) {
            boolean played = this.playOneDecision();
            if (!played)
                break;
            decisions++;
        }

        GameController.printTurretStates();
        this.displayAction.execAction();
        System.out.println("Automatic preparation finished. Launching next wave...\n");
    }

    /**
     * Executes one automatic decision.
     *
     * @return true if an action was performed, false otherwise
     */
    private boolean playOneDecision() {
        int turretCount = Game.instance().getBoard().getTurrets().size();
        int credits = Game.instance().getShop().getCredits();

        if (turretCount == 0 && this.canBuyAnyTurret())
            return this.buyAction();

        if (credits >= 500 && turretCount < Math.max(3, Game.instance().getNbWave() + 1) && this.canBuyAnyTurret()) {
            if (this.random.nextDouble() < 0.60)
                return this.buyAction();
        }

        if (this.upgradeAction.isExecutable() && this.random.nextDouble() < 0.70)
            return GameController.runActionIfPossible(this.upgradeAction, "");

        if (this.canBuyAnyTurret() && this.random.nextDouble() < 0.50)
            return this.buyAction();

        if (this.downgradeAction.isExecutable() && this.random.nextDouble() < 0.10)
            return GameController.runActionIfPossible(this.downgradeAction, "");

        if (this.sellAction.isExecutable() && turretCount > 5 && this.random.nextDouble() < 0.05)
            return GameController.runActionIfPossible(this.sellAction, "");

        if (this.upgradeAction.isExecutable())
            return GameController.runActionIfPossible(this.upgradeAction, "");

        if (this.canBuyAnyTurret())
            return this.buyAction();

        return false;
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

    /**
     * Executes a buy action.
     *
     * @return true if a turret was bought, false otherwise
     */
    private boolean buyAction() {
        return GameController.runActionIfPossible(this.buyAction, "No turret can be bought right now.");
    }
}