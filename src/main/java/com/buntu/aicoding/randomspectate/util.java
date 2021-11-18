package com.buntu.aicoding.randomspectate;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class util {
    public static HashMap<Player, Spectate> player_spectate_state = new HashMap<>();
    public static HashMap<Player, Integer> repeat_task_id = new HashMap<>();
    public static int auto_change_interval = 10;
    public static Plugin plugin;
    public static String AlterColorCode(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void RemoveSpectate(Player player) {
        if (PlayerIsRepeat(player)) {
            RemoveAutoSpectate(player);
        }
        Player target_player = player_spectate_state.get(player).spectated_player;
        player.sendMessage(AlterColorCode(String.format("&e[관전] &a%s&f님으로부터의 관전을 종료합니다.", target_player.getDisplayName())));

        target_player.sendMessage(AlterColorCode(String.format("&e[관전] &c%s&f님으로부터의 관전이 종료되었습니다.", player.getDisplayName())));
        player.setSpectatorTarget(null);
        player.setGameMode(GameMode.CREATIVE);
        player.teleport(player_spectate_state.get(player).spectating_player_previous_location);
        player_spectate_state.remove(player);
        player_spectate_state.remove(target_player);
    }

    public static void RemoveAutoSpectate(Player player) {
        Bukkit.getScheduler().cancelTask(repeat_task_id.get(player));
    }

    public static void RemoveSpectateWithoutChanging(Player player) {
        Player target_player = player_spectate_state.get(player).spectated_player;
        player.sendMessage(AlterColorCode(String.format("&e[관전] &a%s&f님으로부터의 관전을 종료합니다.", target_player.getDisplayName())));

        target_player.sendMessage(AlterColorCode(String.format("&e[관전] &c%s&f님으로부터의 관전이 종료되었습니다.", player.getDisplayName())));
        player_spectate_state.remove(player);
        player_spectate_state.remove(target_player);
    }
    public static void RemoveOfflineSpectating(Player player) {
        if (PlayerIsRepeat(player)) {
            RemoveAutoSpectate(player);
        }
        Player spectated_player = player_spectate_state.get(player).spectated_player;
        spectated_player.sendMessage(AlterColorCode(String.format("&e[관전] &c%s&f님으로부터의 관전이 종료되었습니다.", player.getDisplayName())));
        player_spectate_state.remove(player);
        player_spectate_state.remove(spectated_player);
    }
    public static void RemoveOfflineSpectated(Player player) {
        Player spectating_player = player_spectate_state.get(player).spectating_player;
        if (PlayerIsRepeat(spectating_player)) {
            RemoveAutoSpectate(spectating_player);
        }
        spectating_player.sendMessage(AlterColorCode(String.format("&e[관전] &a%s&f님으로부터의 관전이 종료되었습니다.", player.getDisplayName())));
        player_spectate_state.remove(player);
        player_spectate_state.remove(spectating_player);
    }

    public static void SpectatePlayer(Player player, Player target_player, Location previous_location) {
        if (PlayerIsSpectating(target_player)) {
            player.sendMessage(AlterColorCode("&e[관전] &f이미 관전 중인 플레이어는 관전할 수 없습니다."));
        } else {
            Location target_location = target_player.getLocation();
            if (player_spectate_state.containsKey(player)) {
                RemoveSpectateWithoutChanging(player);
            }
            player.setGameMode(GameMode.SPECTATOR);
            player.teleport(target_location);
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    player.setSpectatorTarget(target_player);
                }
            }, 5L);
            player.sendMessage(AlterColorCode(String.format("&e[관전] &a%s&f님의 관전을 시작합니다.", target_player.getDisplayName())));
            player.sendMessage(AlterColorCode(String.format("&e[관전] &f관전을 종료하고 싶다면 &6[/관전 종료] &f를 입력해 주세요.", target_player.getDisplayName())));
            target_player.sendMessage(AlterColorCode(String.format("&e[관전] &f당신은 &c%s&f님으로부터 관전되고 있습니다.", player.getDisplayName())));
            Spectate spectate = new Spectate(player, target_player, previous_location);
            player_spectate_state.put(player, spectate);
            player_spectate_state.put(target_player, spectate);
        }
    }

    public static Boolean PlayerIsSpectating(Player player) {
        if (player_spectate_state.containsKey(player)) {
            if (player_spectate_state.get(player).spectating_player == player) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static Boolean PlayerIsSpectated(Player player) {
        if (player_spectate_state.containsKey(player)) {
            if (player_spectate_state.get(player).spectated_player == player) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static Boolean PlayerIsRepeat(Player player) {
        if (repeat_task_id.containsKey(player)) {
            return true;
        } else {
            return false;
        }
    }

    public static Player SelectRandomPlayer(Player player) {
        ArrayList<Player> player_list = new ArrayList<>();
        player_list.addAll(Bukkit.getOnlinePlayers());
        player_list.remove(player);
        int player_count = player_list.size();
        Random random = new Random();
        return player_list.get(random.nextInt(player_count));
    }
}
