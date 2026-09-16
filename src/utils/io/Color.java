package utils.io;

/**
 * Color enum with ansii codes for console
 */
public enum Color {

	/** No color, ansii code resets print color
	 */
	none("\033[0m"),

	/** Black ansii code
	 */
	black("\033[0;30m"),

	/** Blue ansii code
	 */
	blue("\033[0;34m"),

	/** Red ansii code
	 */
	red("\033[0;31m"),

	/** Yellow ansii code
	 */
	yellow("\033[0;33m"),

	/** Cyan ansii code
	 */
	cyan("\033[0;36m");

	private String code;

	private Color(String code)
	{
		this.code = code;
	}

	/** Get the color's ansii code
	 * @return the ansii code
	 */
	public String getCode(){return this.code;}
};
