package game.objects.projectiles;

import java.util.List;
import java.util.logging.Logger;

import game.Game;
import game.objects.GameObject;
import game.objects.bloons.Bloon;
import game.objects.turrets.Targetable;
import utils.MyLogger;
import utils.plane2d.Vector2f;


/**
 * Abstract class for any projectile in the game.
 *
 * Each projectile has a name, damage value, and can hit multiple targets (area effect).
 * This class can be upgraded (like increasing damage or adding area effect).
 * 𓆞
 * Specific projectiles (Dart, Bomb, etc.) should extend this class
 * and define how they affect a bloon in ApplyEffect().
 */
public abstract class Projectile extends GameObject  {

	/** Name of the projectile */
	protected String name;

	/** Speed of the projectile */
	protected float speed;

	/** how much damage is dealt by this projectile */
	protected int damage;

	/** Target this projectile aims for */
	protected Targetable target;

	/** List of bloons this projectile can hit */
	protected List<Bloon> bloonsInRange;

	public static float HIT_RANGE = 0.1f;

	/**
	 * Constructor to create a projectile with name, damage and area effect flag.
	 *
	 * @param name Name of the projectile
	 * @param coords Initial coordinates of the projectile
	 * @param speed Speed of the projectile
	 * @param target Target to where the projectile should go
	 */
	public Projectile(String name, Vector2f coords, Targetable target, float speed, int damage) {
		super(coords);
		this.name = name;
		this.speed = speed;
		this.damage = damage;
		this.target = target;
		this.bloonsInRange = null;
	}

	/**
	 * Create a new projectile with a given name, coordinates, target and speed.
	 *
	 * @param name Name of the projectile.
	 * @param coords Spawning coordinates of the projectile.
	 * @param target Target the projectile will go towards.
	 * @param speed Velocity with which the projectile will move.
	 */
	public Projectile(String name, Vector2f coords, Targetable target, float speed) {
		this(name, coords, target, speed, 0);
	}



		/* ---------------- *\
		*					*
		*		GETTERS		*
		*					*
		\* ---------------- */


	/**
	 * Get the name of the projectile.
	 * @return projectile name
	 */
	public String getName() {
		return this.name;
	}


	/**
	 * Get the speed of this projectile.
	 * @return the speed value
	 */
	public float  getSpeed() {
		return this.speed;
	}


	/**
	 * Get the current coordinates of this projectile.
	 * @return the current coordinates
	 */
	public Vector2f getCoords() {
		return this.coords;
	}

	/**
	 * Get the list of bloons in range of this projectile.
	 * @return list of bloons in range. The bloons are ordered by distance from exit
	 */
	protected List<Bloon> getBloonsInRange()
	{
		this.bloonsInRange = this.computeBloonsInRange();
		return this.bloonsInRange;
	}

		/**
	 * Check if this projectile is dead.
	 * @return True if it is dead
	 */
	public boolean isDead()
	{ return this.target.isCollidingWith(this);}



	/* ---------------- *\
	 *					*
	 *		METHODS		*
	 *					*
	\* ---------------- */


	/**
	 * Moves the projectile according to its direction and speed.
	 */
	public void move() {
		coords = coords.add(this.getDirection().mult(this.speed));
	}


	/**
	 * Get the direction this projectile is going towards.
	 * @return The direction it is headed towards.
	 */
	public Vector2f getDirection()
	{
		Vector2f dir = this.target.directionFrom(this).normalize();

		// Could not normalize zero vector
		if(Float.isNaN(dir.x()) && Float.isNaN(dir.y()))
			return new Vector2f(0.f, 0.f);

		return dir;
	}


	/**
	 * Apply the projectile's effect to a single bloon
	 * @param b the bloon to whom apply the effect
	 */
	protected void applyEffect(Bloon b)
	{
		b.takeDamage(this.damage);
	}

	/**
	 * Compute the list of bloons in range this projectile can hit, ordered by distance to the exit.
	 * @return The list of bloons, ordered by distance to the exit
	 */
	protected List<Bloon> computeBloonsInRange()
	{
		List<Bloon> l = Game.instance().getBoard().getBloonsInRange(
				HIT_RANGE,
				this.getCoords().x(),
				this.getCoords().y()
		);

		if(l.isEmpty())
			return l;

		return l.subList(0,1);
	}


	@Override
	public void tick(){
		Logger logger = MyLogger.getInstance();
		logger.finer("Ticking projectile " + this.toString());
		this.move();
		logger.finest("projectile coords: " + this.coords.toString());

		if(this.target.isInHitRangeOf(this))
		{
			logger.finer("Projectile is in hit range of it's target");
			List<Bloon> inRange = this.getBloonsInRange();

			for(Bloon b : inRange)
			{
				MyLogger.getInstance().info(
						"Bloon hit by " + this.getClass().getSimpleName() + " : " +  b.toString()
				);

				this.applyEffect(b);
			}
		}

	}



	/**
	 * Generates a new projectile
	 * @param <T> a subclass of 'Projectile'
	 *	and more specifically a subclass of 'TargetProjectile' or 'AreaProjectile'
	 *	@see game.objects.projectiles.AreaProjectile
	 *	note that these two types cannot be instantiated directly by this factory, you must use a subclass of these with following constructor:
	 *		`ProjectileSubclass(Vector2f coords, Targetable target)`
	 * @param projectileClass class of projectile to create
	 * @param coords coords of the new projectile
	 * @param target target of the new projectile
	 * @return a target projectile
	 *
	 * @throws IllegalArgumentException If there is no such constructor
	 */
	public static <T extends Projectile> T create(Class<T> projectileClass, Vector2f coords, Targetable target)
		throws IllegalArgumentException
	{
		try
		{
			return projectileClass.getDeclaredConstructor(Vector2f.class, Targetable.class).newInstance(coords, target);
		}
		catch (ReflectiveOperationException e)
		{
			throw new IllegalArgumentException("Could not create new instance of Projectile (" + projectileClass + "): " + e);
		}
	}

	/**
	 * String representation of the projectile
	 * @return the projectile meta-infos as string
	 */
	public String toString()
	{
		return this.hashCode() + " " + this.getClass().getSimpleName() + "(speed: " + this.speed + ", damage:" + this.damage + ")";
	}

}
