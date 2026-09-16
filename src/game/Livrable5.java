package game;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import game.actions.Action;
import game.actions.ActionBuyTurret;
import game.actions.templates.ActionDowngradeTurret;
import game.actions.templates.ActionUpgradeTurret;
import game.actions.templates.turrets.ActionBuyCanonMonkeyTurret;
import game.actions.templates.turrets.ActionBuyDartMonkeyTurret;
import game.actions.templates.turrets.ActionBuyFreezeMonkeyTurret;
import game.actions.templates.turrets.ActionBuyJunkyMonkeyTurret;
import game.actions.templates.turrets.ActionBuyNeedleMonkeyTurret;
import game.actions.templates.turrets.ActionBuySlowMonkeyTurret;
import game.actions.templates.turrets.ActionBuySniperMonkeyTurret;
import game.boards.Board;
import game.boards.DefaultBoard;
import game.displays.Display;
import game.displays.GUI.TextureDisplay;
import game.objects.bloons.Bloon;
import game.objects.bloons.templates.BlueBloon;
import game.objects.bloons.templates.GreenBloon;
import game.objects.bloons.templates.RedBloon;
import game.objects.bloons.templates.YellowBloon;
import game.objects.turrets.Turret;
import game.shops.Shop;
import game.upgrade.UpgradeableAttribute;
import utils.coordsChooser.RandomCoordsChooser;
import utils.datastructures.Tuple2;
import utils.listchooser.RandomListChooser;
import utils.maths.RandomUtils;
import utils.plane2d.Direction;

/**
 * Livrable 5
 */
public class Livrable5
{
	/**
	 * Print help message
	 */
	public static void help()
	{
		System.out.println("Usage: Livrable5 <width:int> <height:int> <nbBloons:int>");
		System.exit(1);
	}

	/**
	 * Displays the current state of all turrets placed on the board.
	 * For each turret, this method shows:
	 * - its number,
	 * - its type,
	 * - and the upgrades that are currently applied.
	 */
	private static void printTurretStates()
	{

		System.out.println("Turret upgrades:");

		// This variable is only used to number the turrets:
		// Turret 1, Turret 2, Turret 3, etc.
		int i = 1;

		// Go through all turrets currently placed on the board
		for (Turret t : Game.instance().getBoard().getTurrets())
		{
			// This string builder will store the list
			// of active upgrades for the current turret
			StringBuilder sb = new StringBuilder();

			// Each turret has several upgradeable attributes
			// We go through all of them one by one
			for (Tuple2<String, UpgradeableAttribute<?>> up : t.getAllUpgradeableAttributes())
			{
				// If the level is greater than 0,
				// it means that this upgrade has been applied
				if (up.second().getLevel() > 0)
				{
					if (sb.length() > 0)
						sb.append(", ");

					// Add the upgrade name and its current level
					sb.append(up.first()).append("=").append(up.second().getLevel());
				}
			}

			if (sb.length() == 0)
				sb.append("none");

			System.out.println(" - Turret " + i + " : " + t.getClass().getSimpleName() + " -> " + sb);

			// Move to the next turret number
			i++;
		}
	}

	
	/**
	 * main method
	 */
	public static void main(String args[])
	{
		if (args.length < 3)
			help();

		int width = 0;
		int height = 0;
		int nbBloons = 0;

		try {
			width = Integer.parseInt(args[0]);
			height = Integer.parseInt(args[1]);
			nbBloons = Integer.parseInt(args[2]);
		} catch (Exception e) {
			help();
			System.exit(-1);
		}

		List<Tuple2<String, Class<? extends Display>>> displayClasses = Arrays.asList(
			// new Tuple2<>("Normal", BasicDisplay.class),
			// new Tuple2<>("Fancy", FancyDisplay.class),
			// new Tuple2<>("Stylized", StylizedDisplay.class),
			// new Tuple2<>("Swing", SwingDisplay.class),
			new Tuple2<>("Texture", TextureDisplay.class)
		);

		List<Class<? extends Bloon>> bloonClasses = Arrays.asList(
				RedBloon.class,
				BlueBloon.class,
				GreenBloon.class,
				YellowBloon.class
		);

		RandomCoordsChooser coordsChooser = new RandomCoordsChooser();

		List<ActionBuyTurret> buyActions = Arrays.asList(
			new ActionBuyDartMonkeyTurret(coordsChooser),
			new ActionBuyNeedleMonkeyTurret(coordsChooser),
			new ActionBuySniperMonkeyTurret(coordsChooser),
			new ActionBuyCanonMonkeyTurret(coordsChooser),
			new ActionBuyJunkyMonkeyTurret(coordsChooser),
			new ActionBuyFreezeMonkeyTurret(coordsChooser),
			new ActionBuySlowMonkeyTurret(coordsChooser)
		);

		Iterator<Tuple2<String, Class<? extends Display>>>	itDisp = displayClasses.iterator();

		Board board;
		while(itDisp.hasNext())
		{
			Tuple2<String, Class<? extends Display>> currDisp = itDisp.next();
			System.out.println("= " + currDisp.first() + " version =");

			// Crée un plateau aléatoire dont la largeur la hauteur ainsi que le nombre de ballons sont fournis en argument de la ligne de commande
			// Crée un chemin aléatoire partant du bord gauche (et va vers la droite)
			board = new DefaultBoard(width, height, Direction.RIGHT);


			Game.setInstance(100, board, new Shop(10000)); // give a lot of money to be able to buy all turrets and upgrades without worrying about money ( just for this livrable ) //

			Game.instance().addDisplay(currDisp.second());


			// Create two turrets of each type randomly via actions
			for (ActionBuyTurret buyAction : buyActions)
			{
				buyAction.execAction();
				buyAction.execAction();
			}




			for (int wave = 1; wave <= 10 && !Game.instance().hasGameEnded(); wave++)
			{
				System.out.println("\n==============================");
				System.out.println("Wave " + wave);
				System.out.println("==============================");
				printTurretStates();
				// Crée autant de ballons que demandé, de types aléatoires
				for(int i = 0; i < nbBloons; i++)
				{
					Class<? extends Bloon> randBC = bloonClasses.get(RandomUtils.randInt(0, bloonClasses.size()));
					board.addBloon(randBC);
				}

				Game.instance().startWave();



				// Upgrades ========================================

				Action action;

				if (wave <= 5)
					action = new ActionUpgradeTurret(new RandomListChooser<>(), new RandomListChooser<>());
				else
					action = new ActionDowngradeTurret(new RandomListChooser<>(), new RandomListChooser<>());

				if (action.isExecutable()) // pour evite les errors //
					action.execAction();

				printTurretStates();// La deuxieme fois pour voir les changements d'upgrades//

			}

			Game.instance().removeDisplay(currDisp.second());
		}
	}
}
