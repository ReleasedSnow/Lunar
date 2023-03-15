package me.releasedsnow.com.lunar;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.WaterAbility;
import com.projectkorra.projectkorra.util.DamageHandler;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.Vector;

import java.util.List;

public class CosmicCollide extends WaterAbility implements AddonAbility {

    Listener listener;

    public static final long COOLDOWN = 6000;

    Permission permission;

    public CosmicCollide(Player player) {
        super(player);

        if (!bPlayer.canBend(this)) return;

        start();
    }

    @Override
    public void progress() {
        Vector direction = player.getLocation().getDirection();


        if (!bPlayer.canBendIgnoreBindsCooldowns(this)) {
            remove();
            return;
        }


        if (player.isSneaking()) {
            long duration = 1500;
            long running = System.currentTimeMillis() - getStartTime();
            double step = 0.5;
            double radius = 0.7;
            Location origin = player.getEyeLocation();
            for (double i = 0; i <10; i += step) {
                origin = origin.add(origin.getDirection().multiply(step));
                List<Entity> affected = GeneralMethods.getEntitiesAroundPoint(origin, radius);
                for (Entity entity : affected) {
                    if (entity instanceof LivingEntity living) {
                        if (entity.getUniqueId() != player.getUniqueId()) {
                            player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, living.getLocation(), 1, 0.2, 0.1, 0.2, 0.1);


                            if (running >= duration) {
                                player.teleport(living.getLocation());
                                player.swingMainHand();
                                DamageHandler.damageEntity(living, 3, this);
                                player.getWorld().spawnParticle(Particle.EXPLOSION_LARGE, living.getLocation(), 1);
                                player.playSound(living.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 4, 3);
                                living.setVelocity(direction.multiply(3));
                                bPlayer.addCooldown(this);
                                remove();
                                return;
                            }
                            }
                        }

                    }
                }
            } else {
            remove();

        }
        }












    @Override
    public boolean isSneakAbility() {
        return true;
    }

    @Override
    public Element getElement() {
        return Lunar.ele();
    }

    @Override
    public boolean isHarmlessAbility() {
        return false;
    }

    @Override
    public long getCooldown() {
        return COOLDOWN;
    }

    @Override
    public String getName() {
        return "CosmicCollide";
    }

    @Override
    public Location getLocation() {
        return player.getLocation();
    }

    @Override
    public void load() {
        this.listener = new AbilityListener();
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(listener, ProjectKorra.plugin);
        permission = new Permission("bending.ability.cosmiccollide");
        permission.setDefault(PermissionDefault.OP);
        ProjectKorra.plugin.getServer().getPluginManager().addPermission(permission);

    }

    @Override
    public void stop() {

        HandlerList.unregisterAll(listener);
        ProjectKorra.plugin.getServer().getPluginManager().removePermission(permission);

    }

    @Override
    public String getAuthor() {
        return "ReleasedSnow";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

}
