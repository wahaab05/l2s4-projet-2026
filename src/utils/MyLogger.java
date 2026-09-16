package utils;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import game.Game;

public class MyLogger {

	private static Logger instance;

	static {
		Level lvl = Level.FINE;
		instance = Logger.getLogger("Projet FROMAGE");
		instance.setLevel(lvl);
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(lvl);
		handler.setFormatter(new Formatter() {
			@Override
			public String format(LogRecord record) {
				String offset = "";
				String fish = "";
				Level currentLevel = record.getLevel();
				if (currentLevel != Level.INFO)
					fish = "🐟";
				if (currentLevel == Level.FINE)
					offset = "\t";
				if (currentLevel == Level.FINER)
					offset = "\t\t";
				if (currentLevel == Level.FINEST)
					offset = "\t\t\t";
				return currentLevel.toString() + " " + fish +
						" " + String.format("[t=%.2fs | tick=%d]", Game.instance().getElapsedSeconds(), Game.instance().getTickCount()) +
						": " + offset +
						record.getMessage() + "\n";
			}
		});
		instance.setUseParentHandlers(false);
		instance.addHandler(handler);
	}

	/**
	 * Get the static instance of logger
	 * @return the logger object
	 */
	public static Logger getInstance()
	{ return instance; }
}
