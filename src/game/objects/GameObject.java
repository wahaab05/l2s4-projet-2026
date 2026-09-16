package game.objects;

import utils.plane2d.Vector2f;

/**
 * Class representing dynamic game objects placed on the board.
 * Every class in 'objects' package should inherit this
 * Example of an object:
 * 				 O  o
 * 			_\_   o
 * 	     \\/  o\ .
 * 		 //\___=
 * 			''
 */
public abstract class GameObject
{
	/** coords of this entity */
	protected Vector2f coords;

	/**
	 * Create a new Game object.
	 * @param coords Coordinates of the game object
	 */
	public GameObject(Vector2f coords)
	{
		this.coords = coords;
	}


	/**
	 * Get the coordinates of this game object.
	 * @return the coordinates
	 */
	public Vector2f getCoords()
	{ return this.coords; }


	/**
	 * Change this game object's state from the current one to the next tick
	 */
	abstract public void tick();
}
