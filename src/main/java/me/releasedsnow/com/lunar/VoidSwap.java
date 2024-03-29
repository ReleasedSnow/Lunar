package me.releasedsnow.com.lunar;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AirAbility;
import com.projectkorra.projectkorra.util.DamageHandler;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;

import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class VoidSwap extends AirAbility implements AddonAbility {

    private static final double DAMAGE = 3;

    private static final double RANGE = 17;
    private static final long COOLDOWN = 3000;



    private AbilityListener listener;
    private final Permission permission = LunarElement.permission();

    private Location location;
    private double distancetravelled;

    private Set<Entity> hurt;
    private Vector direction;


    public VoidSwap(Player player) {
        super(player);


        if (!bPlayer.canBend(this)) return;

        bPlayer.addCooldown(this);



        location = player.getEyeLocation();
        direction = player.getLocation().getDirection();
        distancetravelled = 0;
        hurt = new HashSet<>();






        start();

    }



    @Override
    public void progress() {




        if (!bPlayer.canBendIgnoreBindsCooldowns(this)) {
            remove();
            return;

        }
        if (location.getBlock().getType().isSolid()) {
            Location location1 = location.getBlock().getLocation();
            player.spawnParticle(Particle.END_ROD, location1, 19, 0.2, 0.2, 0.2);


            remove();
            return;

        }

        if (distancetravelled >= RANGE) {
            remove();
            return;
        }

        effectTargets();


        GeneralMethods.displayColoredParticle("6f9ddb", location, 4, 0.2, 0.4, 0.3);
        GeneralMethods.displayColoredParticle("9bbfed", location, 4, 0.2, 0.4, 0.2);






        if (ThreadLocalRandom.current().nextInt(6) == 0) {
            player.playSound(location, Sound.BLOCK_BAMBOO_PLACE, 3, 3);
        }

        location.add(direction);
        distancetravelled ++;









    }


    private void effectTargets() {
        List<Entity> targets = GeneralMethods.getEntitiesAroundPoint(location, 1);
        for (Entity target : targets) {
            if (target.getUniqueId() == player.getUniqueId()) {
                continue;
            }
            if (!(target instanceof Mob || target instanceof Player)) {
                continue;
            }

            target.setFreezeTicks(200);
            DamageHandler.damageEntity(target, DAMAGE, this);
            hurt.add(target);
            Location location2 = player.getLocation();
            player.teleport(target.getLocation());
            target.teleport(location2);
            remove();
            return;

        }
    }
    @Override
    public Element getElement() {
        return LunarElement.element;
    }

    @Override
    public void remove() {
        bPlayer.addCooldown(this);
        super.remove();
        hurt.clear();
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
        return "VoidSwap";
    }

    @Override
    public Location getLocation() {
        return location;
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

    @Override
    public String getDescription() {
        return "Swap places with your enemies with a powerful blast";
    }

    @Override
    public String getInstructions() {
        return "Use [left-click]";
    }


}