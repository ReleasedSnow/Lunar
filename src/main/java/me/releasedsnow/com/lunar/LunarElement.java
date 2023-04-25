package me.releasedsnow.com.lunar;

import com.projectkorra.projectkorra.Element;
import com.projectkorra.projectkorra.ProjectKorra;
import com.projectkorra.projectkorra.ability.CoreAbility;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class LunarElement extends JavaPlugin {

    private static LunarElement plugin;
    public static Element element;

    public static Permission permission;

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

        new ConfigManager();
        System.out.println("Lunar has begun");
    }



    public static Permission permission() {
        permission = new Permission("bending.lunar");
        return permission;


    }


    public static LunarElement getPlugin() {
        return plugin;
    }

    @Override
    public void onDisable() {
        ProjectKorra.plugin.getServer().getPluginManager().removePermission(permission());
    }
}
