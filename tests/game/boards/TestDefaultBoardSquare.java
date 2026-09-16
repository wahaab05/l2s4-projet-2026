package game.boards;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

/**
 * Test class for a default board with equal width and height
 */
@TestInstance(Lifecycle.PER_CLASS)
public class TestDefaultBoardSquare {

	private DefaultBoard board;
	private int dimension;

	@BeforeAll
	public void setup() {
		this.dimension = 5;
	}

	@BeforeEach
	public void before()
	{
		this.board = new DefaultBoard(this.dimension, this.dimension);
	}

	@Test
	public void testDefaultBoardCreation()
	{
		assertNotNull(board);
	}
	//        o                 o
	//                   o
	//          o   ______      o
	//            _/  (   \_
	//  _       _/  (       \_  O
	// | \_   _/  (   (    0  \
	// |== \_/  (   (          |
	// |=== _ (   (   (        |
	// |==_/ \_ (   (          |
	// |_/     \_ (   (    \__/
	//           \_ (      _/
	//             |  |___/
	//            /__/
	@Test
	public void testBoardDimensions()
	{
		assertEquals(this.dimension, board.getWidth());
		assertEquals(this.dimension, board.getHeight());
	}

	@Test
	public void testOneRoadIsCreated()
	{
		assertEquals(1, board.roads.size());
		assertNotNull(board.roads.get(0));
	}
}
