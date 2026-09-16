package game.actions;

/**
 * Represents an action the player can perform
 * A fish cannot perform an action
 */
public abstract class Action {

	public Action() {}

	/**
	 * Checks if the action can be executed in the current game state
	 * @return can this action be executed right now
	 */
	public abstract boolean isExecutable();
	
	/**
	 * Actually perform the action
	 */
	public abstract void execAction();

}
