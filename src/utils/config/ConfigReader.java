package utils.config;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Configuratoin file reader (config.properties)
 * Allows to easily fetch specific config data
 * Example:
 * {@code 
 * 	int age = ConfigReader.get("playerAge");
 *	// get the attribute "playerAge" from config file
 * }
 */
public class ConfigReader {
	
	/** properties reader object */
	private static final Properties properties = new Properties();
	/** Initialize reader to config file */
	static
	{
		try
		{
			/* reads 'config.properties' at project root */
			properties.load(new FileInputStream("config.properties"));
		}
		catch (java.io.FileNotFoundException e)
		{
			System.out.println("Le fichier 'config.properties' contenant les données relatives aux éléments de jeu (tourelles, projectiles, upgrades..) est indispensable à l'execution. Veuillez vérifier la présence et le nom de celui-ci.");
			System.exit(2);
		}
		catch (java.io.IOException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Private constructor for static class
	 */
	private ConfigReader() {}

	/**
	 * Return the string associated with given property or null if this property is unknown
	 * @param propertyName a property name
	 * @return property value if exists, null else
	 */
	public static String get(String propertyName)
	{
		return (String) properties.getOrDefault(propertyName, null);
	}

	/**
	 * Get an integer property
	 * @param propertyName property name
	 * @return the property value as an integer
	 * @throws NumberFormatException if the property value cannot be parsed to Integer
	 */
	public static int getInt(String propertyName) throws NumberFormatException
	{
		String propertyValue = ConfigReader.get(propertyName);
		Integer intPropertyValue = null;
		try
		{
			intPropertyValue = Integer.parseInt(propertyValue);
		} catch (NumberFormatException e)
		{
			System.err.println("Bad property for type 'int': " + propertyValue + " for property name: " + propertyName);
			throw new NumberFormatException(e.getMessage());
		}
		return intPropertyValue;
	}

	/**
	 * Get a float property
	 * @param propertyName property name
	 * @return the property value as a float
	 * @throws NumberFormatException if the property value cannot be parsed to Float
	 */
	public static float getFloat(String propertyName) throws NumberFormatException
	{
		String propertyValue = ConfigReader.get(propertyName);
		Float floatPropertyValue = null;
		try
		{
			floatPropertyValue = Float.parseFloat(propertyValue);
		} catch (NumberFormatException e)
		{
			System.err.println("Bad property for type 'float': " + propertyValue + " for property name: " + propertyName);
			throw new NumberFormatException(e.getMessage());
		}
		return floatPropertyValue;
	}

	/**
	 * Get an boolean property
	 * If the property value is not equal to "true" (not case sensitive),
	 * then the result will be false,
	 * including for "null" value
	 * @param propertyName property name
	 * @return the property value as a boolean
	 */
	public static boolean getBool(String propertyName)
	{
		String propertyValue = ConfigReader.get(propertyName);
		Boolean boolPropertyValue = null;
		boolPropertyValue = Boolean.parseBoolean(propertyValue);
		return boolPropertyValue;
	}

	public static String getFish()
	{
		return "🐟";
	}
}
