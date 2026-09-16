package utils.plane2d.coordsiterator;

import utils.plane2d.Direction;

/**
 * Coords iterator used to generate straight paths
 */
public class StraightCoordsIterator extends CoordsIterator {

	/**
	 * Create a new StraightCoordsIterator instance moving in a rectangle of a given size.
	 * Its starting point is selected randomly on the opposite side of the given direction.
	 * @param maxWidth Width of the rectangle to move through
	 * @param maxHeight Height of the rectangle to move through
	 * @param direction Direction to follow
	 */
	public StraightCoordsIterator(int maxWidth, int maxHeight, Direction direction)
	{
		super(maxWidth, maxHeight, direction);
	}



	/* ---------------- *\
	 *					*
	 * 		METHODS		*
	 * 					*
	\* ---------------- */
	
	/**
	 * Move this iterator in its direction
	 */
	public void next() throws CoordsIteratorReachedLimitException
	{
		this.moveInDirection(this.direction);
	}

	/**
	 * Move this iterator in its opposite direction
	 */
	public void previous() throws CoordsIteratorReachedLimitException
	{
		moveInDirection(this.direction.opposite());
	}
}
