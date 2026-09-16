package game.objects.bloons.templates;

import game.objects.bloons.Bloon;
import game.roads.RoadTile;
import utils.config.ConfigReader;

/**
 * second weakest bloon with a life of 2 and a speed of 0.03 units/tick
 */
public class BlueBloon extends Bloon {

	private static final float SPEED = ConfigReader.getFloat("blue_bloon_speed");
	private static final int INITIAL_LIFE = ConfigReader.getInt("blue_bloon_initial_life");

	/**
	 * Create a new Blue Bloon on a given tile
	 * @param t Tile to place bloon on
	 */
	public BlueBloon(RoadTile t)
	{
		super(INITIAL_LIFE, SPEED, t);
	}
}
