package game;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import game.boards.Board;
import game.displays.Display;
import game.objects.bloons.Bloon;
import game.objects.turrets.Turret;
import game.roads.Road;
import game.shops.Shop;
import game.waves.Wave;
import game.waves.WaveGenerator;
import utils.MyLogger;

/**
 * Class containing information on the state of the game.
 * This is the core class orchestrating interactions between components.
 */
public class Game {

	private static Game instance;

	public static void setInstance(int initialLife, Board board, Shop shop)
	{ Game.instance = new Game(initialLife, board, shop); }

	public static Game instance()
	{ return Game.instance; }

	/* ============================	*\
	 * 			Instance			*
	 *        .						*
	 *       ":"					*
	 *     ___:____     |"\/"|		*
	 *   ,'        `.    \  /		*
	 *   |  O        \___/  |		*
	 * ~^~^~^~^~^~^~^~^~^~^~^~^~	*
	\* ============================ */

	private int life;
	private List<Player> players;
	private Board board;
	private List<Display> displays;
	private Shop shop;
	private int nbWave; // Number of current wave
	private WaveGenerator waveGenerator;
	private Wave lastGeneratedWave;

	/**
	 * Actual time (in seconds) took to compute last tick, including wait period.
	 */
	private double deltaT;

	/**
	 * Goal tick frequency (in Hz) to compute ticks
	 */
	private int goalTickRate = 60;

	/**
	 * Time at which the current tick has started (in nanoseconds, perhaps overkill)
	 */
	private long waveLoopStartTime;

	/*
	 * Number of ticks already executed since the start of the current game.
	 * Used to display explicit time information in livrable event logs.
	 */
	private long tickCount;

	/**
	 * Create a new game instance
	 * @param initialLife Life points at the start of the game
	 * @param board Game board to play on
	 * @throws IllegalArgumentException If {@code initialLife} &le; 0
	 */
	private Game(int initialLife, Board board, Shop shop) throws IllegalArgumentException
	{
		if(initialLife <= 0)
			throw new IllegalArgumentException("Initial Game life cannot be less or equal to 0");

		this.life = initialLife;
		this.board = board;
		this.shop = shop;

		this.nbWave = 0;
		this.waveGenerator = null;
		this.lastGeneratedWave = null;
		this.displays = new ArrayList<>();
		this.players = new ArrayList<>();
		this.tickCount = 0;
	}






	/* ---------------- *\
	 *					*
	 *		GETTERS		*
	 *					*
	\* ---------------- */




	/**
	 * Get the amount of life points left
	 * @return life points
	 */
	public int getLife()
	{ return this.life; }




	/**
	 * Get the list of players in this game
	 * @return life of players
	 */
	public List<Player> getPlayers()
	{ return this.players; }

	/**
	 * Get the board on which this game is played
	 * @return board
	 */
	public Board getBoard()
	{ return this.board; }

	/**
	 * Get the list of all displays this game uses.
	 * @return list of displays
	 */
	public List<Display> getDisplays()
	{ return this.displays; }

	/**
	 * Get the shop object
	 * @return shop
	 */
	public Shop getShop()
	{ return this.shop; }
	/**
	 * Get this game's goal tick rate.
	 * The actual running tick rate is not guaranteed to be the same.
	 * @return the goal tick rate
	 */
	public int getGoalTickRate()
	{ return this.goalTickRate; }

	/**
	 * Get the time took for the last tick to compute.
	 * Result unit is seconds.
	 * The result includes the sleep period between each tick, if a tick happens
	 * to take less time to compute than its goal time.
	 *	       .
	 *	\_____)\_____
	 *	/--v____ __`
	 *	       )/
	 *	       '
	 * @return delta t
	 */
	public double getDeltaT()
	{ return this.deltaT; }


	/**
	 * Get the current wave number.
	 * @return wave number.
	 */
	public int getNbWave()
	{ return this.nbWave; }



	/**
	 * Get the wave generator used by this game.
	 * @return the wave generator, or {@code null} if none was configured.
	 */
	public WaveGenerator getWaveGenerator()
	{ return this.waveGenerator; }



	/**
	 * Get the most recently generated wave.
	 * @return last generated wave, or {@code null} if no wave was generated yet.
	 */
	public Wave getLastGeneratedWave()
	{ return this.lastGeneratedWave; }






	/* ---------------- *\
	 *					*
	 *		METHODS		*
	 *					*
	\* ---------------- */



