package game.displays.CLI;

import game.Game;
import utils.io.ConsoleUtils;

/**
 * Basic display, using ansi codes to display cell types with different colors.
 */
public class BasicColorDisplay extends DisplayCLI
{
	/**
	 * Display cell composition of the game board in the console, with color ansi codes.
	 */
	@Override
	public void display()
	{
		for (int j = 0; j < Game.instance().getBoard().getHeight(); j++)
		{
			for (int i = 0; i < Game.instance().getBoard().getWidth(); i++)
			{
				System.out.print(Game.instance().getBoard().getCell(i, j).toColoredString());
				System.out.print(" ");
			}
			System.out.println();
		}

		System.out.println(
				"\nState: [Lives: "+Game.instance().getLife()+
				"] [Tick rate: "+Game.instance().getGoalTickRate()+
				" Hz]\n"
		);

		ConsoleUtils.flushBuffer();
	}

}
