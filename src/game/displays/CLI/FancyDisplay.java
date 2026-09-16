package game.displays.CLI;

import game.Game;
import game.cells.Cell;
import game.roads.*;
import utils.io.*;

/**
 * Display the state of the game board. Displays cells used in paths with the 'P' char.
 */
public class FancyDisplay extends DisplayCLI
{
	/**
	 * Displays board by manually searching through roads for cells used as path by bloons
	 * Show "path" (not technically a RoadCell but can be crossed by bloons) cells as "P"
	 */
	@Override
	public void display()
	{
		String[][] tmpBoard = this.searchPaths();
		tmpBoard = this.fillStringBoard(tmpBoard);

		for (int j = 0; j < Game.instance().getBoard().getHeight(); j++)
		{
			for (int i = 0; i < Game.instance().getBoard().getWidth(); i++)
			{
				System.out.print(tmpBoard[j][i]);
				System.out.print(" ");
			}
			System.out.println();
		}

		this.displayStatus();

		ConsoleUtils.flushBuffer();
	}


	/**
	 * Search for all roads in the board and return a String tab containing "P" where path goes by, null everywhere else
	 * @return a string tab corresponding to the board paths
	 */
	public String[][] searchPaths()
	{
		// 2d array with same dimensions as the board
		// used to store the String representation of each cell while seeking paths
		String[][] tmpBoard = new String[Game.instance().getBoard().getHeight()][Game.instance().getBoard().getWidth()];

		for (Road r : Game.instance().getBoard().getRoads())
		{
			RoadTile currentTile = r.getHead();
			Cell c;

			do {
				c = currentTile.getCell();
				String cellString = ConsoleUtils.colorString("P", Color.yellow);
				if (c.hasBloons())
					cellString = cellString.replace("P", "o");
				tmpBoard[c.getY()][c.getX()] = cellString;
				// >('>
				currentTile = currentTile.next();
			} while (currentTile != null);
		}
		return tmpBoard;
	}

}
