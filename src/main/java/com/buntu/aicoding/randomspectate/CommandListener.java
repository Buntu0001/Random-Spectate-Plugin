package com.buntu.aicoding.randomspectate;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.buntu.aicoding.randomspectate.util.AlterColorCode;
import static com.buntu.aicoding.randomspectate.util.player_spectate_state;

public class CommandListener implements CommandExecutor {
    private void RemoveSpectate(Player player) {
        Player target_player = player_spectate_state.get(player).spectated_player;
        player.sendMessage(AlterColorCode(String.format("&e[관전] &a%s&f님으로부터의 관전을 종료합니다.", target_player.getDisplayName())));

        target_player.sendMessage(AlterColorCode(String.format("&e[관전] &c%s&f님으로부터의 관전이 종료되었습니다.", player.getDisplayName())));
        player.setSpectatorTarget(null);
        player.setGameMode(GameMode.CREATIVE);
        player.teleport(player_spectate_state.get(player).spectating_player_previous_location);
        player_spectate_state.remove(player);
        player_spectate_state.remove(target_player);
    }

    private void SpectatePlayer(Player player, Player target_player, Location previous_location) {
        Location target_location = target_player.getLocation();
        player.setGameMode(GameMode.SPECTATOR);
        player.teleport(target_location);
        player.setSpectatorTarget(target_player);
        player.sendMessage(AlterColorCode(String.format("&e[관전] &a%s&f님의 관전을 시작합니다.", target_player.getDisplayName())));
        player.sendMessage(AlterColorCode(String.format("&e[관전] &f관전을 종료하고 싶다면 &6[/관전 종료] &f를 입력해 주세요.", target_player.getDisplayName())));
        target_player.sendMessage(AlterColorCode(String.format("&e[관전] &f당신은 &c%s&f님으로부터 관전되고 있습니다.", player.getDisplayName())));
        if (player_spectate_state.containsKey(player)) {
            RemoveSpectate(player);
        }
        Spectate spectate = new Spectate(player, target_player, previous_location);
        player_spectate_state.put(player, spectate);
        player_spectate_state.put(target_player, spectate);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        //player.sendMessage(AlterColorCode("&e[관전] &f랜덤한 플레이어를 관전합니다."));
        if (label.equalsIgnoreCase("관전")) {
            if (player.isOp()) {
                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("종료")) {
                        if (player_spectate_state.containsKey(player)) {
                            if (player_spectate_state.get(player).spectating_player == player) {
                                RemoveSpectate(player);
                                return true;
                            } else {
                                player.sendMessage(AlterColorCode("&e[관전] &f당신은 관전 중이 아닙니다."));
                                return true;
                            }
                        } else {
                            player.sendMessage(AlterColorCode("&e[관전] &f당신은 관전 중이 아닙니다."));
                            return true;
                        }
                    } else {
                        if (args[0].equalsIgnoreCase(player.getName())) {
                            player.sendMessage(AlterColorCode("&e[관전] &f자기 자신을 관전할 수 없습니다."));
                            return true;
                        }
                        Player target_player = Bukkit.getPlayer(args[0]);
                        Location previous_location = player.getLocation();
                        SpectatePlayer(player, target_player, previous_location);
                        return true;
                    }
                } else {
                    ArrayList<Player> player_list = new ArrayList<>();
                    player_list.addAll(Bukkit.getOnlinePlayers());
                    player_list.remove(player);
                    int player_count = player_list.size();
                    Random random = new Random();
                    Player target_player = player_list.get(random.nextInt(player_count));
                    //player.sendMessage(AlterColorCode(String.format("&e[관전] &a%s&f님이 걸렸습니다!", target_player.getDisplayName())));
                    Location previous_location = player.getLocation();
                    SpectatePlayer(player, target_player, previous_location);
                    return true;
                }
            }
        } else {
            player.sendMessage(AlterColorCode("&c[ERROR] &f이 명령어를 사용할 권한이 없습니다."));
        }
        return false;
    }
}

