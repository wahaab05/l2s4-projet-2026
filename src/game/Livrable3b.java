package game;


import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import game.boards.Board;
import game.boards.MultiPathBoard;
import game.displays.CLI.StylizedDisplay;
import game.displays.Display;
import game.objects.bloons.Bloon;
import game.objects.bloons.templates.BlueBloon;
import game.objects.bloons.templates.GreenBloon;
import game.objects.bloons.templates.RedBloon;
import game.objects.bloons.templates.YellowBloon;
import game.objects.turrets.Turret;
import game.objects.turrets.templates.*;
import utils.maths.RandomUtils;

/**
 * Livrable 3b
 */
public class Livrable3b
{
	/**
	 * Print help message
	 */
	public static void help()
	{
		System.out.println("Usage: Livrable3b <width:int> <height:int> <nbChemins:int>");
		System.exit(1);
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
		int nbRoads = 0;

		try {
			width = Integer.parseInt(args[0]);
			height = Integer.parseInt(args[1]);
			nbRoads = Integer.parseInt(args[2]);
		} catch (Exception e) {
			help();
			System.exit(-1);
		}

		List<Class<? extends Display>> displayClasses = Arrays.asList(
			//BasicDisplay.class,
			//FancyDisplay.class,
			StylizedDisplay.class
		);

		List<String> names = Arrays.asList(
				//"Normal",
				//"Fancy",
				"Stylized"
		);

		List<Class<? extends Bloon>> bloonClasses = Arrays.asList(
				RedBloon.class,
				BlueBloon.class,
				GreenBloon.class,
				YellowBloon.class
		);

		Iterator<Class<? extends Display>>	itd = displayClasses.iterator();
		Iterator<String>					itn = names.iterator();

		Board board;
		while(itd.hasNext() && itn.hasNext())
		{
			System.out.println("= " + itn.next() + " version =");

			// Crée un plateau aléatoire dont la largeur la hauteur ainsi que le nombre de ballons sont fournis en argument de la ligne de commande
			// Crée un chemin aléatoire partant du bord gauche (et va vers la droite)
			board = new MultiPathBoard(width, height, nbRoads);
			int totalPV = 0;

			// Crée autant de ballons que de routes, de types aléatoires
			for(int i = 0; i < nbRoads; i++)
			{
				Class<? extends Bloon> randBC = bloonClasses.get(RandomUtils.randInt(0, bloonClasses.size()));
				Bloon bloon = board.addBloon(randBC);
				totalPV += bloon.getLife();
			}

			// Crée deux tours de chaque type et les place de manière aléatoire
			List<Class<? extends Turret>> tcs = Arrays.asList(
				DartMonkey.class,
				JunkyMonkey.class,
				SniperMonkey.class,
				CanonMonkey.class,
				FreezeMonkey.class,
				SlowMonkey.class
			);

			for(Class<? extends Turret> tc : tcs)
			{
				int nbPlaced = 0;
				while(nbPlaced < 2)
				{
					try {
						board.addTurret(
								tc,
								RandomUtils.randCoords(board.getWidth(), board.getHeight())
						);
						nbPlaced++;
					} catch (Exception e) {
						System.out.println("Log: Error when placing turret, trying again: "+e);
					}
				}
			}

			// game pv = total bloon's pv so game ends when all bloons escaped (no turret are reducing bloon's hp)
			Game.setInstance(totalPV, board, null);


			Game.instance().addDisplay(itd.next());
			Game.instance().startWave();
		}

	}
}
