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
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;


import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LunarRing extends AirAbility implements AddonAbility {

    private static final long COOLDOWN = 10000;
    private AbilityListener listener;

    private double angle1;
    public static Location location;
    private Permission permission = LunarElement.permission();
    int yheight;

    private Vector direction;
    private Location start;
    private static final double RANGE = 7;



    public LunarRing(Player player) {
        super(player);


        if (!bPlayer.canBend(this)) return;





        final Location location1 = player.getLocation();
        yheight = location1.getBlockY() + 6;





        bPlayer.addCooldown(this);


        start();
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
    public void progress() {


        if (!bPlayer.getBoundAbilityName().equalsIgnoreCase("lunarring")) {
            player.setAllowFlight(false);
            player.setFlying(false);
            player.setGlowing(false);
            remove();
            return;
        }


        if (!bPlayer.canBendIgnoreBindsCooldowns(this)) {
            remove();
            return;


        }





        angle1 += 5;
        double x3 = Math.cos(Math.toRadians(angle1));
        double z3 = Math.sin(Math.toRadians(angle1));
        Location point = player.getLocation().clone().add(x3 * 1, 0, z3 * 1);
        GeneralMethods.displayColoredParticle("98f6d8", point, 4, 0.2, 0.2, 0.15);
        GeneralMethods.displayColoredParticle("109ca1", point, 3, 0.2, 0.2, 0.15);




        if (player.getLocation().getBlockY() >= yheight) {
            player.setVelocity(new Vector(0,0,0));
            player.setAllowFlight(true);//
            player.setFlying(true);
            createBeam();
            player.setGlowing(true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 50, 4));
            if (!player.isFlying()) {
                player.setFlying(true);
            }
        }else {
            player.setVelocity(new Vector(0, 0.30, 0));


        }




        long duration = 3500;
        long runningTime = System.currentTimeMillis() - getStartTime();

        if(runningTime >= duration) {
            player.setFallDistance(0f);


            player.setGlowing(false);
            player.setFlying(false);
            player.setAllowFlight(false);
            player.removePotionEffect(PotionEffectType.SPEED);
            bPlayer.addCooldown(this);
            remove();
            return;



        }





        if (ThreadLocalRandom.current().nextInt(100) == 0) {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 3, 3);
        }

    }

    public void createBeam() {
        start = player.getEyeLocation();
        direction = start.getDirection();
        for (double i = 0; i < RANGE; i += 0.5) {
            if (start.getBlock().getType().isSolid()) {
                List<Location> location1 = GeneralMethods.getCircle(start, 2, 1, false, false, 0);
                for (Location loc : location1) {
                    Block block = loc.getBlock();
                    if (!block.getType().isAir()) {
                        player.getWorld().spawnParticle(Particle.SNOWFLAKE, loc, 2, 0.1, 0.1, 0.1, 0.1f);


                    }
                }
                break;
            }


            if (start.getBlock().getType().isAir()) {
                start = start.add(direction.multiply(0.5).normalize());

                GeneralMethods.displayColoredParticle("061720", start, 1, 0.2, 0.2, 0.15);
                GeneralMethods.displayColoredParticle("023e85", start, 1, 0.2, 0.2, 0.2);

                if (ThreadLocalRandom.current().nextInt(50) == 0) {
                    player.spawnParticle(Particle.FIREWORKS_SPARK, start, 1, 0.15, 0.1,  0.15, 0.1f);

                }
                List<Entity> entities = GeneralMethods.getEntitiesAroundPoint(start, 2);
                for (Entity entity : entities) {
                    if (entity.getUniqueId() != player.getUniqueId()) {
                        if (entity instanceof Player || entity instanceof Mob) {
                            if (getRunningTicks() % 20 == 0) {
                                DamageHandler.damageEntity(entity, 0.5, this);
                            }
                        }
                    }

                }

            }





        }
    }




    @Override
    public Element getElement () {
        return LunarElement.element;
    }

    @Override
    public void remove () {
        super.remove();
    }

    @Override
    public boolean isSneakAbility () {
        return false;
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
        return "LunarRing";
    }

    @Override
    public Location getLocation () {
        return location;
    }

    @Override
    public String getDescription() {
        return "Float into the air with rings of energy, once at a certain level expel a ray of light that summons enemies towards you.";
    }

    @Override
    public String getInstructions() {
        return "Use [left-click] and move your cursor to direct the beam.";
    }
}
