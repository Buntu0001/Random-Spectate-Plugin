package com.buntu.aicoding.randomspectate;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import static com.buntu.aicoding.randomspectate.util.player_spectate_state;

public class EventListener implements Listener {
    @EventHandler
    public void onPlayerCancelSpectate(PlayerTeleportEvent teleportEvent) {
        if (teleportEvent.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE) {
            Player player = teleportEvent.getPlayer();
            if (player_spectate_state.containsKey(player)) {
                if (player_spectate_state.get(player).spectating_player == player) {
                    teleportEvent.setCancelled(true);
                }
            }
        }
    }
}
