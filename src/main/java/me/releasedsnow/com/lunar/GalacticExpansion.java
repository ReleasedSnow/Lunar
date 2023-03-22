package me.releasedsnow.com.lunar;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.AirAbility;
import com.projectkorra.projectkorra.util.DamageHandler;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;


public class GalacticExpansion extends AirAbility implements AddonAbility {


    Location sphere = player.getLocation();
    private Listener listener;
    private Permission permission = Lunar.permission();

    private static final long COOLDOWN = 8000;

    double multiply;
    private Set<Entity> targets;
    private double width;





    public GalacticExpansion(Player player) {
        super(player);

        if (!bPlayer.canBend(this)) return;

        bPlayer.addCooldown(this);

        targets = new HashSet<>();


        start();

    }

    @Override
    public void progress() {

        if (!bPlayer.getBoundAbilityName().equalsIgnoreCase("galacticexpansion")) {
            removecooldown();
            return;
        }


        if (!bPlayer.canBendIgnoreBindsCooldowns(this)) {
            remove();
            return;
        }



        sphere.setY(player.getLocation().getY() +1 );
        sphere.setX(player.getLocation().getX());
        sphere.setZ(player.getLocation().getZ());

        for (double i = 0; i <= Math.PI; i += Math.PI / 10) {
             double radius = multiply *  Math.sin(i);
             double y = Math.cos(i) * multiply;
            for (double a = 0; a < Math.PI * 2; a+= Math.PI / 10) {
                double x = Math.cos(a) * radius;
                double z = Math.sin(a) * radius;
                sphere.add(x, y, z);
                if (ThreadLocalRandom.current().nextInt(2) == 0) {
                    GeneralMethods.displayColoredParticle("f6927d", sphere, 1, 0.25, 0.25, 0.25);
                    GeneralMethods.displayColoredParticle("c5a9ad", sphere, 1, 0.25, 0.25, 0.25);
                }
                sphere.subtract(x, y, z);
            }
            if (getRunningTicks() % 15 == 0) {
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_CONDUIT_AMBIENT, 3, 3);
                multiply += 0.08;
                width += 0.08;

            }

            List<Entity> entities = GeneralMethods.getEntitiesAroundPoint(player.getLocation(), width);

            for (Entity entity : entities) {
                if (entity.getUniqueId() != player.getUniqueId()) {
                    if (getRunningTicks() % 15 == 0) {
                        DamageHandler.damageEntity(entity, 0.25, this);
                        Vector direction = entity.getLocation().getDirection().multiply(-1.5);
                        entity.setVelocity(direction);

                    }
                }


            }





            long duration = 5000;
            long runningTime = System.currentTimeMillis() - getStartTime();


            if (runningTime >= duration) {
                removecooldown();
                return;
            }







        }



    }



    @Override
    public boolean isSneakAbility() {
        return true;
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
        return "GalacticExpansion";
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
    public Element getElement() {
        return Lunar.ele();
    }

    @Override
    public void remove() {
        super.remove();
    }

    public void removecooldown() {
        bPlayer.addCooldown(this);
        super.remove();
    }



    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String getDescription() {
        return "Damage your enemies with expanding surges of energy, sent from space.";
    }

    @Override
    public String getInstructions() {
        return "Use [shift] to activate the ability, do not switch slots or the move will be cancelled.";
    }
}

