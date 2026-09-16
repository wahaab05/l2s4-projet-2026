package game;

import game.boards.*;
import game.displays.*;
import game.displays.CLI.*;

/**
 * Livrable 1b
 */
public class Livrable1b
{
	/**
	 * Print help message
	 */
	public static void help()
	{
		System.out.println("Usage: Livrable1b <width:int> <height:int> <nbRoads:int>");
		System.exit(1);
	}

	/**
	 * main method
	 */
	public static void main(String args[])
	{
		if (args.length < 3)
			help();

		Board b = new MultiPathBoard(Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2]));

		Game.setInstance(1, b, null);

		Display bcd = new BasicColorDisplay();
		Display fd = new FancyDisplay();
		Display sd = new StylizedDisplay();

		System.out.println("= Normal version =");
		bcd.display();
		System.out.println("= Fancy version =");
		fd.display();
		System.out.println("= Stylized version =");
		sd.display();
	}
}
