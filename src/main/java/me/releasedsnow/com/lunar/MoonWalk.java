package me.releasedsnow.com.lunar;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AirAbility;
import com.projectkorra.projectkorra.util.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class MoonWalk extends AirAbility implements AddonAbility {


    private final static long COOlDOWN = 7000;

    public Listener listener;
    public Permission permission;
    private final long revert = 3000;
    Location location;


    public MoonWalk(Player player) {
        super(player);

        if (!bPlayer.canBend(this)) return;

        bPlayer.addCooldown(this);


        start();
    }

    @Override
    public void progress() {
        if (!bPlayer.canBendIgnoreBindsCooldowns(this)) {
            remove();
            return;
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.LEVITATION, 1, 20));


        long longest = 1000;
        long start = System.currentTimeMillis() - getStartTime();

        if (start >= longest) {
            player.removePotionEffect(PotionEffectType.LEVITATION);


            Location starts = player.getLocation();
            int feet = player.getLocation().getBlockY() - 2;
            starts.setY(feet);

            if (starts.getBlock().getType().isAir()) {
                List<Location> locations = GeneralMethods.getCircle(starts, 2, 1, false, false, 0);
                for (Location location1 : locations) {
                    Block underneath = location1.getBlock();
                    location = underneath.getLocation();
                    if (!underneath.getType().isSolid()) {
                        if (!TempBlock.isTempBlock(underneath)) {
                            new TempBlock(underneath, Material.BLUE_STAINED_GLASS.createBlockData(), revert);
                            player.spawnParticle(Particle.ENCHANTMENT_TABLE, location1, 2);
                            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 50, 5));

                        }
                    }

                }
            }




        }


        long duration = 7000;
        long runningTime = System.currentTimeMillis() - getStartTime();

        if (runningTime >= duration) {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 60, 1));
            bPlayer.removeCooldown(this);
            removewithcooldown();

        }

    }

    @Override
    public boolean isSneakAbility() {
        return false;
    }

    @Override
    public boolean isHarmlessAbility() {
        return true;
    }

    @Override
    public long getCooldown() {
        return COOlDOWN;
    }

    @Override
    public String getName() {
        return "MoonWalk";
    }

    @Override
    public void remove() {
        super.remove();
    }

    public void removewithcooldown() {
        bPlayer.addCooldown(this);
        super.remove();
    }

    @Override
    public Element getElement() {
        return Lunar.ele();
    }

    @Override
    public Location getLocation() {
        return null;
    }

    @Override
    public void load() {
        this.listener = new AbilityListener();
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(listener, ProjectKorra.plugin);
        permission = new Permission("bending.ability.platformwalker");
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
        return null;
    }

    @Override
    public String getDescription() {
        return "Launch yourself into the air, and gracefully descend from the sky.";
    }

    @Override
    public String getInstructions() {
        return "Use [left-click]";
    }
}

