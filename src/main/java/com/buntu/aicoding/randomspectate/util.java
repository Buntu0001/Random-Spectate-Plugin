package com.buntu.aicoding.randomspectate;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class util {
    public static HashMap<Player, Spectate> player_spectate_state = new HashMap<>();
    public static String AlterColorCode(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
