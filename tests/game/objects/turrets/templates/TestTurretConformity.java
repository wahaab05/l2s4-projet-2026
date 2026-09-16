package game.objects.turrets.templates;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import game.cells.TurretCell;
import game.objects.turrets.Turret;
import game.upgrade.UpgradeableAttribute;
import utils.datastructures.Tuple2;

public class TestTurretConformity {

    private TurretCell cell()
    {
        return new TurretCell(0, 0);
    }

    private UpgradeableAttribute<?> upgradeNamed(Turret turret, String name)
    {
        List<Tuple2<String, UpgradeableAttribute<?>>> upgrades = turret.getAllUpgradeableAttributes();

        return upgrades.stream()
            .filter(upgrade -> upgrade.first().equals(name))
            .findFirst()
            .orElseThrow(() -> new AssertionError("Missing upgrade: " + name))
            .second();
    }

    @Test
    public void subjectTowerCostsAreCorrect()
    {
        assertEquals(200, new DartMonkey(cell()).getInitialCost());
        assertEquals(350, new NeedleMonkey(cell()).getInitialCost());
        assertEquals(500, new SniperMonkey(cell()).getInitialCost());
        assertEquals(600, new CanonMonkey(cell()).getInitialCost());
        assertEquals(1200, new JunkyMonkey(cell()).getInitialCost());
        assertEquals(400, new FreezeMonkey(cell()).getInitialCost());
        assertEquals(500, new SlowMonkey(cell()).getInitialCost());
    }

    @Test
    public void freezeAndSlowMonkeysHaveNoUpgrades()
    {
        FreezeMonkey freezeMonkey = new FreezeMonkey(cell());
        SlowMonkey slowMonkey = new SlowMonkey(cell());

        assertTrue(freezeMonkey.getAllUpgradeableAttributes().isEmpty());
        assertTrue(slowMonkey.getAllUpgradeableAttributes().isEmpty());

        assertFalse(freezeMonkey.canBeUpgraded());
        assertFalse(slowMonkey.canBeUpgraded());
    }

    @Test
    public void dartMonkeyUpgradeCostsMatchSubject()
    {
        DartMonkey dartMonkey = new DartMonkey(cell());

        assertEquals(3, dartMonkey.getAllUpgradeableAttributes().size());
        assertEquals(100, upgradeNamed(dartMonkey, "RANGE").getCost());
        assertEquals(150, upgradeNamed(dartMonkey, "SHOOTING_COOLDOWN").getCost());
        assertEquals(250, upgradeNamed(dartMonkey, "PROJECTILE_TYPE").getCost());
    }

    @Test
    public void needleMonkeyUpgradeCostsMatchSubject()
    {
        NeedleMonkey needleMonkey = new NeedleMonkey(cell());

        assertEquals(2, needleMonkey.getAllUpgradeableAttributes().size());
        assertEquals(150, upgradeNamed(needleMonkey, "RANGE").getCost());
        assertEquals(200, upgradeNamed(needleMonkey, "SHOOTING_COOLDOWN").getCost());
    }

    @Test
    public void canonMonkeyHasRangeCadenceAndProjectileUpgrade()
    {
        CanonMonkey canonMonkey = new CanonMonkey(cell());

        assertEquals(3, canonMonkey.getAllUpgradeableAttributes().size());
        assertEquals(250, upgradeNamed(canonMonkey, "RANGE").getCost());
        assertEquals(300, upgradeNamed(canonMonkey, "SHOOTING_COOLDOWN").getCost());
        assertEquals(400, upgradeNamed(canonMonkey, "PROJECTILE_TYPE").getCost());
    }

    @Test
    public void junkyMonkeyUpgradeCostsMatchSubject()
    {
        JunkyMonkey junkyMonkey = new JunkyMonkey(cell());

        assertEquals(2, junkyMonkey.getAllUpgradeableAttributes().size());
        assertEquals(400, upgradeNamed(junkyMonkey, "RANGE").getCost());
        assertEquals(1000, upgradeNamed(junkyMonkey, "SHOOTING_COOLDOWN").getCost());
    }
}