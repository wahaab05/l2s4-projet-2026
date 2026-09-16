package game.displays.CLI;

import game.Game;
import game.displays.Display;
import utils.io.ConsoleUtils;

/**
 * Dislay type destined to be shown in the console.
 */
public abstract class DisplayCLI extends Display
{
	/**
	 * Clear the console and display the new game state.
	 */
	@Override
	public void repaint() {
		ConsoleUtils.clearConsole();
		this.display();
		ConsoleUtils.flushBuffer();
	}

	/**
	 * Fills a String tab representing the board with the corresponding cell String representation, when no value is already set
	 * @param tmpBoard a 2D String tab (same size as the board)
	 * @return the filles tmpBoard
	 */
	public String[][] fillStringBoard(String[][] tmpBoard)
	{
		for (int j=0; j<Game.instance().getBoard().getHeight(); j++)
		{
			for (int i=0; i<Game.instance().getBoard().getWidth(); i++)
			{
				if (tmpBoard[j][i] == null)
					tmpBoard[j][i] = Game.instance().getBoard().getCell(i, j).toColoredString();
			}
		}
		return tmpBoard;
	}

	public void displayStatus()
	{
		System.out.println("\nState: [Lives: "+Game.instance().getLife()+"] [Tick rate: "+Game.instance().getGoalTickRate()+" Hz] (here's a little fish: 🐟)\n");
	}

	public void close()
	{ return; } // Nothing to do

}
