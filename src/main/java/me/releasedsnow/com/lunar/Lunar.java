package me.releasedsnow.com.lunar;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.CoreAbility;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public final class Lunar extends JavaPlugin {

    private static Lunar plugin;
    public static Element element;

    @Override
    public void onEnable() {


        plugin = this;
        getPlugin().getServer().getPluginManager().registerEvents(new AbilityListener(), plugin);
        CoreAbility.registerPluginAbilities(this, "me.releasedsnow.com.lunar");
        element = new Element("Lunar", Element.ElementType.BENDING, ProjectKorra.plugin) {



            @Override
            public ChatColor getColor() {
                return ChatColor.of("#8B62F3");
            }
        };
        System.out.println("Lunar has begun");
    }

    public static Element ele() {
        return element;
    }


    private static Lunar getPlugin() {
        return plugin;
    }



}
