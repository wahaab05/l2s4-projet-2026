package game.objects.bloons;

import java.util.logging.Logger;

import game.Game;
import game.cells.Cell;
import game.objects.GameObject;
import game.objects.projectiles.Projectile;
import game.objects.turrets.Targetable;
import game.roads.RoadTile;
import utils.MyLogger;
import utils.plane2d.Vector2f;
/**
* This class represents the entity type able to deal damage to the player
*/
public abstract class Bloon extends GameObject implements Targetable {

	public final float speed;

	public final int initialLife;
	protected int life;

	protected RoadTile tile;

	public float distanceToTheEnd;

	// --------------------
	// Slow & Freeze Fields ><(((°>
	// --------------------

	/** Slow amount applied to the bloon (0 = no slow, 0.5 = 50% speed reduction) */
	protected float slowAmount = 0f;

	/** Remaining ticks for the slow effect */
	protected int slowDuration = 0;

	/** True if the bloon is frozen */
	protected boolean frozen = false;

	/** Remaining ticks for the freeze effect */
	protected int freezeDuration = 0;

	public static final float HIT_BOX_RADIUS = 0.25f;
	/*  Reward given to the player when this bloon is destroyed*/
	public static final int DESTROY_REWARD = 10;
	/*Prevents giving the destruction reward more than once */
	protected boolean deathRewardGranted = false;


	/**
	 * Create a new bloon instance
	 * 🪼
	 * @param initialLife Maximum life points at the start of its life
	 * @param speed Speed of the bloon, in cells/tick. Must always be &le; 1.
	 * @param t Road tile to place bloon at the center of. Must not be {@code null}.
	 */
	public Bloon(int initialLife, float speed, RoadTile t)
	{
		super(t.getCell().getVectorCenter());
		this.speed = speed;

		this.initialLife = initialLife;
		this.life = initialLife;

		this.setTileAndCenter(t);
	}


	/* ---------------- *\
	 *					*
	 *		GETTERS		*
	 *					*
	\* ---------------- */

	/**
	 * Get this bloon's x coordinate
	 * @return x coordinate
	 */
	public float x()
	{ return this.coords.x(); }

	/**
	 * Get this bloon's y coordinate
	 * @return y coordinate
	 */
	public float y()
	{ return this.coords.y(); }

	/**
	 * Get this Bloon's coordinates as a Vector2f
	 * @return this bloon's coordinates as Vector2f
	 */
	public Vector2f getCoords() {
		return new Vector2f(this.x(), this.y());
	}

	/**
	 * Return this bloon current movement direction as a normalized Vector2f
	 * The direction vector will never point toward the closest fish (probably..)
	 * @return this bloon's current direction as a normalized Vector2f
	 */
	public Vector2f getDirection()
	{
		float dist = this.signedDistanceFromCellCenter();
		RoadTile t = this.tile;

		if(dist < 0.f)
			return t.getInputDirection().getVect();
		else
			return t.getOutputDirection().getVect();
	}

	/**
	 * 🐡 {@see game.objects.turrets.Targetable#getDirectionVectorToBeReached}
	 */
	public Vector2f getDirectionVectorToBeReached(Projectile p)
	{
		/** Direction = destination - currentPos
		 In this case 'this' (a bloon) is the destination */
		return this.getCoords().diff(p.getCoords());
	}

	/**
	 * Get this bloon's current life points
	 * @return life points
	 */
	public int getLife()
	{ return this.life; }

	/**
	 * Get the road tile on which this bloon is
	 * @return the road tile
	 */
	public RoadTile getTile()
	{ return this.tile; }

	/**
	 * Get the distance to the end of this bloon
	 * @return the distance to the end in float
	 */
	public float getDistanceToTheEnd()
	{ return (float)this.getTile().getDistToEnd() + this.signedDistanceFromCellCenter(); }

	/* ---------------- *\
	 *					*
	 *		METHODS		*
	 *					*
	\* ---------------- */


	/**
	 * Put this bloon on a given tile.
	 * This method places the bloon on the middle of its input edge.
	 * Adds this bloon in the tile's cell's contained bloons.
	 * @param t Tile to place bloon on
	 */
	public void setTileAndCenter(RoadTile t)
	{
		Cell c = t.getCell();
		Vector2f center = c.getVectorCenter();
		Vector2f input = t.getInputEdge().getVect();

		this.coords = center.addVector(input.mult(0.5f));

		c.addBloon(this);

		this.tile = t;
	}

