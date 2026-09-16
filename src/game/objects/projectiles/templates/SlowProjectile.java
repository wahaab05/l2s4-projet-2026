package game.objects.projectiles.templates;

import game.objects.bloons.Bloon;
import game.objects.projectiles.AreaProjectile;
import game.objects.turrets.Targetable;
import utils.config.ConfigReader;
import utils.plane2d.Vector2f;

/**
 * Projectile that slows all bloons in range.
 */
public class SlowProjectile extends AreaProjectile {

    private float slowFactor;
    private int durationTicks;

    private static final float SLOW_PROJECTILE_SPEED = ConfigReader.getFloat("slow_projectile_speed");
    private static final float SLOW_PROJECTILE_RANGE = ConfigReader.getFloat("slow_projectile_range");
    private static final float SLOW_PROJECTILE_FACTOR = ConfigReader.getFloat("slow_projectile_factor");
    private static final int SLOW_PROJECTILE_DURATION_TICKS = ConfigReader.getInt("slow_projectile_duration_ticks");

    public SlowProjectile(Vector2f coords, Targetable target) {
        super(
			"SlowProjectile",
            coords,
            target,
            SLOW_PROJECTILE_SPEED,
            SLOW_PROJECTILE_RANGE
        );
        // static definition of attributes related only to this projectile
        this.slowFactor = SLOW_PROJECTILE_FACTOR;
        this.durationTicks = SLOW_PROJECTILE_DURATION_TICKS;
    }

    protected void applyEffect(Bloon b) {
        b.applySlow(this.slowFactor, this.durationTicks);
    }

    // private void 🐟() {}
}
