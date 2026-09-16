package game.displays.CLI;

import game.Game;
import game.cells.Cell;
import utils.io.ConsoleUtils;

/**
 * Basic non-color display, only show the cell types contained in the game board.
 */
public class BasicDisplay extends DisplayCLI
{
	/**
	 * Display cell composition of the game board in the console.
	 * May sometimes include this fish 🐡 in the display :p
	 */
	@Override
	public void display()
	{
		for (int j = 0; j < Game.instance().getBoard().getHeight(); j++)
		{
			for (int i = 0; i < Game.instance().getBoard().getWidth(); i++)
			{
				Cell c = Game.instance().getBoard().getCell(i, j);
				String cellString = c.toColoredString();

				if (c.hasBloons())
					cellString = cellString.replace(c.toString(), "o");

				System.out.print(cellString);
				System.out.print(" ");
			}
			System.out.println();
		}

		this.displayStatus();

		ConsoleUtils.flushBuffer();
	}

}
