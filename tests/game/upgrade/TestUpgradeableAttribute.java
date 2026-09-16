package game.upgrade;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import game.objects.projectiles.Projectile;
import game.objects.projectiles.templates.DartProjectile;
import game.objects.projectiles.templates.SharpDartProjectile;
import game.objects.projectiles.templates.VerySharpDartProjectile;

public class TestUpgradeableAttribute {

    @Test
    public void intUpgradeableAttributeCanUpgradeAndDowngrade()
    {
        IntUpgradeableAttribute attribute =
            new IntUpgradeableAttribute(100, 0.75f, 10, 150, 1f, 1);

        assertEquals(100, attribute.getValue());
        assertEquals(0, attribute.getLevel());
        assertEquals(150, attribute.getCost());

        attribute.upgrade();

        assertEquals(85, attribute.getValue());
        assertEquals(1, attribute.getLevel());
        assertFalse(attribute.canBeUpgraded());

        attribute.downgrade();

        assertEquals(100, attribute.getValue());
        assertEquals(0, attribute.getLevel());
    }

    @Test
    public void floatUpgradeableAttributeCanUpgradeAndDowngrade()
    {
        FloatUpgradeableAttribute attribute =
            new FloatUpgradeableAttribute(100f, 1.25f, 100, 1f, 1);

        assertEquals(100f, attribute.getValue(), 0.001f);

        attribute.upgrade();

        assertEquals(125f, attribute.getValue(), 0.001f);
        assertEquals(1, attribute.getLevel());

        attribute.downgrade();

        assertEquals(100f, attribute.getValue(), 0.001f);
        assertEquals(0, attribute.getLevel());
    }

    @Test
    public void cannotUpgradePastMaxLevel()
    {
        IntUpgradeableAttribute attribute =
            new IntUpgradeableAttribute(10, 5, 100, 1f, 1);

        attribute.upgrade();

        assertThrows(IllegalStateException.class, () -> {
            attribute.upgrade();
        });
    }

    @Test
    public void cannotDowngradeAtLevelZero()
    {
        IntUpgradeableAttribute attribute =
            new IntUpgradeableAttribute(10, 5, 100, 1f, 1);

        assertThrows(IllegalStateException.class, () -> {
            attribute.downgrade();
        });
    }

    @Test
    public void projectileUpgradeableAttributeFollowsProjectilePath()
    {
        ProjectileUpgradeableAttribute attribute =
            new ProjectileUpgradeableAttribute(DartProjectile.class, 250, 1f);

        assertEquals(DartProjectile.class, attribute.getValue());

        attribute.upgrade();

        assertEquals(SharpDartProjectile.class, attribute.getValue());

        attribute.upgrade();

        assertEquals(VerySharpDartProjectile.class, attribute.getValue());

        attribute.downgrade();

        assertEquals(SharpDartProjectile.class, attribute.getValue());
    }

    @Test
    public void projectileUpgradeableAttributeRejectsInvalidProjectile()
    {
        assertThrows(IllegalArgumentException.class, () -> {
            new ProjectileUpgradeableAttribute(Projectile.class, 250, 1f);
        });
    }
}