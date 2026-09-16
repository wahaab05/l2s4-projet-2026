package game.actions;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.*;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import game.Game;
import game.shops.Shop;
import game.upgrade.UpgradeableAttribute;
import utils.config.ConfigReader;
import utils.coordsChooser.InteractiveCoordsChooser;
import utils.datastructures.Tuple2;
import utils.listchooser.InteractiveListChooser;
import utils.listchooser.InteractiveListChooserNoNewInstance;
import utils.maths.RandomUtils;
import utils.plane2d.Direction;
import game.actions.templates.ActionDowngradeTurret;
import game.actions.templates.ActionSellTurret;
import game.actions.templates.ActionUpgradeTurret;
import game.actions.templates.turrets.ActionBuyNeedleMonkeyTurret;
import game.boards.Board;
import game.boards.DefaultBoard;
import game.objects.turrets.Turret;
import game.objects.turrets.templates.DartMonkey;
import game.objects.turrets.templates.NeedleMonkey;

public class TestActions {

	private static boolean manualTest = ConfigReader.getBool("interactive_menu_test");
	private static InputStream systemIn;

	private Game game;
	private Shop shop;
	private Board board;

	private int creditsBeforeAction;



	/**
	 * Save the "real" system in so it can be restored later
	 */
	@BeforeClass
	public static void saveSystemIn()
	{
		if (manualTest)
			systemIn = System.in;
	}
	/**
	 * Restore "real" system in after all tests are done
	 */
	@AfterClass
	public static void restoreSystemIn()
	{
		if (manualTest)
			System.setIn(systemIn);
	}
	/**
	 * Simulate an input "as stdin"
	 * @param input text to be given as an input
	 */
	public void simulateInput(String input)
	{
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
	}


	/**
	 *
	 * 		ACTUAL TESTS
	 *
	 */

	@BeforeEach
	public void before() // 𓆞
	{
		RandomUtils.setSeed(50); // generate the exact same board each time
		Game.setInstance(1, new DefaultBoard(10, 10, Direction.RIGHT), new Shop());
		game = Game.instance();
		shop = game.getShop();
		board = game.getBoard();

		creditsBeforeAction = shop.getCredits();
	}

	@Test
	public void testActionBuyTurret()
	{
		if (!manualTest)
			simulateInput("0\n5\n");

		int turretCost = shop.getTurretCost(NeedleMonkey.class);

		System.out.println("/!\\ REQUIRED: Please select NeedleMonkey and coords x:0 y:5");

		Action a = new ActionBuyNeedleMonkeyTurret(new InteractiveCoordsChooser());
		a.execAction();

		// In this context, board should contain a single turret (the one just bought) therfore the turret 0 should be NeedleMonkey x:0 y:5
		assertInstanceOf(NeedleMonkey.class, board.getTurrets().getFirst()); // turret was successfully added to board
		// turrets pos is in the middle of it's cell (+0.5)
		assertEquals(0.5, board.getTurrets().get(0).getCoords().x()); // turret coords are right
		assertEquals(5.5, board.getTurrets().get(0).getCoords().y());

		assertEquals(creditsBeforeAction-turretCost, shop.getCredits()); // credits were decreased accordingly
	}

	@Test
	public void testActionSellTurret()
	{
		if (!manualTest)
			simulateInput("1\n");

		/* buy a turret  */
		shop.buyTurret( // DartMonkey is arbitraty
			board, DartMonkey.class, new Tuple2<Integer,Integer>(1, 1)
		);
		assertEquals(1, board.getTurrets().size());
		assertEquals(shop.getCredits(), creditsBeforeAction - shop.getTurretCost(DartMonkey.class));

		Action a = new ActionSellTurret(new InteractiveListChooser<>());
		a.execAction();

		int dartCost = shop.getTurretCost(DartMonkey.class);
		int expectedRefund = (int)(dartCost * 0.8f);

		assertEquals(0, board.getTurrets().size());
		assertEquals(creditsBeforeAction - dartCost + expectedRefund, shop.getCredits());
	}

	@Test
	public void testActionUpgradeTurret()
	{
		if (!manualTest)
			simulateInput("1\n1\n");

		shop.buyTurret(board, DartMonkey.class, new Tuple2<Integer,Integer>(1, 1));
		Turret t = board.getTurrets().getFirst();
		assertInstanceOf(DartMonkey.class, t);

		assertTrue(t.canBeUpgraded());
		// get the first upgradable attribute of this turret
		UpgradeableAttribute<?> attribute = t.getAllUpgradeableAttributes().getFirst().second();
		assertTrue(attribute.canBeUpgraded());
		// initial upgrade level should be 0
		assertEquals(0, attribute.getLevel());

		System.out.println("/!\\ REQUIRED: Please select the first available upgrade");
		Action a = new ActionUpgradeTurret(new InteractiveListChooser<>(), new InteractiveListChooserNoNewInstance<>());
		a.execAction();

		assertEquals(1, attribute.getLevel());
	}

	@Test
	public void testActionDowngradeTurret()
	{
		if (!manualTest)
			simulateInput("1\n1\n");

		shop.buyTurret(board, DartMonkey.class, new Tuple2<Integer,Integer>(1, 1));
		Turret t = board.getTurrets().getFirst();
		assertInstanceOf(DartMonkey.class, t);

		// upgrade first attribute once (so it can be downgraded)
		t.getAllUpgradeableAttributes().getFirst().second().upgrade();

		assertTrue(t.canBeDowngraded());
		// get the first upgradable attribute of this turret
		UpgradeableAttribute<?> attribute = t.getAllUpgradeableAttributes().getFirst().second();
		assertTrue(attribute.canBeDowngraded());
		// one upgrade was performed already so level should be 1
		assertEquals(1, attribute.getLevel());

		System.out.println("/!\\ REQUIRED: Please select the first available upgrade");
		Action a = new ActionDowngradeTurret(new InteractiveListChooser<>(), new InteractiveListChooserNoNewInstance<>());
		a.execAction();

		assertEquals(0, attribute.getLevel());
	}
}