	/**
	 * Put this bloon on a given tile, while preserving its coordinates.
	 * @param t Tile to place bloon on
	 */
	public void setTile(RoadTile t)
	{
		Cell c = t.getCell();
		c.addBloon(this);
		this.tile = t;
	}

	/**
	 * Add each component of the parsed vector to the actual bloon coords
	 * @param v x and y to add to this bloon's coords, as a Vector2f
	 */
	private void updateCoords(Vector2f v) {
		this.coords = this.coords.add(v);
	}

	/**
	 * Check if a bloon is dead
	 *
	 * @return true if the bloon has no more lifepoint left
	 */
	public boolean isDead()
	{ return this.life <= 0; }


	/**
	 * Move this bloon for the current tick, depending on the bloon's speed.
	 *
	 * The bloon is guaranteed to strictly lie on the line formed by the previous tile's
	 * center and the next tile's center.
	 *
	 * This method handles the change of tiles while the bloon moves.
	 *
	 * If this bloon has reached the end of the path, the bloon set its tile to NULL and is to
	 * not be considered part of the board anymore.
	 *
	 */
	public void move()
	{
		float d = this.signedDistanceFromCellCenter();

		RoadTile t = this.tile;

		Vector2f dir;

		Vector2f oe = t.getOutputEdge().getVect();
		Vector2f ie = t.getInputEdge().getVect();
		float effectiveSpeed = this.getEffectiveSpeed();

		/*
		 * Bloon is past the center
		 *
		 *	______ Output
		 *
		 *	   ^
		 *	   |
		 *	   b   -+
		 *			|  < d
		 *	   *   -+
		 *
		 */
		if(d > 0.f)
			dir = oe.mult(effectiveSpeed);

		// Bloon is before the center because d is negative
		else
		{
			// If bloon would move beyond center
			/*
			 *	   _________ Output
			 *
			 *			^
			 *			|	  < speed + d
			 *		 b--*
			 *
			 *		  ^
			 *		  -d
			 */
			if(-d <= effectiveSpeed)
			{
				// Split speed between d*input_dir
				// which moves the bloon the remaning
				// distance to the center
				dir = ie.mult(d);

				// and (speed + d)*output_dir
				// which moves it toward the output
				// with the remaning speed
				dir = dir.addVector(oe.mult(effectiveSpeed + d));
			}

			/*
			 * Else bloon is before the center and will
			 * not move past it in the current tick so
			 * no need to take the center into account
			 *
			 *		 -d
			 *	  +------+
			 *	  |		 |
			 *
			 *	  b--->  *
			 *
			 *		^
			 *	  speed
			 */
			else
				dir = ie.mult(-effectiveSpeed);
		}

		this.updateCoords(dir);

		// Check if bloon has changed cell
		Vector2f v = t.getCell().getVectorCenter();
		if(Math.abs(v.x() - this.x()) > 0.5 || Math.abs(v.y() - this.y()) > 0.5)
		{
			// Change its cell
			t.getCell().removeBloon(this);

			RoadTile next = t.next();

			if(next != null)
				this.setTile(next);

			else
			{
				this.tile.getRoad().addBloonToExitedList(this);
				this.tile = null;
			}
		}
	}


	/**
	 * Returns the signed orthogonal distance from the center, only taking into consideration the
	 * non-0 axis from the current tile's input direction.
	 *
	 *	The result is:
	 *	- Negative if the bloon is on cell's same side as the input edge
	 *	- Positive if the bloon is on the opposite side from the input edge
	 *
	 * @return The signed distance
	 */
	private float signedDistanceFromCellCenter()
	{
		Vector2f inputDir = this.tile.getInputEdge().getVect();
		Vector2f cellCenter = this.tile.getCell().getVectorCenter();

		// Cancel out unimportant direction
		cellCenter = cellCenter.mult(inputDir);
		Vector2f coords = this.getCoords().mult(inputDir);

		return cellCenter.compSum() - coords.compSum();
	}


	/**
	 * Reduces the life points of the bloon by the specified damage amount.
	 * If the life drops to zero or below, the bloon is considered dead.
	 * This method ensures that life never goes below zero.
	 *
	 * @param amount The amount of damage to apply to the bloon
	 */
	public void takeDamage(int amount) {
		if(this.isDead())
			return;

		// Subtract the damage from the bloon's current life
		this.life -= amount;

		// Prevent life from being negative
		if (this.life < 0) {
			this.life = 0;
		}

		if(this.life == 0 && !this.deathRewardGranted)
		{
			this.deathRewardGranted = true;
			if(Game.instance() != null && Game.instance().getShop() != null)
				Game.instance().getShop().addCredits(DESTROY_REWARD); /* project information */
		}
	}

