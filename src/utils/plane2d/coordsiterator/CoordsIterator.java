package utils.plane2d.coordsiterator;

import utils.maths.RandomUtils;
import utils.plane2d.Direction;

/**
 * Coords iterator used to generate straight paths
 */
public abstract class CoordsIterator {

	// TODO @markusavrelius documentation
	protected int maxWidth;
	protected int maxHeight;
	protected int x;
	protected int y;
	protected Direction direction;
	protected Direction lastDirection;

	/* ---------------- *\
	 *					*
	 *	 CONSTRUCTOR	*
	 *					*
	\* ---------------- */


	/**
	 * Creates an Iterator to iterate through a grid using methods like next previous randomNext or randomPrevious	and places the cursor in the end of the direction its facing
	 * @param maxWidth the width of the grid to be iterated through
	 * @param maxHeight the height of the grid to be iterated through
	 * @param direction the direction in wich the path should go(You can move in any direction except the one directly opposite to your current flow. That is for example if you have the direction chosen as RIGHT you cannot get the cell to your left calling the method next)
	 */
	public CoordsIterator(int maxWidth, int maxHeight, Direction direction)
	{
		if (direction == Direction.RIGHT)
		{
			this.x = maxWidth - 1;
			this.y = RandomUtils.randInt(maxHeight);
		}
		if (direction == Direction.LEFT)
		{
			this.x = 0;
			this.y = RandomUtils.randInt(maxHeight);
		}
		if (direction == Direction.DOWN)
		{
			this.x = RandomUtils.randInt(maxWidth);
			this.y = maxHeight - 1;
		}
		if (direction == Direction.UP)
		{
			this.x = RandomUtils.randInt(maxWidth);
			this.y = 0;
		}

		this.maxWidth = maxWidth;
		this.maxHeight = maxHeight;
		this.direction = direction;
		this.lastDirection = direction.opposite();
	}



	/* ---------------- *\
	 *					*
	 *		GETTERS		*
	 *					*
	\* ---------------- */

	/**
	 * Get this iterator's moving space max width
	 * @return The max width
	 */
	public int getMaxWidth()
	{ return this.maxWidth; }

	/**
	 * Get this iterator's moving space max height
	 * @return The max height
	 */
	public int getMaxHeight()
	{ return this.maxHeight; }

	/**
	 * Get this iterator's vertical coordinate
	 * @return the vertical coordinate
	 */
	public int getY()
	{ return this.y; }

	/**
	 * Get this iterator's horizontal coordinate
	 * @return the horizontal coordinate
	 */
	public int getX()
	{ return this.x; }

	/**
	 * Get this iterator's direction
	 * @return this iterator's direction
	 */
	public Direction getDirection()
	{ return this.direction; }



	/* ---------------- *\
	 *					*
	 *		Methods		*
	 *					*
	\* ---------------- */

	/**
	 * Check if a direction from this iterator's coordinates is not out of bounds.
	 * @param direction Direction to check
	 * @return True if the iterator can advance in the supplied direction
	 */
	public boolean hasInDirection(Direction direction)
	{
		int y = this.getY() + direction.getY();
		int x = this.getX() + direction.getX();
		//          ^
		//        //                        ___   ___
		//      (*)     "O"                /  _   _  \
		//     (*)                           / \ / \
		//    (*)    "O"                    |   |   |    |\
		//   //                             |O  |O  |___/  \     ++
		//  //                               \_/ \_/    \   | ++
		// //                              _/      __    \  \
		// /     /|   /\                  (________/ __   |_/
		//      / |  |  |                   (___      /   |    |\
		//     / /  /   |                     \     \|    |___/  |
		//    |  | |   /                       \_________      _/   ++++
		//   /   | |  |                      ++           \    |
		//  |   / /   |                              ++   |   /  +++
		// /   /  |   |                               ++ /__/
		// ~~~ ~~~~   ~~~~~~~~~~~~  ~~~~~~~~~~~~~  ~~~~        ~~+++~~~~ ~
		return
			0 <= x && x < this.getMaxWidth() &&
			0 <= y && y < this.getMaxHeight();
	}


	/**
	 * Check if the iterator has a next position
	 * @return True if is has a next position
	 */
	public boolean hasNext()
	{
		return this.hasInDirection(this.direction);
	}


	/**
	 * Check if an iterator has a previous position
	 * @return True if it has a previous position
	 */
	public boolean hasPrevious()
	{
		return this.hasInDirection(this.direction.opposite());
	}


	/**
	 * moves the cursor forward
	 */
	public abstract void next();


	/**
	 * moves the cursor backwards
	 */
	public abstract void previous();


	/**
	 * Moves the cursor in the direction given
	 * @param direction direction to move the cursor to
	 */
	public void moveInDirection(Direction direction)
	{
		if (this.hasInDirection(direction))
		{
			this.x += direction.getX();
			this.y += direction.getY();
			this.lastDirection = direction;
		}
		else
		{
			throw new
				CoordsIteratorReachedLimitException("the CoordsIterator have reached the end of the board");
		}
	}
}
