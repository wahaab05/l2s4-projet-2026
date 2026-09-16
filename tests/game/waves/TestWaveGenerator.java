package game.waves;

import game.boards.Board;
import game.boards.DefaultBoard;
import org.junit.jupiter.api.Test;
import utils.plane2d.Direction;
import game.objects.bloons.templates.BlueBloon;
import game.objects.bloons.templates.GreenBloon;
import game.objects.bloons.templates.RedBloon;
import game.objects.bloons.templates.YellowBloon;
import static org.junit.jupiter.api.Assertions.*;

public class TestWaveGenerator {

	@Test
	public void generatedWavesGrowInVolumeAndUnlockStrongerBloons()
	{
		Board board = new DefaultBoard(10, 10, Direction.RIGHT);
		WaveGenerator generator = new WaveGenerator();

		Wave wave1 = generator.generate(1, board);
		Wave wave5 = generator.generate(5, board);

		assertTrue(wave5.getTotalBloons() > wave1.getTotalBloons());
		assertTrue(wave5.getThreatScore() > wave1.getThreatScore());
		assertTrue(wave5.isEliteWave());
		assertFalse(wave1.isEliteWave());
	}
	@Test
public void generatedWaveContainsAtLeastFifteenBloons()
{
    Board board = new DefaultBoard(10, 10, Direction.RIGHT);
    WaveGenerator generator = new WaveGenerator();

    Wave wave1 = generator.generate(1, board);

    assertTrue(wave1.getTotalBloons() >= 15);
}

	@Test
	public void firstWaveContainsOnlyRedBloons()
	{
			Board board = new DefaultBoard(10, 10, Direction.RIGHT);
			WaveGenerator generator = new WaveGenerator();

			Wave wave1 = generator.generate(1, board);

			assertTrue(wave1.getBloonCounts().containsKey(RedBloon.class));
			assertFalse(wave1.getBloonCounts().containsKey(BlueBloon.class));
			assertFalse(wave1.getBloonCounts().containsKey(GreenBloon.class));
			assertFalse(wave1.getBloonCounts().containsKey(YellowBloon.class));
			assertEquals(wave1.getTotalBloons(), wave1.getBloonCounts().get(RedBloon.class));
	}

	@Test
	public void spawnOnAddsAllWaveBloonsToBoard()
	{
			Board board = new DefaultBoard(10, 10, Direction.RIGHT);
			WaveGenerator generator = new WaveGenerator();

			Wave wave = generator.generate(3, board);
			int before = countBloonsOnBoard(board);

			wave.spawnOn(board);

			int after = countBloonsOnBoard(board);
			assertEquals(before + wave.getTotalBloons(), after);
	}

	private int countBloonsOnBoard(Board board)
	{
			int total = 0;

			for(int y = 0; y < board.getHeight(); y++)
					for(int x = 0; x < board.getWidth(); x++)
							total += board.getCell(x, y).getBloons().size();

			return total;
	}
}