package game;

/**
 * Class representing a player identified by a name.
 * Each player has its own reserve of money
 */
public class Player {

	private int money;
	private String name;

	/**
	 * Create a new player with a given name and an initial amount of money
	 * @param name Name of player
	 * @param initialMoney Amount of money the player starts with
	 */
	public Player(String name, int initialMoney)
	{
		this.name = name;
		this.money = initialMoney;
	}



	/* ---------------- *\
	 *					*
	 * 		GETTERS		*
	 *					*
	\* ---------------- */

	/**
	 * Get the player's name 🐟
	 * @return the name
	 */
	public String getName()
	{ return this.name; }

	/**
	 * Get the player's amount of money
	 * @return amount of money
	 */
	public int getMoney()
	{  return this.money; }

}
