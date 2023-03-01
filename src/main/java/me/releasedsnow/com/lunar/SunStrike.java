package me.releasedsnow.com.lunar;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AirAbility;
import com.projectkorra.projectkorra.util.DamageHandler;
import com.projectkorra.projectkorra.util.TempBlock;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class SunStrike extends AirAbility implements AddonAbility {

    Location sphere = player.getLocation();

    private static final long COOLDOWN = 20000;
    private Listener listener;

    public static Location location;
    private Permission permission;

    Location start = player.getLocation();
    private Set<Entity> hurt;




    public SunStrike(Player player) {
        super(player);

        hurt = new HashSet<>();


        bPlayer.addCooldown(this);


        start();
    }

    @Override
    public void load() {
        this.listener = new AbilityListener();
        ProjectKorra.plugin.getServer().getPluginManager().registerEvents(listener, ProjectKorra.plugin);
        permission = new Permission("bending.ability.SunStrike");
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
    public void progress() {

        if (!bPlayer.getBoundAbilityName().equalsIgnoreCase("sunstrike")) {
            remove();
            return;
        }
        if (!bPlayer.canBendIgnoreBindsCooldowns(this)) {
            remove();
            return;


        }
        double above = player.getEyeLocation().getBlockY() + 5;
        double side = player.getEyeLocation().getBlockX();
        double side1 = player.getEyeLocation().getBlockZ();
        sphere.setY(above);
        sphere.setX(side);
        sphere.setZ(side1);

        for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
            double radius = Math.sin(i);
            double y = Math.cos(i);
            for (double a = 0; a < Math.PI * 2; a+= Math.PI / 10) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                sphere.add(x, y, z);
                if (ThreadLocalRandom.current().nextInt(2) == 0) {
                    GeneralMethods.displayColoredParticle("e6a638", sphere, 1, 0.25, 0.25, 0.25);
                    GeneralMethods.displayColoredParticle("e67538", sphere, 1, 0.25, 0.25, 0.25);
                }
                sphere.subtract(x, y, z);
            }
        }
        start = player.getLocation();
        start.setY(above);
        createBeam();




        long duration = 10000;
        long runningTime = System.currentTimeMillis() - getStartTime();

        if(runningTime >= duration) {
            remove();
            return;

        }






        if (ThreadLocalRandom.current().nextInt(500) == 0) {
            player.playSound(player.getLocation(), Sound.BLOCK_BAMBOO_PLACE, 3, 3);
        }

    }

    public void createBeam() {
        Vector direction = player.getLocation().getDirection();
        long RANGE = 9;
        for (double i = 0; i < RANGE; i += 0.5) {
            if (start.getBlock().getType().isSolid()) {
                List<Location> location1 = GeneralMethods.getCircle(start, 2, 1, false, false, 0);
                for (Location loc : location1) {
                    Block block = loc.getBlock();
                    if (!block.getType().isAir()) {
                        if (!TempBlock.isTempBlock(block)) {
                            System.out.println(block);
                            System.out.println(loc);
                            long REVERT = 10000;

                            new TempBlock(loc.getBlock(), Material.ORANGE_CONCRETE.createBlockData(), REVERT);

                        }
                    }
                }
                break;
            }
            if (start.getBlock().getType().isAir()) {
                start = start.add(direction.multiply(0.5).normalize());

                GeneralMethods.displayColoredParticle("f0e05e", start, 2, 0.2, 0.2, 0.15);
                if (ThreadLocalRandom.current().nextInt(75) == 0) {
                    player.spawnParticle(Particle.DRIPPING_HONEY, start, 5);
                }


            }

            List<Entity> entities = GeneralMethods.getEntitiesAroundPoint(start, 2);
            for (Entity entity : entities) {
                if (entity.getUniqueId() == player.getUniqueId()) {
                    continue;
                }
                if (!hurt.contains(entity)) {
                    if (entity instanceof Player) {
                        ((Player) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 30, 2));
                        entity.setFireTicks(5000);
                        DamageHandler.damageEntity(entity, 3, this);
                        hurt.add(entity);
                    }
                    entity.setFireTicks(5000);
                    DamageHandler.damageEntity(entity, 3, this);
                    hurt.add(entity);

                }
            }







        }
    }



    @Override
    public Element getElement () {
        return Lunar.element;
    }

    @Override
    public void remove () {
        super.remove();
    }

    @Override
    public boolean isSneakAbility () {
        return true;
    }

    @Override
    public boolean isHarmlessAbility () {
        return true;
    }

    @Override
    public long getCooldown () {
        return COOLDOWN;
    }


    @Override
    public String getName () {
        return "SunStrike";
    }

    @Override
    public Location getLocation () {
        return location;
    }
}

