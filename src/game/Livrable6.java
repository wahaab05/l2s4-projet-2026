package game;
import java.io.File;

import game.boards.Board;
import game.boards.DefaultBoard;
import game.boards.MultiPathBoard;
import game.controller.AutoGameController;
import game.controller.GameController;
import game.controller.ManualGameController;
import game.displays.CLI.StylizedDisplay;
import game.shops.Shop;
import game.waves.Wave;
import game.waves.WaveGenerator;
import utils.plane2d.Direction;

/**
 * Final livrable launcher.
 *
 * This class provides one single entry point for the complete game loop.
 * It supports:
 * - the two board types required by the subject,
 * - a manual preparation mode,
 * - an automatic / random preparation mode,
 * - progressive wave generation,
 * - repeated waves until the player has no life left.
 *
 * This final version uses a terminal display only.
 */
public class Livrable6 {

    private static final int DEFAULT_WIDTH = 20;
    private static final int DEFAULT_HEIGHT = 8;
    private static final int DEFAULT_NB_ROADS = 5;
    private static final int DEFAULT_INITIAL_LIFE = 20;




    /**
     * Prints the expected command-line syntax and exits.
     */
    public static void help() {
        System.out.println("Usage du rendu final :");
        System.out.println("  java -jar jar/towerdefense-a-interactive.jar <width> <height>");
        System.out.println("  java -jar jar/towerdefense-a-random.jar <width> <height>");
        System.out.println("  java -jar jar/towerdefense-b-interactive.jar <width> <height> <nbRoads>");
        System.out.println("  java -jar jar/towerdefense-b-random.jar <width> <height> <nbRoads>");
        System.out.println();
        System.out.println("Usage developpeur :");
        System.out.println("  Livrable6 <width> <height> <board:default|multipath> <mode:manual|auto> [nbRoads]");
        System.exit(1);
    }




    /**
     * Main entry point of the final livrable.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        int width;
        int height;
        int nbRoads = DEFAULT_NB_ROADS;
        String boardMode;
        String controlMode;

        String jarName = getCurrentJarName();

        try {
            if (jarName.contains("towerdefense-a-interactive")) {
                if (args.length != 2)
                    help();

                width = Integer.parseInt(args[0]);
                height = Integer.parseInt(args[1]);
                boardMode = "default";
                controlMode = "manual";
            } else if (jarName.contains("towerdefense-a-random")) {
                if (args.length != 2)
                    help();

                width = Integer.parseInt(args[0]);
                height = Integer.parseInt(args[1]);
                boardMode = "default";
                controlMode = "auto";
            } else if (jarName.contains("towerdefense-b-interactive")) {
                if (args.length != 3)
                    help();

                width = Integer.parseInt(args[0]);
                height = Integer.parseInt(args[1]);
                nbRoads = Integer.parseInt(args[2]);
                boardMode = "multipath";
                controlMode = "manual";
            } else if (jarName.contains("towerdefense-b-random")) {
                if (args.length != 3)
                    help();

                width = Integer.parseInt(args[0]);
                height = Integer.parseInt(args[1]);
                nbRoads = Integer.parseInt(args[2]);
                boardMode = "multipath";
                controlMode = "auto";
            } else {
                /*
                * Mode developpeur : permet de lancer Livrable6 directement
                * sans passer par les JAR du rendu final.
                */
                if (args.length != 4 && args.length != 5)
                    help();

                width = Integer.parseInt(args[0]);
                height = Integer.parseInt(args[1]);
                boardMode = args[2].toLowerCase();
                controlMode = args[3].toLowerCase();

                if (args.length == 5)
                    nbRoads = Integer.parseInt(args[4]);
            }
        } catch (Exception e) {
            help();
            return;
        }

        if (width <= 1 || height <= 1) {
            System.err.println("Width and height must be strictly greater than 1.");
            System.exit(1);
        }

        if (nbRoads <= 0) {
            System.err.println("The number of roads must be strictly positive.");
            System.exit(1);
        }

        Board board = createBoard(width, height, boardMode, nbRoads);
        GameController controller = createController(controlMode);

        Game.setInstance(DEFAULT_INITIAL_LIFE, board, new Shop(Shop.DEFAULT_INITIAL_CREDITS));
        Game.instance().setWaveGenerator(new WaveGenerator());

        addTerminalDisplay();
        printLaunchSummary(width, height, boardMode, controlMode, nbRoads);

        while (!Game.instance().hasGameEnded()) {
            System.out.println("\n=== PHASE 1 : preparation du joueur ===");
            controller.playPreparationPhase();

            System.out.println("\n=== PHASE 2 : generation des ballons ===");
            Wave wave = Game.instance().generateNextWave();
            System.out.println("Ballons generes : " + wave);

            System.out.println("\n=== PHASE 3 : simulation de la wave ===");
            Game.instance().startWave();
        }

        System.out.println("==========================================");
        System.out.println("Game over after wave " + Game.instance().getNbWave());
        System.out.println("Remaining credits: " + Game.instance().getShop().getCredits());
        System.out.println("Remaining life   : " + Game.instance().getLife());
        System.out.println("==========================================");
    }
    /**
     * Returns the name of the current JAR file.
     *
     * @return current JAR name, or an empty string when the program is not launched from a JAR
     */
    private static String getCurrentJarName() {
        try {
            String path = Livrable6.class
                .getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath();

            return new File(path).getName().toLowerCase();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Creates the board requested on the command line.
     *
     * @param width board width
     * @param height board height
     * @param boardMode board mode name
     * @param nbRoads number of roads used in multipath mode
     * @return instantiated board
     */
    private static Board createBoard(int width, int height, String boardMode, int nbRoads) {
        switch (boardMode) {
            case "default":
                return new DefaultBoard(width, height, Direction.RIGHT);
            case "multipath":
                return new MultiPathBoard(width, height, nbRoads);
            default:
                throw new IllegalArgumentException("Unknown board mode: " + boardMode);
        }
    }

    /**
     * Creates the controller requested on the command line.
     *
     * @param controlMode manual or auto
     * @return matching controller
     */
    private static GameController createController(String controlMode) {
        switch (controlMode) {
            case "manual":
                return new ManualGameController();
            case "auto":
                return new AutoGameController();
            default:
                throw new IllegalArgumentException("Unknown control mode: " + controlMode);
        }
    }

    /**
     * Adds the terminal display only.
     */
    private static void addTerminalDisplay() {
        Game.instance().addDisplay(StylizedDisplay.class);
    }

    /**
     * Prints a compact summary before the game starts.
     */
    private static void printLaunchSummary(int width, int height, String boardMode, String controlMode, int nbRoads) {
        System.out.println("==========================================");
        System.out.println("Livrable 6 - Final game loop");
        System.out.println("Display mode : terminal");
        System.out.println("Board mode   : " + boardMode);
        System.out.println("Control mode : " + controlMode);
        System.out.println("Board size   : " + width + " x " + height);
        if ("multipath".equals(boardMode))
            System.out.println("Road count   : " + nbRoads);
        System.out.println("Initial life : " + DEFAULT_INITIAL_LIFE);
        System.out.println("Initial cash : " + Shop.DEFAULT_INITIAL_CREDITS);
        System.out.println("==========================================");
    }
}