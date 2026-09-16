package utils.plane2d;

import utils.maths.Comparator;

/**
 * 2d float vector object
 */
public class Vector2f {

	private float x;
	private float y;

	// CACHE DATA
	private float chached_dist_sqrd = -1.f;
	private float chached_dist = -1.f;

	/**
	 * Vector2f constructor
	 * @param x x component
	 * @param y y component
	 */
	public Vector2f(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	public Vector2f(double x, double y)
	{ this((float)x, (float)y); }


	/* ----------------	*\
	 *					*
	 *	🐟	GETTERS	 🐟	*
	 *					*
	\* ---------------- */



	/**
	 * Get the vector's X coordinate
	 * @return X coord
	 */
	public float x()
	{ return this.x; }

	/**
	 * Get this vector's Y coordinate
	 * @return Y coord
	 */
	public float y()
	{ return this.y; }
	
	
	/* ----------------	*\
	 *					*
	 *	🐟	SETTERS	 🐟	*
	 *					*
	\* ---------------- */



	/**
	 * set this vector's Y coordinate to one passed into parameters
	 * @param y the y coordinate to parameters
	 */

	
	public void setY(float y)
	{ this.y = y;}
	/**
	 * set this vector's X coordinate to one passed into parameters
	 * @param x the x coordinate to parameters
	 */
	public void setX(float x)
	{ this.x = x;}



	/* ----------------	*\
	 *					*
	 *	🐟	METHODS	 🐟	*
	 *					*
	\* ---------------- */



	/**
	 * Multiply coord by coord and return a new Vector2f containing
	 * as x the product of x's from args
	 * as y the product of y's from args
	 * @param v another Vector2f
	 * @return the result of their component-wise multiplication
	 */
	public Vector2f mult(Vector2f v)
	{
		return new Vector2f((float)this.x * (float)v.x, (float)this.y * (float)v.y);
	}

	/**
	 * Multiply coords by given scalar
	 * @param f a float scalar
	 * @return a new vector with both components multiplied by this scalar
	 */
	public Vector2f mult(float f)
	{
		return new Vector2f(this.x * f, this.y * f);
	}

	/**
	 * Computes the component difference
	 * Returns a new Vector2f with (this.x-v.x) (thix.y-v.y) as components
	 * @param v another vector2f
	 * @return a new vector corresponding do the components difference
	 */
	public Vector2f diff(Vector2f v) {
		return new Vector2f(this.x - v.x, this.y - v.y);
	}

	/**
	 * Sums Vector's components
	 * @return the sum of this vector's components
	 */
	public float compSum() {
		return this.x + this.y;
	}

	/**
	 * Add a scalar value to both vector's components
	 * @param value a float value
	 * @return a new Vector2f with v.x+value v.y+value as components
	 */
	public Vector2f addScalar(float value) {
		return new Vector2f(this.x + value, this.y + value);
	}
	/**
	 * Add two vectors component-wise
	 * @param v another Vector2f
	 * @return new Vector2f which is this + v
	 */
		public Vector2f add(Vector2f v) {
		return new Vector2f(this.x + v.x, this.y + v.y);
	}

	/**
	 * Add a vector's component to vector's components
	 * @param v another vector
	 * @return a new Vector2f with x+v.x y+v.y as components
	 */
	public Vector2f addVector(Vector2f v) {
		return new Vector2f(this.x + v.x, this.y + v.y);
	}

	/**
	 * Return a string representation of the object
	 */
	public String toString()
	{ return "Vector2f(x:" + this.x + " y:" + this.y + ")"; }


	/**
	 * Get the squared norm of this vector.
	 * @return squared norm
	 */
	public float normSqrd()
	{
		if(this.chached_dist_sqrd < 0.)
			this.chached_dist_sqrd = this.x*this.x + this.y*this.y;

		return this.chached_dist_sqrd;
	}


	/**
	 * Get the norm of this vector
	 * @return the norm
	 */
	public float norm()
	{
		if(this.chached_dist < 0.)
			this.chached_dist = (float)Math.sqrt(this.normSqrd());

		return this.chached_dist;
	}



	/**
	 * Check if the point described by this vector is within a given range of another point.
	 *
	 * @param v Vector describing other point
	 * @param range Comparison range to use
	 * @return an int describing the distance of the two points:
	 *	- {@code -1} if &mid; {@code v} &minus; {@code d} &mid; &lt; {@code range}
	 *	- {@code 0} if &mid; {@code v} &minus; {@code d} &mid; &equals; {@code range}
	 *	- {@code 1} if &mid; {@code v} &minus; {@code d} &mid; &gt; {@code range}
	 * 🐠
	 *	Does not use a square root.
	 */
	public int rangeCompare(Vector2f v, float range)
	{
		float rangeSqrd = range*range;
		Vector2f diff = this.diff(v);
		return Comparator.compFloat(diff.normSqrd(), rangeSqrd);
	}

	/**
	 * Get the normalized (of length 1) version of this vector.
	 * @return The normalized vector
	 */
	public Vector2f normalize()
	{
		return this.mult(1.f / this.norm());

	}
}