	/**
	 * Start the main game loop.
	 */
	public void startWave()
	{
		Logger logger = MyLogger.getInstance();
		this.waveLoopStartTime = System.nanoTime();
		this.nbWave++;
		int ticks = 0;

		logger.fine("Starting wave number " + this.nbWave);

		// Initial display
		for(Display d : this.displays)
			d.display();

		while(!this.hasWaveEnded())
		{
			logger.finer("Start tick nb " + ticks);

			if(ticks % 20 == 0)
			{
				// Update display states
				for(Display d : this.displays)
					// d.repaint();
					d.display();

				logger.finer("Game was displayed");
			}

			this.tick();

			ticks++;
			this.waitUntilNextTick();
		}

		long elapsed = (System.nanoTime() - this.waveLoopStartTime) / (long)1e9;
		System.out.println("\n==========================================");
		System.out.println("\nWave "+this.nbWave+" over! Lasted " + elapsed + " seconds\n");
		System.out.println("==========================================\n");

		// ConsoleUtils.clearConsole();
		for(Display d : this.displays)
			d.display();
	}


	/**
	 * Configure the wave generator used by this game.
	 * @param waveGenerator generator to use for future waves
	 */
	public void setWaveGenerator(WaveGenerator waveGenerator)
	{ this.waveGenerator = waveGenerator; }


	/**
	 * Generate and spawn the next wave on the current board.
	 *
	 * The returned wave only describes the composition that has just been spawned.
	 * The actual execution of the wave is still handled by {@link #startWave()}.
	 *
	 * @return generated wave descriptor
	 * @throws IllegalStateException if no wave generator has been configured.
	 */
	public Wave generateNextWave()
	{
		if(this.waveGenerator == null)
			throw new IllegalStateException("No WaveGenerator configured for this game");

		this.lastGeneratedWave = this.waveGenerator.generate(this.nbWave + 1, this.board);
		this.lastGeneratedWave.spawnOn(this.board);
		return this.lastGeneratedWave;
	}


	/**
	 * Make this game change state into the next tick.
	 */
	public void tick()
	{
		/* Increment the tick count after each tick */
		this.tickCount++;
		Logger logger = MyLogger.getInstance();
		for(Road r : this.board.getRoads())
		{
			logger.finer("Process road " + r.toString());
			Iterator<Bloon> bloonsIt = r.getBloons().iterator();
			Bloon b;
			logger.finer("About to start ticking bloons");
			while(bloonsIt.hasNext())
			{
				b = bloonsIt.next();

				if(!b.isOutOfBoard())
					b.tick();
			}

			bloonsIt = r.getExitedBloons().iterator();
			while(bloonsIt.hasNext())
			{
				b = bloonsIt.next();
				this.life -= 1;
				bloonsIt.remove();
				MyLogger.getInstance().info("Bloon left the board: " + b.toString()); /* show event with elapsed time */
			}
		}

		logger.finer("About to start ticking turrets");
		for(Turret t : this.board.getTurrets())
			t.tick();
	}





	/**
	 * Check tick timing error, runs for a fixed number of seconds.
	 * Prints results in the console.
	 */
	@SuppressWarnings("unused")
	private void benchmarkTiming()
	{
		long start = System.nanoTime();
		int benchmarkSeconds = 120;

		int ticks = 0;
		int secs = -1;

		this.waveLoopStartTime = System.nanoTime();

		while(!this.hasGameEnded())
		{
			if(ticks%60 == 0)
				secs++;

			System.out.print("\rBenchmark: Tick "
					+ ticks + " ; "
					+ secs + " / " + benchmarkSeconds + " seconds");

			if(secs == benchmarkSeconds)
				break;

			this.waitUntilNextTick();
			ticks++;
		}

		long end = System.nanoTime();
		long duration = end - start;

		long diff = duration - (long)(benchmarkSeconds * 1e9);
		double diffPercent = diff / 1e9 / benchmarkSeconds * 100.;

		int goalTicks = this.goalTickRate * benchmarkSeconds;
		int diffTicks = ticks - goalTicks;

		System.out.println("\nResult: ");
		System.out.println("	Goal time	: " + benchmarkSeconds + " s");
		System.out.println("	Real time	: " + (double)(duration / 1.e9) + " s");
		System.out.println("	Error		: " + diff + " ns <=> " + (diff / 1.e9) + " s");
		System.out.println("	Error ratio : " + diffPercent + " %");
		System.out.println("	Total ticks : " + ticks);
		System.out.println("	Goal ticks	: " + goalTicks);
		System.out.println("	Diff ticks	: " + diffTicks);
	}





	/**
	 * Check if this game has ended
	 * @return True if game is finished
	 */
	public boolean hasGameEnded()
	{ return this.getLife() <= 0; }

	/**
	 * Check if the current wave has ended.
	 * @return True if the current wave has ended
	 */
	public boolean hasWaveEnded()
	{ return !this.board.hasBloonsLeft() || this.hasGameEnded(); }

	/**
	 * Add a player to this game
	 * @param p Player to add
	 */
	public void addPlayer(Player p)
	{
		this.players.add(p);
	}





