package utils.maths;

/**
 * This class provides static tools for the comparison of floating point values.
 * {@code float}s and {@code double}s have a limited precision, so comparing operation results might yield undesired results (famous example: {@code 0.1 + 0.2 != 0.3}).
 *
 * The tools reports two values as equal if their difference is smaller than a given delta.
 * @see #delta
 *
 */
public class Comparator {

	/**
	 * Private constructor for static class
	 */
	private Comparator() {}
	
	/**
	 * Difference delta for determining the equality of two values.
	 */
	public static final float delta = 1.e-4f;

//       \/)/)
//     _'  oo(_.-. 
//   /'.     .---'
// /'-./    (
// )     ; __\
// \_.'\ : __|
//      )  _/
//     (  (,.
//   mrf'-.-'

	/**
	 * Compares two float values
	 * Return -1 if a &lt; b <br>
	 * Return 0 if a &equals; b &plusmn; delta <br>
	 * Return 1 if a &gt; b <br>
	 *
	 * Comparisons are made with a static error delta defined in this class.
	 * Given two values A and B, <br>
	 * &emsp; If B &isin; ]A-delta, A+delta[, B is said to be equal to A (and conversely).
	 * @param a first value to compare
	 * @param b second value to compare
	 * @return int corresponding to float comparison
	 */
	public static int compFloat(float a, float b) throws IllegalArgumentException
	{
		double diff = a - b;

		if (Math.abs(diff) < delta) // (a == b) +- delta
			return 0;
		else if (diff > 0) // a > b
			return 1;
		else if (diff < 0) // a < b
			return -1;

		throw new IllegalArgumentException("I don't know how but "+a+" is neither equal nor greater nor smaller than "+b); //lmao
	}

	/**
	 * Compare two double-precision floating point values.
	 * Does not grant any precision benefit, casts to {@code float} and compares.
	 * @param a First value
	 * @param b Second value
	 * @return int corresponding to comparison result
	 *
	 * @see #compFloat(float, float)
	 */
	public static int compFloat(double a, double b)
	{ return compFloat((float)a, (float)b); }


	/**
	 * Compare a double-precision value with a single-precision value.
	 * Does not grant any precision benefit, casts to {@code float} and compares.
	 * @param a First value
	 * @param b Second value
	 * @return int corresponding to comparison result
	 *
	 * @see #compFloat(float, float)
	 */
	public static int compFloat(double a, float b)
	{ return compFloat((float)a, b); }

	/**
	 * Compare a single-precision value with a double-precision value.
	 * Does not grant any precision benefit, casts to {@code float} and compares.
	 * @param a First value
	 * @param b Second value
	 * @return int corresponding to comparison result
	 *
	 * @see #compFloat(float, float)
	 */
	public static int compFloat(float a, double b)
	{ return compFloat(a, (float)b); }

}
