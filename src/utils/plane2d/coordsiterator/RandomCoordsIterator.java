package utils.plane2d.coordsiterator;

import utils.plane2d.Direction;

/**
 * Coords Iterator used to generate random coordinates and paths
 */
public class RandomCoordsIterator extends CoordsIterator {

	/**
	 * Create a RandomCoordsIterator which will evolve randomly in a rectangle of a given size and will follow a main direction
	 * @param maxWidth Width of rectangle to move in
	 * @param maxHeight Height of rectangle to move in
	 * @param direction Main direction to follow and not go against
	 */
    public RandomCoordsIterator(int maxWidth, int maxHeight, Direction direction)
    {
        super(maxWidth, maxHeight, direction);
    }

    /**
     * Moves in the random direction, except for:
	 * 	- the opposite of where it moved before
	 * 	- the opposite of where the main direction of the road goes
     */
    public void next() throws CoordsIteratorReachedLimitException
    {
        this.moveInDirection(this.choseRandomDirection(this.direction.opposite()));
    }

    /**
     * Moves in the random direction except, for
	 * 	- the opposite of where it moved before
	 * 	- the opposite of the main direction of the road
     */
	public void previous() throws CoordsIteratorReachedLimitException
    {
		this.moveInDirection(this.choseRandomDirection(this.direction.opposite()));
	}

    /**
     * Chose the cell in the given direction, with a chance of it being perpendicular to that direction.
	 * It verifies if there is a cell in the chosen direction, and tries again if not.
     * @param direction direction to set chose either it or the one perpendicular to it
	 * @return The chosen direction
	 */
    protected Direction choseRandomDirection(Direction direction)
    {
        Direction chosenDirection;

        do {
            Direction[] directions =
			{
                    direction.opposite(),
                    this.lastDirection.opposite()
			};

            chosenDirection = Direction.randomExcept(directions);
        }
        // check if the chosen direction doesn't move the cursor overboard
        while (!this.hasInDirection(chosenDirection));

        return chosenDirection;
    }
}