	/**
	 * Create a display of a given type for this game.
	 * @param <T> Type of display to create
	 * @param DC Display's Class
	 * @return The newly created display
	 * @throws IllegalArgumentException if no constructor {@code public DC(Game)} exists
	 *
	 * Multiple displays of the same type can be added to the game. However there is no guarantee
	 * that they are able to coexist nicely, as it depends on their individual implementations.
	 */
	public <T extends Display> T addDisplay(Class<T> DC) throws IllegalArgumentException
	{
		T d = null;

		try {
			Constructor<T> C = DC.getConstructor();

			d = C.newInstance();
			this.addDisplay(d);
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"Could not create a new instance of Display(Game):\n"+e
			);
		}

		return d;
	}

	/**
	 * Add an existing display to the list of this game's displays.
	 *
	 * @param d Display to add
	 * @throws IllegalArgumentException if the display is NULL
	 */
	public void addDisplay(Display d) throws IllegalArgumentException
	{
		if(d == null)
			throw new IllegalArgumentException("Given display cannot be null !");

		this.displays.add(d);
	}



	/**
	 * Remove all displays of a given type from this game.
	 * @param DC Display Class to remove all instances of
	 * @param <T> Type of Display
	 * @return True if the game contained displays of this type
	 */
	public <T extends Display> boolean removeDisplay(Class<T> DC)
	{
		boolean has_removed = false;

		Iterator<Display> it = this.displays.iterator();
		Display d = null;
		while(it.hasNext())
		{
			d = it.next();
			if(DC.isInstance(d))
			{
				/*
				 * Since remove() is an optional operation and it
				 * returns void, there is no actual way of testing
				 * if the operation was successful except by iterating
				 * on the displays again.
				 * I consider that remove() always succeeds.
				 */
				d.close();
				it.remove();
				has_removed = true;
			}
		}

		return has_removed;
	}





	/**
	 * Remove a specified instance of Display from this game.
	 * @param d Display to remove
	 * @return True if the display was removed
	 */
	public boolean removeDisplay(Display d)
	{
		d.close();
		return this.displays.remove(d);
	}






	/**
	 * This method puts the main thread to sleep until the next tick must start.
	 * {@link #deltaT} is updated with the correct timing information.
	 *
	 * <bold>This function waits until the beginning of the next fixed tick.</bold> <br>
	 * <bold>If a tick takes longer than 1/tickrate to compute, this function will still wait until the next fixed tick start.</bold>
	 */
	private void waitUntilNextTick()
	{
		long nowNs = System.nanoTime();

		// Time a tick should ideally take to compute
		long goalTimeNs = (long)1e9 / this.goalTickRate;

		/*
		 * Time difference between the closest previous tick start and the current time.
		 *
		 * /!\
		 * If a tick takes more than 1/tickrate time to compute, elapsedNs will contain the time between
		 * now and the previous fixed interval tick start.
		 * ! Not the time between the start of the current game tick's computation and now. !
		 * /!\
		 *
		 */
		long elapsedNs = (nowNs - this.waveLoopStartTime) % goalTimeNs;

		// Get the total time left in the tick duration, in Ns
		long totalSleepNs = (goalTimeNs - elapsedNs);

		// Get the time left in Ms and Ns (sleepMs * 10e6 + sleepNs = totalSleepNs)
		int sleepMs = (int)(totalSleepNs / 1e6);
		int sleepNs = (int)(totalSleepNs % 1e6);

		if(elapsedNs < goalTimeNs)
		{
			try {
				Thread.sleep(sleepMs, sleepNs);
			} catch (Exception e) {
				System.out.println("Could not put thread to sleep.");
				System.out.println("Calling Thread.sleep(");
				System.out.println("	" + sleepMs);
				System.out.println("	" + sleepNs);
				System.out.println(");\nError: " + e);
			}
		}

		nowNs = System.nanoTime();
		elapsedNs = nowNs - this.waveLoopStartTime;

		this.deltaT = elapsedNs / 1.e9;
	}






		/**
	 * Returns the number of game ticks already executed since the start
	 * of the current game
	 *
	 * A tick is one update step of the game loop. This value is useful
	 * to display precise temporal information in event messages.
	 *
	 * @return current tick count since the beginning of the game
	 */
	public long getTickCount()
	{
		return this.tickCount;
	}








	/**
	 * Returns the elapsed game time in seconds
	 *
	 * The value is computed from the number of executed ticks and the
	 * configured tick rate of the game. For example, with a tick rate
	 * of 60 Hz, 120 ticks correspond to 2 seconds
	 *
	 * This method is used to display readable event logs for the livrable.
	 *
	 * @return elapsed time in seconds since the game started
	 */
	public double getElapsedSeconds()
	{
		return this.tickCount / (double)this.goalTickRate;
	}
}
