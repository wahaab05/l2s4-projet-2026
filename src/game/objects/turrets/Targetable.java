package game.objects.turrets;

import game.objects.projectiles.Projectile;
import utils.plane2d.Vector2f;

/**
 * All game objects that can be targeted by a projectile
 * Bloons, Turrets, Fishes...
 * 
 * Max   /\
 *     _/./
 *  ,-'    `-:..-'/
 * : o )      _  (
 * "`-....,--; `-.\
 *     `'
 */
public interface Targetable {
	
	/**
	 * Return the direction vector to go for the projectile to reach the current object
	 * @param p a projectile moving toward the current object
	 * @return direction vector to go for the projectile
	 */
	public Vector2f directionFrom(Projectile p);

	/**
	 * Check if a given projectile is close enough from this targetable and it should start shooting.
	 * @param p Projectile to check
	 * @return True if the projectile can start shooting
	 */
	public boolean isInHitRangeOf(Projectile p);

	/**
	 * Check if a given projectile is in the hitbox of this targetable.
	 * @param p Projectile to check
	 * @return True if the projectile is within the hitbox
	 */
	public boolean isCollidingWith(Projectile p);
}
