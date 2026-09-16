package game.boards;

import java.util.ArrayList;
import java.util.List;

import game.cells.TurretCell;
import game.roads.Road;
import utils.plane2d.Direction;
import utils.plane2d.coordsiterator.StraightCoordsIterator;

/**
 * A board type where bloons each have their own path and travel in a straight line
 */
public class MultiPathBoard extends Board {

	/**
	 * Create a new MultiPathBoard instance of a given size
	 *
	 * @param sizeX The board's horizontal size
	 * @param sizeY The board's vertical size
	 * @param nbRoads Number of randomly generated roads to spawn
	 */
	public MultiPathBoard(int sizeX, int sizeY, int nbRoads)
	{
		super(sizeX, sizeY);

		for (Road r : this.generateStraightPaths(nbRoads))
			this.roads.add(r);

		this.fillBoard();
	}

	/**
	 * Generate new straight paths within the bounds of this board.
	 * The paths consists of a list of cells from this board.
	 * @param nbRoads number of roads to generate on this board
	 * @return Multiple straight paths
	 */
	protected List<Road> generateStraightPaths(int nbRoads)
	{
		List<Road> roads = new ArrayList<Road>();

		for(int i = 0; i < nbRoads; i++)
		{
			Road road = this.generatePath(
				new StraightCoordsIterator(
					this.getWidth(), this.getHeight(),
					Direction.random()
				),
				TurretCell.class
			);

			roads.add(road);
		}

		return roads;
	}

}
