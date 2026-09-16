package game.cells;

import utils.io.*;

/**
 * Represents a road cell where bloons can move through but turrets cannot be placed, neither can fishes 🐟
 */
public class RoadCell extends Cell {
	/**
	 * Constructs a RoadCell with given coordinates.
	 * >('>
	 * @param x x-coordinate
	 * @param y y-coordinate
	 */
	public RoadCell(int x, int y)
	{
		super(x, y);
		this.col = Color.red;
	}

	@Override
	public String toString()
	{
		return "R";
	}
}
