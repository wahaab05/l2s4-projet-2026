package game;

import game.boards.*;
import game.displays.CLI.StylizedDisplay;
import game.displays.Display;
import game.objects.bloons.Bloon;

import game.objects.bloons.templates.BlueBloon;
import game.objects.bloons.templates.GreenBloon;
import game.objects.bloons.templates.RedBloon;
import game.objects.bloons.templates.YellowBloon;
import game.objects.turrets.templates.CanonMonkey;
import game.objects.turrets.templates.DartMonkey;
import game.objects.turrets.templates.JunkyMonkey;
import game.objects.turrets.templates.SniperMonkey;
import game.upgrade.UpgradeableAttribute;
import game.objects.turrets.*;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import utils.MyLogger;
import utils.datastructures.Tuple2;
import utils.maths.RandomUtils;
import utils.plane2d.Direction;

/**
 * Livrable 3a
 */
public class Livrable4a
{
	/**
	 * Print help message
	 */
	public static void help()
	{
		System.out.println("Usage: Livrable3a <width:int> <height:int> <nbBloons:int>");
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
		int nbBloons = 0;

		try {
			width = Integer.parseInt(args[0]);
			height = Integer.parseInt(args[1]);
			nbBloons = Integer.parseInt(args[2]);
		} catch (Exception e) {
			help();
			System.exit(-1);
		}

		Logger log = MyLogger.getInstance();

		List<Class<? extends Display>> displayClasses = Arrays.asList(
			//BasicDisplay.class,
			//FancyDisplay.class,
			StylizedDisplay.class
		);

		List<String> names = Arrays.asList(
				// "Normal",
				// "Fancy",
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
			board = new DefaultBoard(width, height, Direction.RIGHT);



			// Crée deux tours de chaque type et les place de manière aléatoire
			List<Class<? extends Turret>> tcs = Arrays.asList(
				DartMonkey.class,
				JunkyMonkey.class,
				SniperMonkey.class,
				CanonMonkey.class
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
						e.printStackTrace();
						System.out.println("Log: Error when placing turret, trying again: "+e);
					}
				}
			}


			// game pv = total bloon's pv so game ends when all bloons escaped (no turret are reducing bloon's hp)
			Game.setInstance(50, board, null);

			Game.instance().addDisplay(itd.next());

			Scanner scan = new Scanner(System.in);
			while(!Game.instance().hasGameEnded() && Game.instance().getNbWave() < 10)
			{
				// Crée autant de ballons que demandé, de types aléatoires
				for(int i = 0; i < nbBloons; i++)
				{
					Class<? extends Bloon> randBC = bloonClasses.get(RandomUtils.randInt(0, bloonClasses.size()));
					board.addBloon(randBC);
				}

				Game.instance().startWave();
				boolean shouldAddUpgrade = Game.instance().getNbWave() <= 5;

				for(Turret t : board.getTurrets())
				{
					boolean applied = false;
					final int MAX_TRIES = 5;
					int tries = 0;

					Tuple2<String, UpgradeableAttribute<?>> up;
					List<Tuple2<String, UpgradeableAttribute<?>>> upgradeables = t.getAllUpgradeableAttributes();

					for(tries = 0; tries < MAX_TRIES && !applied; tries++)
					{
						up = upgradeables.get( RandomUtils.randInt(upgradeables.size()) );

						if(up.second().canBeUpgraded())
						{
							up.second().upgrade();
							applied = true;
							log.info("Upgraded turret "+t+" with upgrade "+up.first()+" : "+up.second());
						}
					}

					if(tries == MAX_TRIES)
						log.info("Turret "+t+" doesn't have any "+(shouldAddUpgrade ? "up" : "down")+"grade left to apply");
				}


				System.out.print("Please input the number of bloons for this wave: ");

				try {
					nbBloons = Integer.parseInt(scan.nextLine());
					System.out.println("Parsed int: "+nbBloons);
				} catch (Exception e) {
					System.err.println("\n\nError while reading number: " + e);
					System.out.print("\nPlease input the number of bloons for this wave: ");
				}

			}
			scan.close();
		}
	}
}
