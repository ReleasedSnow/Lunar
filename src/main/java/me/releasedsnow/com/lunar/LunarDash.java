package me.releasedsnow.com.lunar;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.GeneralMethods;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.AddonAbility;
import com.projectkorra.projectkorra.ability.EarthAbility;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LunarDash extends EarthAbility implements AddonAbility {

    private static final long COOLDOWN = 8000;

    private Listener listener;
    private Permission permission = LunarElement.permission();


    private double angle;
    private double height;
    private double angle2;
    private double height2;




    public LunarDash(Player player) {
        super(player);

        if (!bPlayer.canBend(this)) return;



        Location location = player.getLocation();

        bPlayer.addCooldown(this);

        height = location.getBlockY() + 5;
        height2 = location.getBlockY() + 5;

        start();
    }

    @Override
    public void progress() {




        if (!bPlayer.canBendIgnoreBindsCooldowns(this)) {
            remove();
            return;
        }

        if (!player.isSneaking()) {
            remove();
            return;
        }



        Vector direction = player.getLocation().getDirection();
        direction.multiply(3);





        if (height <= player.getLocation().getBlockY()) {
            player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 3, 3);
            player.setVelocity(direction);
            player.setFallDistance(0f);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100,  5));
            remove();
            return;
        }
        if (height2 <= player.getLocation().getBlockY()) {
            remove();
            return;
        }


        angle += 4;
        height -= 0.2;
        height2 -= 0.2;
        angle2 = angle2 + 180;
        angle2 += 4;

        double x = Math.cos(Math.toRadians(angle));
        double x2 = Math.cos(Math.toRadians(angle2));
        double z = Math.sin(Math.toRadians(angle));
        double z2 = Math.sin(Math.toRadians(angle2));

        Location point = player.getLocation().clone().add(x * 0.6, height - player.getLocation().getY(), z * 0.6);
        Location point2 = player.getLocation().clone().add(x2 * 0.6, height2 - player.getLocation().getY(), z2 * 0.6);



        GeneralMethods.displayColoredParticle("fc65bb", point, 2, 0.2, 0.2, 0.2);
        GeneralMethods.displayColoredParticle("cc86e3", point2, 3, 0.2, 0.2, 0.2);


        List<Entity> entities = GeneralMethods.getEntitiesAroundPoint(player.getLocation(), 1.5);
        for (Entity entity : entities) {
                if (entity instanceof Player) {
                    Player entityplayer = ((Player) entity).getPlayer();
                    if (entityplayer != null) {
                        if (entityplayer.getUniqueId() != player.getUniqueId()) {
                            entity.sendMessage(ChatColor.LIGHT_PURPLE + "MoonStruck");
                            entityplayer.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 2));
                        }else {
                           entityplayer.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20, 2));




                        }
                    }
                }
        }






        if (ThreadLocalRandom.current().nextInt(20) == 0) {
            player.playSound(player.getLocation(), Sound.BLOCK_GLASS_STEP, 3, 3);

        }

    }



    @Override
    public Element getElement() {
        return LunarElement.element;
    }

    @Override
    public void remove() {
        super.remove();
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
        return COOLDOWN;
    }

    @Override
    public String getName() {
        return "LunarDash";
    }

    @Override
    public Location getLocation() {
        return player.getLocation();
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
        return "Charge up lunar energy to propel yourself through the air";
    }

    @Override
    public String getInstructions() {
        return "Hold down [shift] until you hear a click";
    }


}

