package game.boards;



import game.cells.RoadCell;
import game.roads.Road;

import utils.maths.RandomUtils;
import utils.plane2d.Direction;
import utils.plane2d.coordsiterator.RandomCoordsIterator;

/**
 * A game board composed of a unique path guiding bloons (not fishes !)
 */
public class DefaultBoard extends Board {

	/**
	 * Create a new default board of a given size
	 *
	 * @param sizeX The board's horizontal size
	 * @param sizeY The board's vertical size
	 * @param direction direction of the road on this board
	 */
	public DefaultBoard(int sizeX, int sizeY, Direction direction)
	{
		super(sizeX, sizeY);
        Road road = this.generatePath(new RandomCoordsIterator(
					sizeX,
					sizeY,
					direction
				),
					RoadCell.class
			);

		this.roads.add(road);

		this.fillBoard();
	}
	/**
	 * Auxiliary class constructor
	 * Without direction: randomly chosen
	 * @param sizeX board's horizontal size
	 * @param sizeY board's vertical size
	 */
	public DefaultBoard(int sizeX, int sizeY)
	{ this(sizeX, sizeY, Direction.random()); }
    public DefaultBoard()
	{ this(RandomUtils.randInt(10,20), RandomUtils.randInt(10,20), Direction.random());     }
}
