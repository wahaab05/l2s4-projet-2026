package utils.plane2d;

import utils.maths.RandomUtils;


/**u
 * Enum representing one of four directions:
 *	- Two strictly vertical (UP / DOWN)
 *	- Two strictly horizontal (LEFT / RIGHT)
 */
public enum Direction {

	// TODO set vectors instead of tuples
	/** direction UP (y--) */
	UP(0,-1),
	/** direction DOWN (y++) */
	DOWN(0,1),
	/** direction LEFT (x--) */
	LEFT(-1,0),
	/** direction RIGHT (x++) */
	RIGHT(1,0);

	private Vector2f coords;
	private Direction opposite;

	static {
		UP.opposite = DOWN;
		DOWN.opposite = UP;
		LEFT.opposite = RIGHT;
		RIGHT.opposite = LEFT;
	}

	private Direction(int x, int y)
	{
		this.coords = new Vector2f((float) x, (float) y);
	}


	/* ---------------- *\
	 *					*
	 *		GETTERS		*
	 *					*
	\* ---------------- */

	/**
	 * Get the opposite direction to this direction
	 * @return The opposite direction
	 */
	public Direction opposite()
	{ return this.opposite; }

	/**
	 * Get the X coordinate of this direction (one of [-1, 0, 1])
	 * @return X coordinate
	 */
	public int getX()
	{ return (int) this.coords.x(); }

	/**
	 * Get the Y coordinate of this direction (one of [-1, 0, 1])
	 * @return Y coordinate
	 */
	public int getY()
	{ return (int) this.coords.y(); }

	/**
	 * Get the X and Y coordinates of this direction as a Vector2f
	 * @return Direction coordinates as a Vector2f
	 */
	public Vector2f getVect()
	{ return this.coords; }


	/* ---------------- *\
	 *					*
	 *		METHODS		*
	 *					*
	\* ---------------- */


	/**
	 * Get a random direction amongst all 4
	 * @return a random direction
	 */
	public static Direction random()
	{
		int index = RandomUtils.randInt(Direction.values().length);
		return Direction.values()[index];
	}


	/**
	 * Get the direction associated with this coordinates.
	 * @param x X direction. Must be one of [-1, 0, 1]
	 * @param y Y direction. Must be one of [-1, 0, 1]
	 * @return The direction queried for
	 * @throws IllegalArgumentException if (`x`, `y`) not in (-1, 0) (1, 0) (0, -1) (0, 1)
	 */
	public static Direction fromCoords(int x, int y)
	{
		if(x == 0)
		{
			if(y == -1)
				return UP;
			if(y == 1)
				return DOWN;

			throw new IllegalArgumentException(
				"fromCoords arguments must be one of : (-1, 0) (1, 0) (0, -1) (0, 1). Supplied: ("+x+", "+y+")"
			);
		}

//  _________         .    .
// (..       \_    ,  |\  /|
//  \       O  \  /|  \ \/ /
//   \______    \/ |   \  / 
//      vvvv\    \ |   /  |
//      \^^^^  ==   \_/   |
//       `\_   ===    \.  |
//       / /\_   \ /      |
//       |/   \_  \|      /
//  snd         \________/

		if(y == 0)
		{
			if(x == -1)
				return LEFT;
			if(x == 1)
				return RIGHT;
		}

		throw new IllegalArgumentException(
			"fromCoords arguments must be one of : (-1, 0) (1, 0) (0, -1) (0, 1). Supplied: ("+x+", "+y+")"
		);
	}

	/**
	 * Generate a random direction amongst those not contained in the supplied directions
	 * @param directions Directions to discard when choosing a new one
	 * @return The new direction not contained in the array
	 * @throws IllegalArgumentException If all directions are supplied in array
	 */
	public static Direction randomExcept(Direction[] directions) throws IllegalArgumentException
	{
		// Check if at least one direction can be returned
		boolean oneDirNotBlocked = false;
		for(Direction d1 : Direction.values())
		{
			boolean isInList = false;
			for(Direction d2 : directions)
			{
				if(d1 == d2)
					isInList = true;
			}

			if(!isInList)
			{
				oneDirNotBlocked = true;
				break;
			}
		}

		if(!oneDirNotBlocked)
			throw new IllegalArgumentException("No direction available for random generation: Every direction were passed in arguments");

		return randomExceptNoCheck(directions);
	}

	private static Direction randomExceptNoCheck(Direction[] directions)
	{
		Direction d = Direction.random();

		for(Direction d1 : directions)
			if(d == d1)
				return Direction.randomExceptNoCheck(directions);

		return d;
	}
}
