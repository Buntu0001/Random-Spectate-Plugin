package com.buntu.aicoding.randomspectate;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.buntu.aicoding.randomspectate.CommandListener;

import static com.buntu.aicoding.randomspectate.util.*;

public class EventListener implements Listener {
    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent teleportEvent) {
        if (teleportEvent.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE) {
            Player player = teleportEvent.getPlayer();
            if (player_spectate_state.containsKey(player)) {
                if (player_spectate_state.get(player).spectating_player == player) {
                    teleportEvent.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent quitEvent) {
        Player player = quitEvent.getPlayer();
        if (PlayerIsSpectating(player) || PlayerIsRepeat(player)) {
            try {
                RemoveOfflineSpectating(player);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (PlayerIsSpectated(player)) {
            try {
                RemoveOfflineSpectated(player);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
