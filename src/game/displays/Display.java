package game.displays;

/**
 * This class allows for the display of the game state.
 */
public abstract class Display
{
	/**
	 * Create a new display instance
	 */
	public Display() {}

	/**
	 * Display the current state of the game.
	 */
	public abstract void display();

	/**
	 * Update the display state.
	 */
	public abstract void repaint();

	/**
	 * Terminate the display.
	 */
	public abstract void close();

    //    _
    //   /_|
    //  ('_)<|
    //   \_|
}
