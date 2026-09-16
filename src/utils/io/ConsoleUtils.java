package utils.io;

/**
 * A static utility class for console output
 */
public class ConsoleUtils
{
	private ConsoleUtils(){} // No doc warning and no instance

	/**
	 * Move the console's cursor to the top left
	 */
	public static void cursorToTopLeft()
	{
		cursorTo(0, 0);
	}

	/**
	 * Clear the console and move cursor to top left.
	 */
	public static void clearConsole()
	{
		System.out.print("\033[H\033[2J");
	}

	/**
	 * Hide the terminal cursor
	 */
	public static void hideCursor()
	{
		System.out.print("\033[?25l");
	}

	/**
	 * Show the terminal cursor
	 */
	public static void showCursor()
	{
		System.out.print("\033[?25h");
	}

	/** Move the console's cursor to a given position
	 * @param row Row to move the cursor to
	 * @param column Column to move the cursor to
	 * @throws IllegalArgumentException if position is negative
	 */
	public static void cursorTo(int row, int column)
	{
		if(row < 0 || column < 0)
			throw new IllegalArgumentException("Cursor position needs to be >= 0");

		// https://stackoverflow.com/a/1001368
		char escCode = 0x1B;
		System.out.print(String.format("%c[%d;%df",escCode,row,column));
	}

	/** Print in console with a color output. With Newline
	 * @param output String to print
	 * @param col Color of output
	 */
	public static void colorPrintLn(String output, Color col)
	{
		colorPrint(output+"\n", col);
	}

	/** Print in console with a color output. Without Newline
	 * @param output String to print
	 * @param col Color of output
	 */
	public static void colorPrint(String output, Color col)
	{
		System.out.print(colorString(output, col));
	}

	/** Get a string fitted with ansii codes destined to be printed with color
	 * @param output String to which color is added
	 * @param col Color to add
	 * @return String with color codes
	 */
	public static String colorString(String output, Color col)
	{
		return col.getCode() + output + Color.none.getCode();
	}

	/**
	 * Flush the stdout buffer.
	 */
	public static void flushBuffer()
	{
		System.out.flush();
	}
}
