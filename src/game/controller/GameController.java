package game.controller;

import java.util.Map;

import game.Game;
import game.actions.Action;
import game.objects.bloons.Bloon;
import game.objects.turrets.Turret;
import game.upgrade.UpgradeableAttribute;
import game.waves.Wave;
import utils.datastructures.Tuple2;

/**
 * Controller used during the preparation phase of a wave.
 *
 * A controller does not run the wave itself. Its responsibility is only to
 * decide what the player does before the next wave starts:
 * buy, sell, upgrade, downgrade, inspect the board, then confirm that the
 * game is ready to launch the next wave.
 *
 * This abstraction makes it possible to reuse the exact same game engine for:
 * - a manual mode, where the player chooses every action,
 * - an automatic mode, where choices are made by a simple strategy.
 */
public interface GameController {

    /**
     * Execute one full preparation phase.
     *
     * The method returns only when the controller considers that the game is
     * ready to launch the next wave.
     */
    void playPreparationPhase();

    /**
     * Prints a compact summary of the current game state.
     */
    static void printPreparationHeader() {
        System.out.println("\n==========================================");
        System.out.println("Preparation phase before wave " + (Game.instance().getNbWave() + 1));
        System.out.println("Life    : " + Game.instance().getLife());
        System.out.println("Credits : " + Game.instance().getShop().getCredits());
        System.out.println("Turrets : " + Game.instance().getBoard().getTurrets().size());
        System.out.println("==========================================");
    }

    /**
     * Prints the currently placed turrets and their applied upgrades.
     */
    static void printTurretStates() {
        if (Game.instance().getBoard().getTurrets().isEmpty()) {
            System.out.println("No turret is currently placed on the board.");
            return;
        }

        System.out.println("Placed turrets:");
        int i = 1;
        for (Turret turret : Game.instance().getBoard().getTurrets()) {
            StringBuilder sb = new StringBuilder();
            for (Tuple2<String, UpgradeableAttribute<?>> up : turret.getAllUpgradeableAttributes()) {
                if (up.second().getLevel() > 0) {
                    if (sb.length() > 0)
                        sb.append(", ");
                    sb.append(up.first()).append("=").append(up.second().getLevel());
                }
            }
            if (sb.length() == 0)
                sb.append("none");

            System.out.println(" - #" + i + " " + turret.getClass().getSimpleName()
                + " at (" + turret.getCell().getX() + ", " + turret.getCell().getY() + ")"
                + " -> " + sb);
            i++;
        }
    }

    /**
     * Prints a preview of the next wave if a wave generator is configured.
     */
    static void printNextWavePreview() {
        if (Game.instance().getWaveGenerator() == null) {
            System.out.println("No wave generator configured.");
            return;
        }

        Wave nextWave = Game.instance().getWaveGenerator()
            .generate(Game.instance().getNbWave() + 1, Game.instance().getBoard());

        System.out.println("Next wave preview:");
        System.out.println(" - number : " + nextWave.getNumber());
        System.out.println(" - total  : " + nextWave.getTotalBloons());
        System.out.println(" - threat : " + nextWave.getThreatScore());
        System.out.println(" - elite  : " + (nextWave.isEliteWave() ? "yes" : "no"));
        for (Map.Entry<Class<? extends Bloon>, Integer> entry : nextWave.getBloonCounts().entrySet()) {
            System.out.println("   * " + entry.getKey().getSimpleName() + " x" + entry.getValue());
        }
    }

    /**
     * Executes an action only if it is currently executable.
     *
     * @param action action to execute
     * @param unavailableMessage message displayed when the action cannot be executed
     * @return true if the action was executed, false otherwise
     */
    static boolean runActionIfPossible(Action action, String unavailableMessage) {
        if (!action.isExecutable()) {
            System.out.println(unavailableMessage);
            return false;
        }

        action.execAction();
        return true;
    }
}