package me.releasedsnow.com.lunar;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.FireAbility;
import com.projectkorra.projectkorra.util.DamageHandler;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;

public class SolarFlares extends FireAbility implements AddonAbility {



    private Vector direction;
    private Location location;
    private double distance;
    static HashMap<Player,Integer> map = new HashMap<>();




   private Listener listener;
    private final Permission permission = Lunar.permission();
    private static final long COOLDOWN = 5000;

    public SolarFlares(Player player) {
        super(player);

        if (!bPlayer.canBend(this)) return;

        direction = player.getLocation().getDirection();
        location = player.getEyeLocation();
        distance = 0;
        map.put(player, map.getOrDefault(player, 0) + 1);



        start();
    }

    @Override
    public void progress() {

        long duration = 5500;
        long runningtime = System.currentTimeMillis() - getStartTime();
        if (runningtime >= duration) {
            remove();
            return;
        }


        if (!bPlayer.canBendIgnoreBindsCooldowns(this)) {
            removenocd();
            return;
        }

        switch (map.getOrDefault(player, 0)) {
            case 0, 1 -> {


                if (location.getBlock().getType().isSolid()) {
                    Location location1 = location.getBlock().getLocation();
                    player.getWorld().spawnParticle(Particle.FLAME, location1, 19, 0.2, 0.2, 0.2, 0.3);
                    removenocd();

                }

                if (distance >= 12) {
                    removenocd();
                }

                GeneralMethods.displayColoredParticle("#e31943", location, 5, 0.2, 0.3, 0.25);
                player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, location, 3, 0.1 , 0.1, 0.2, 0.05);



                location.add(direction);
                distance ++;

                effecttargets();
            }
            case 2-> {

                if (location.getBlock().getType().isSolid()) {
                    Location location1 = location.getBlock().getLocation();
                    player.getWorld().spawnParticle(Particle.FLAME, location1, 19, 0.2, 0.2, 0.2, 0.3);
                    remove();

                }

                if (distance >= 12) {
                    remove();
                }

                GeneralMethods.displayColoredParticle("#e31943", location, 5, 0.2, 0.3, 0.25);
                player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, location, 3, 0.1 , 0.1, 0.2, 0.05);



                location.add(direction);
                distance ++;

                effecttargets();

            }
            }
        }



    public void effecttargets() {

        List<Entity> entities = GeneralMethods.getEntitiesAroundPoint(location, 1.5);
        for (Entity entity : entities) {
            if (entity instanceof LivingEntity) {
                if (entity.getUniqueId() != player.getUniqueId()) {
                    DamageHandler.damageEntity(entity, 1.5, this);
                    if (map.get(player) == 1) {
                        removenocd();
                    }else {
                        remove();
                    }

                }

            }
        }

    }

    @Override
    public Element getElement() {
        return Lunar.ele();
    }

    @Override
    public void remove() {
        map.clear();
        bPlayer.addCooldown(this);
        super.remove();
    }

    public void removenocd() {
        super.remove();
    }

    @Override
    public boolean isSneakAbility() {
        return false;
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
        return "SolarFlares";
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public void load() {

        this.listener = new AbilityListener();
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(listener, ProjectKorra.plugin);
        ProjectKorra.plugin.getServer().getPluginManager().addPermission(permission);


    }

    @Override
    public void stop() {
        HandlerList.unregisterAll(listener);

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
