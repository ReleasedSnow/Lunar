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
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class VoidSwap extends AirAbility implements AddonAbility {

    private static final double DAMAGE = 2;

    private static final double RANGE = 15;
    private static final long COOLDOWN = 3000;



    private AbilityListener listener;
    private Permission permission;

    private Location location;
    private double distancetravelled;

    private Set<Entity> hurt;
    private Vector direction;


    public VoidSwap(Player player) {
        super(player);
        if (!bPlayer.getBoundAbilityName().equalsIgnoreCase("voidswap")) {
            bPlayer.addCooldown(this);
            remove();
            return;
        }

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


        GeneralMethods.displayColoredParticle("052f89", location, 4, 0.2, 0.4, 0.3);
        GeneralMethods.displayColoredParticle("6d05a0", location, 4, 0.2, 0.4, 0.2);
        GeneralMethods.displayColoredParticle("9a73e2", location, 3, 0.2, 0.4, 0.2);





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
        return Lunar.element;
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
        permission = new Permission("bending.ability.FireAbility");
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

    @Override
    public String getDescription() {
        return "Swap places with your enemies with a powerful blast";
    }

    @Override
    public String getInstructions() {
        return "Use [left-click]";
    }


}