package game;

import java.util.List;
import java.util.Arrays;
import java.util.Iterator;

import game.boards.*;

import game.displays.Display;
import game.displays.CLI.BasicDisplay;
import game.displays.CLI.FancyDisplay;
import game.displays.CLI.StylizedDisplay;

import game.objects.bloons.Bloon;
import game.objects.bloons.templates.BlueBloon;
import game.objects.bloons.templates.GreenBloon;
import game.objects.bloons.templates.RedBloon;
import game.objects.bloons.templates.YellowBloon;
import utils.maths.RandomUtils;

/**
 * Livrable 2a
 */

public class Livrable2a
{
	/**
	 * Print help message
	 */
	public static void help()
	{
		System.out.println("Usage: Livrable2a <width:int> <height:int>");
		System.exit(1);
	}

	/**
	 * main method
	 */
	public static void main(String args[])
	{
		if (args.length < 2)
			help();

		int width = 0;
		int height = 0;

		try {
			width = Integer.parseInt(args[0]);
			height = Integer.parseInt(args[1]);
		} catch (Exception e) {
			help();
			System.exit(-1);
		}

		List<Class<? extends Display>> displayClasses = Arrays.asList(
			BasicDisplay.class,
			FancyDisplay.class,
			StylizedDisplay.class
		);

		List<String> names = Arrays.asList(
				"Normal",
				"Fancy",
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

		Board b;
		while(itd.hasNext() && itn.hasNext())
		{
			System.out.println("= " + itn.next() + " version =");

			b = new DefaultBoard(width, height);
			int totalPV = 0;

			for(int i = 0; i < 10; i++)
			{
				Class<? extends Bloon> randBC = bloonClasses.get(RandomUtils.randInt(0, bloonClasses.size()));
				Bloon bloon = b.addBloon(randBC);
				totalPV += bloon.initialLife;
			}

			// game pv = total bloon's pv so game ends when all bloons escaped (no turret are reducing bloon's hp)
			Game.setInstance(totalPV, b, null);

			Game.instance().addDisplay(itd.next());
			Game.instance().startWave();
		}

	}
}
