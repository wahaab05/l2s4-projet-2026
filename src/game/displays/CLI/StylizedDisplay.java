package game.displays.CLI;

import game.Game;

import game.cells.Cell;
import game.roads.Road;
import game.roads.RoadTile;
import utils.io.*;

/**
 * Display the state of this game, using special chars to display paths used by bloons.
 * Color CLI display, using ansi codes.
 */
public class StylizedDisplay extends DisplayCLI {

	private static final String[] bloonStrings =
	{
		"○",
		"①",
		"②",
		"③",
		"④",
		"⑤",
		"⑥",
		"⑦",
		"⑧",
		"⑨",
		"⑩",
		"⑪",
		"⑫",
		"⑬",
		"⑭",
		"⑮",
		"⑯",
		"⑰",
		"⑱",
		"⑲",
		"⑳"
	};

	@SuppressWarnings("unused")
	private String fish = "🐟";

	/**
	 * Display a board by manually searching through roads for cells used as paths.
	 * Paths are displayed using special chars (e.g. '┛', '━', ...).
	 */
	@Override
	public void display()
	{
		String[][] tmpBoard = this.stylizedPaths();
		tmpBoard = this.fillStringBoard(tmpBoard);

		for (int j = 0; j < Game.instance().getBoard().getHeight(); j++) {
			for (int i = 0; i < Game.instance().getBoard().getWidth(); i++)
				System.out.print(tmpBoard[j][i]);

			System.out.println();
		}

		this.displayStatus();

		ConsoleUtils.flushBuffer();
	}


	/**
	 * Using all paths in this board, fill a  2d string array representing the cells contained in a path, with a char representation (e.g. '┛', '━', ...).
	 * Any cell not in a path results in a null string in the 2d array.
	 * @return The 2d array
	 */
	private String[][] stylizedPaths()
	{
		String[][] b = new String[Game.instance().getBoard().getHeight()][Game.instance().getBoard().getWidth()];

		Color col = Color.yellow;

		for (Road r : Game.instance().getBoard().getRoads()) {

			RoadTile current = r.getHead();
			Cell c;

			do {
				c = current.getCell();
				String newStr;


				if(!c.hasBloons())
				{
					String cellStr = String.valueOf(current.getPathType().getChar());
					String currStr = b[c.getY()][c.getX()];

					// Get the color string directly to compare with a potential colored string already placed here
					cellStr = ConsoleUtils.colorString(cellStr, col);

					/*
					 * Check if string has:
					 *	- No path
					 *	- Already a path in the same orientation
					 */
					if(currStr == null || currStr.equals(cellStr))
						newStr = cellStr;

					// Else it's a crossing
					//       .
					// \_____)\_____
					// /--v____ __`<
					//         )/
					//         '
					else
						newStr = ConsoleUtils.colorString(String.valueOf('╋'), col);
				}
				else
				{
					int num = c.getBloons().size();
					int max = StylizedDisplay.bloonStrings.length;
					String bloonStr = StylizedDisplay.bloonStrings[num < max ? num : 0];

					newStr = ConsoleUtils.colorString(bloonStr, col);
				}

				b[c.getY()][c.getX()] = newStr;

				current = current.next();
			} while (current != null);
		}

		return b;
	}


}
