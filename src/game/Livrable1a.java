package game;

import game.boards.*;
import game.displays.*;
import game.displays.CLI.*;

/**
 * Livrable 1a
 */
public class Livrable1a
{
	/**
	 * Print help message
	 */
	public static void help()
	{
		System.out.println("Usage: Livrable1a <width:int> <height:int>");
		System.exit(1);
	}

	/**
	 * main method
	 */
	public static void main(String args[])
	{
		if (args.length < 2)
			help();

		Board b = new DefaultBoard(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

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
