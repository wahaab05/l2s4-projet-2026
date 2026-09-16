package game.shops;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game.Game;
import game.boards.Board;
import game.boards.DefaultBoard;
import game.objects.turrets.Turret;
import game.objects.turrets.templates.CanonMonkey;
import game.objects.turrets.templates.DartMonkey;
import game.objects.turrets.templates.FreezeMonkey;
import game.objects.turrets.templates.JunkyMonkey;
import game.objects.turrets.templates.NeedleMonkey;
import game.objects.turrets.templates.SlowMonkey;
import game.objects.turrets.templates.SniperMonkey;
import game.upgrade.UpgradeableAttribute;
import utils.datastructures.Tuple2;
import utils.maths.RandomUtils;
import utils.plane2d.Direction;

public class TestShop {

    private Board board;
    private Shop shop;
    private int initialCredits;

    @BeforeEach
    public void before()
    {
        RandomUtils.setSeed(50);
        this.board = new DefaultBoard(10, 10, Direction.RIGHT);
        this.shop = new Shop();
        Game.setInstance(20, this.board, this.shop);
        this.initialCredits = this.shop.getCredits();
    }

    @Test
    public void defaultShopStartsWithSubjectCredits()
    {
        assertEquals(2500, this.shop.getCredits());
    }

    @Test
    public void defaultShopContainsTheSevenRequiredTurrets()
    {
        assertEquals(7, this.shop.getCatalog().size());

        assertTrue(this.shop.getCatalog().contains(DartMonkey.class));
        assertTrue(this.shop.getCatalog().contains(NeedleMonkey.class));
        assertTrue(this.shop.getCatalog().contains(SniperMonkey.class));
        assertTrue(this.shop.getCatalog().contains(CanonMonkey.class));
        assertTrue(this.shop.getCatalog().contains(JunkyMonkey.class));
        assertTrue(this.shop.getCatalog().contains(FreezeMonkey.class));
        assertTrue(this.shop.getCatalog().contains(SlowMonkey.class));
    }

    @Test
    public void buyTurretPlacesTurretAndDebitsCredits()
    {
        int cost = this.shop.getTurretCost(DartMonkey.class);

        Turret turret = this.shop.buyTurret(
            this.board,
            DartMonkey.class,
            new Tuple2<Integer, Integer>(1, 1)
        );

        assertNotNull(turret);
        assertEquals(1, this.board.getTurrets().size());
        assertSame(turret, this.board.getTurrets().get(0));
        assertEquals(this.initialCredits - cost, this.shop.getCredits());
    }

    @Test
    public void sellTurretRemovesTurretAndRefundsEightyPercent()
    {
        int cost = this.shop.getTurretCost(DartMonkey.class);

        Turret turret = this.shop.buyTurret(
            this.board,
            DartMonkey.class,
            new Tuple2<Integer, Integer>(1, 1)
        );

        int refund = this.shop.sellTurret(turret);
        int expectedRefund = (int)(cost * 0.8f);

        assertEquals(expectedRefund, refund);
        assertEquals(0, this.board.getTurrets().size());
        assertEquals(this.initialCredits - cost + expectedRefund, this.shop.getCredits());
    }

    @Test
    public void buyUpgradeDebitsCreditsAndIncreasesUpgradeLevel()
    {
        Turret turret = this.shop.buyTurret(
            this.board,
            DartMonkey.class,
            new Tuple2<Integer, Integer>(1, 1)
        );

        UpgradeableAttribute<?> upgrade = turret.getAllUpgradeableAttributes().get(0).second();

        int creditsBeforeUpgrade = this.shop.getCredits();
        int upgradeCost = upgrade.getCost();

        this.shop.buyUpgrade(upgrade);

        assertEquals(1, upgrade.getLevel());
        assertEquals(creditsBeforeUpgrade - upgradeCost, this.shop.getCredits());
    }

    @Test
    public void sellUpgradeRefundsCreditsAndDecreasesUpgradeLevel()
    {
        Turret turret = this.shop.buyTurret(
            this.board,
            DartMonkey.class,
            new Tuple2<Integer, Integer>(1, 1)
        );

        UpgradeableAttribute<?> upgrade = turret.getAllUpgradeableAttributes().get(0).second();

        int creditsBeforeUpgrade = this.shop.getCredits();
        int upgradeCost = upgrade.getCost();

        this.shop.buyUpgrade(upgrade);

        int refund = this.shop.sellUpgrade(upgrade);
        int expectedRefund = (int)(upgradeCost * 0.8f);

        assertEquals(expectedRefund, refund);
        assertEquals(0, upgrade.getLevel());
        assertEquals(creditsBeforeUpgrade - upgradeCost + expectedRefund, this.shop.getCredits());
    }
}