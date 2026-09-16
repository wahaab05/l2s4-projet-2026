package utils.config;

import org.junit.jupiter.api.*;

import utils.maths.Comparator;

import static org.junit.jupiter.api.Assertions.*;

public class TestConfigReader {

	@Test
	public void testParamRetreive()
	{
		// String value
		assertEquals("azerty1234-!&.,", ConfigReader.get("debug"));
		// a shark is not a tropical fish !
		assertNotEquals("🦈", ConfigReader.get("tropicalfish"));
		// int value
		assertThrows(NumberFormatException.class, () -> { ConfigReader.getInt("debug");});
		assertEquals(1354648413, ConfigReader.getInt("debugint"));
		// float value
		assertThrows(NumberFormatException.class, () -> { ConfigReader.getFloat("debug");});
		assertEquals(1354648413, ConfigReader.getFloat("debugint"));
		assertTrue(
			Comparator.compFloat(3.1415926, ConfigReader.getFloat("debugfloat")) == 0
		);
	}
}
