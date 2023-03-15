package me.releasedsnow.com.lunar;

import com.projectkorra.projectkorra.BendingPlayer;
import com.projectkorra.projectkorra.ability.CoreAbility;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;


public class AbilityListener implements Listener {

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.LEFT_CLICK_BLOCK) return;

        Player player = e.getPlayer();
        BendingPlayer bendingPlayer = BendingPlayer.getBendingPlayer(player);

        if (bendingPlayer == null) {
            return;
        }

        String bound = bendingPlayer.getBoundAbilityName();
        if (bound.equalsIgnoreCase("voidswap")) {
            new VoidSwap(player);

        } else if (bound.equalsIgnoreCase("lunarring")) {
            new LunarRing(player);

        } else if (bound.equalsIgnoreCase("moonwalk")) {
            new MoonWalk(player);

        }
        }


    @EventHandler
    public void onShift(PlayerToggleSneakEvent e) {
        if (e.isSneaking()) {

            Player player = e.getPlayer();
            BendingPlayer bendingPlayer = BendingPlayer.getBendingPlayer(player);
            String bound = bendingPlayer.getBoundAbilityName();
            if (bound.equalsIgnoreCase("sunstrike")) {
                if (bendingPlayer.canBend(CoreAbility.getAbility(SunStrike.class))) {
                    new SunStrike(player);
                }
            } else if (bound.equalsIgnoreCase("lunardash")) {
                new LunarDash(player);

            } else if (bound.equalsIgnoreCase("galacticexpansion")) {
                new GalacticExpansion(player);
            }else  if (bound.equalsIgnoreCase("cosmiccollide")) {
                new CosmicCollide(player);
        }
        }
    }
}
