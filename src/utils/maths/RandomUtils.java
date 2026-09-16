package utils.maths;

import java.lang.System.Logger;
import java.util.Random;

import utils.MyLogger;
import utils.config.ConfigReader;
import utils.datastructures.Tuple2;

/**
 * Utilities to obtain (pseudo) random values.
 * Reads the seed used to run the program's randomness from the config file.
 */
public class RandomUtils
{
	private static long RAND_SEED = ConfigReader.getInt("seed");
	private static Random rand;

	static {
		if (RAND_SEED == 0)
		{
			rand = new Random();
			RAND_SEED = rand.nextInt();
		}

		rand = new Random(RAND_SEED);

		// TODO: Use MyLogger.
		// Current problem: Game instance not created when
		// class is loaded in, so MyLogger throws an error.
		System.out.println(" ====================== RANDOM SEED : " + RAND_SEED);
	}

	private RandomUtils() {}

// <( Jellyfish is still a fish )
//       _______
//  ,-~~~       ~~~-,
// (                 )
//  \_-, , , , , ,-_/
//     / / | | \ \
//     | | | | | |
//     | | | | | |
//    / / /   \ \ \
//    | | |   | | |

	/**
	 * Set the randomizer's seed (for static test cases)
	 * Create a new random object
	 * @param seed the seed to set
	 */
	public static void setSeed(int seed)
	{
		MyLogger.getInstance().warning("RandomUtils seed was reset to: " + seed);
		RandomUtils.rand = new Random(seed);
	}
	/**
	 * Get a random int, between Integer.MAX_VALUE and Integer.MIN_VALUE.
	 * @return random int
	 */
	public static int randInt()
	{ return RandomUtils.rand.nextInt(); }


	/**
	 * Get a random int, between {@code min} (inclusive) and {@code max} (exclusive).
	 * @param min Minimum value possible, included
	 * @param max Maximum value possible, excluded
	 * @return random int
	 */
	public static int randInt(int min, int max)
	{ return RandomUtils.rand.nextInt(max - min)+min; }

	/**
	 * Get a random int, between 0 (inclusive) and {@code max} (exclusive).
	 * @param max Maximum value possible, excluded
	 * @return random int
	 */
	public static int randInt(int max)
	{ return RandomUtils.rand.nextInt(max); }


	/**
	 * Get random 2D integer coordinates, between {@code (0, 0)} inclusive and {@code (maxX, maxY)} exclusive.
	 * @param maxX Maximum value for the generated X coordinate. Exclusive
	 * @param maxY Maximum value for the generated Y coordinate. Exclusive
	 * @return Randomly generated 2D coordinates
	 */
	public static Tuple2<Integer, Integer> randCoords(int maxX, int maxY)
	{
		return new Tuple2<Integer, Integer>(
				RandomUtils.randInt(maxX),
				RandomUtils.randInt(maxY)
		);
	}

}
