package utils.plane2d;

/**
 * Enum representing the different types of path shapes.
 * Composed of two directions (in no particular order):
 * 	- Input direction
 * 	- Output direction
 * Each enum value contains a char of its shape.
 */
public enum PathType {

	LEFT_RIGHT	(Direction.LEFT	, Direction.RIGHT	, '━'),
	LEFT_UP		(Direction.LEFT	, Direction.UP		, '┛'),
	LEFT_DOWN	(Direction.LEFT	, Direction.DOWN	, '┓'),
	RIGHT_UP	(Direction.RIGHT, Direction.UP		, '┗'),
	RIGHT_DOWN	(Direction.RIGHT, Direction.UP		, '┏'),
	UP_DOWN		(Direction.UP	, Direction.DOWN	, '┃');
	// FISH_UP

	private Direction d1;
	private Direction d2;
	private char c;

	private PathType(Direction d1, Direction d2, char c)
	{
		this.d1 = d1;
		this.d2 = d2;
		this.c = c;
	}


	/* ----------------- *\
	 *					 *
	 * 		GETTERS		 *
	 *					 *
	\* ----------------- */

	/**
	 * Get an array composed of the 2 directions this path char represents.
	 * @return The 2 directions
	 */
	public Direction[] getDirections()
	{
		return new Direction[] {
			this.d1,
			this.d2
		};
	}

	/**
	 * Get the char associated with this path shape
	 * @return Char the shape of this path
	 */
	public char getChar()
	{ return this.c; }


	/* ----------------- *\
	 *					 *
	 * 		METHODS		 *
	 *					 *
	\* ----------------- */

	/**
	 * Get the PathChar instance from 2 given directions. The order of `a` and `b` is not important.
	 * @param a First direction
	 * @param b Second direction
	 * @return The PathChar instance
	 * @throws IllegalArgumentException If `a` and `b` are the same
	 */
	public static PathType fromDirections(Direction a, Direction b) throws IllegalArgumentException
	{
		if(a == b)
			throw new IllegalArgumentException("fromDirections args `a` and `b` cannot be equal. Supplied: "+a+" , "+b);

		// Divide cases by common directions
		if(a != Direction.LEFT && b != Direction.LEFT)
		{
			if(a != Direction.RIGHT && b != Direction.RIGHT)
				return UP_DOWN;

			else
				return
					(a == Direction.UP || b == Direction.UP) ? RIGHT_UP: RIGHT_DOWN;
		}

		// 3 cases with LEFT dir
		else
		{
			if(a == Direction.RIGHT || b == Direction.RIGHT)
				return LEFT_RIGHT;


			if(a == Direction.UP || b == Direction.UP)
				return LEFT_UP;

			return LEFT_DOWN;
		}
	}

}
