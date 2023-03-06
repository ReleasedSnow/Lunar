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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;


public class GalacticExpansion extends AirAbility implements AddonAbility {


    Location sphere = player.getLocation();
    private Listener listener;
    private Permission permission;

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
                    GeneralMethods.displayColoredParticle("c168f6", sphere, 1, 0.25, 0.25, 0.25);
                    GeneralMethods.displayColoredParticle("61477b", sphere, 1, 0.25, 0.25, 0.25);
                }
                sphere.subtract(x, y, z);
            }
            if (getRunningTicks() % 20 == 0) {
                player.getWorld().playSound(player.getLocation(), Sound.ITEM_AXE_STRIP, 3, 3);
                multiply += 0.08;
                width += 0.08;
                List<Entity> entities = GeneralMethods.getEntitiesAroundPoint(player.getLocation(), width);

                for (Entity entity : entities) {
                    if (entity.getUniqueId() != player.getUniqueId()) {
                            DamageHandler.damageEntity(entity, 2, this);
                    }


                }

            }





            long duration = 6000;
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
        permission = new Permission("bending.ability.glacticexpansion");
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