	/**
	 * Generates a bloon of the given class
	 * @param bloonClass an instanciable subclass of Bloon
	 * @param <T> type of bloon to use
	 * @param t tile of the new bloon @see game.objects.bloons
	 * @return a bloon object of the given class
	 */
	public static <T extends Bloon> T createBloon(Class<T> bloonClass, RoadTile t)
	{
		try
		{
			return bloonClass.getDeclaredConstructor(RoadTile.class).newInstance(t);
		}
		catch (ReflectiveOperationException e)
		{
			System.out.println("Error: " + e);
			return null;
		}
	}



	// --------------------
	// Slow & Freeze Methods
	// --------------------

	/**
	 * Applies a slow effect to the bloon.
	 * If the bloon is frozen, the slow effect is ignored.
	 *
	 * A message is displayed only when the bloon starts being slowed.
	 *
	 * @param amount percentage of speed reduction
	 * @param duration duration of the slow in ticks
	 */
	public void applySlow(float amount, int duration)
	{
		if (!this.frozen)
		{
			boolean wasNotSlowed = (this.slowDuration <= 0);

			this.slowAmount = Math.max(this.slowAmount, amount);
			this.slowDuration = Math.max(this.slowDuration, duration);

			if(wasNotSlowed)
				MyLogger.getInstance().info("Bloon slowed: " + this.toString());
		}
	}

	/**
	 * Applies a freeze effect to the bloon.
	 * Any active slow effect is removed.
	 *
	 * A message is displayed only when the bloon becomes frozen.
	 *
	 * @param duration duration of the freeze in ticks
	 */
	public void applyFreeze(int duration)
	{
		boolean wasFrozen = this.frozen;

		this.frozen = true;
		this.freezeDuration = Math.max(this.freezeDuration, duration);
		this.slowAmount = 0f;
		this.slowDuration = 0;

		if(!wasFrozen)
			MyLogger.getInstance().info("Bloon stopped: " + this.toString());
	}

	/**
	 * Updates slow and freeze effects every game tick.
	 * Durations are decreased and effects are removed when they expire.
	 *
	 * When a freeze effect ends, a restart event is displayed.
	 */
	public void updateEffects()
	{
		if (this.frozen)
		{
			this.freezeDuration--;

			if (this.freezeDuration <= 0)
			{
				this.frozen = false;
				MyLogger.getInstance().info("Bloon restarted: " + this.toString());
			}
		}
		else if (this.slowDuration > 0)
		{
			this.slowDuration--;

			if (this.slowDuration <= 0)
				this.slowAmount = 0f;
		}
	}

	/**
	 * Returns the bloon's effective speed considering slow and freeze effects.
	 *
	 * @return the effective speed of the bloon
	 */
	public float getEffectiveSpeed() {
		if (frozen) return 0f;
		return speed * (1f - slowAmount);
	}


	/**
	 * Check if this bloon is out of the board.
	 * A bloon is considered out if it is not on any tile
	 * @return True if it is out
	 */
	public boolean isOutOfBoard()
	{ return this.tile == null; }


	@Override
	public void tick() {
		Logger logger = MyLogger.getInstance();
		logger.finer("Ticking bloon " + this.toString());
		if(this.isDead())
		{
			logger.info("Bloon destroyed: " + this.toString());
			this.tile.getCell().removeBloon(this);
		}
		else
		{
			this.updateEffects();
			this.move();
			logger.finest("Bloon coords: " + this.coords.toString());
		}
	}

	@Override
	public boolean isInHitRangeOf(Projectile p) {
		return this.getCoords().rangeCompare(
				p.getCoords(),
				Bloon.HIT_BOX_RADIUS + Projectile.HIT_RANGE
		) <= 0;
	}

	@Override
	public boolean isCollidingWith(Projectile p) {
		return this.isInHitRangeOf(p);
	}


	@Override
	public Vector2f directionFrom(Projectile p) {
		return this.getCoords().diff(p.getCoords());
	}

	/**
	 * String representation of the bloon
	 * @return the bloon meta-infos as string
	 */
	public String toString()
	{
		return this.hashCode() + " " + this.getClass().getSimpleName() + "(life: " + this.life + ")";
	}

}
