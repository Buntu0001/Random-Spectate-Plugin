package com.buntu.aicoding.randomspectate;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.buntu.aicoding.randomspectate.util.*;

public class CommandListener implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if (label.equalsIgnoreCase("관전")) {
            try {
                if (player.isOp()) {
                    if (!PlayerIsSpectated(player)) {
                        if (args.length == 1) {
                            if (args[0].equalsIgnoreCase("종료")) {
                                if (PlayerIsSpectating(player)) {
                                    RemoveSpectate(player);
                                    return true;
                                } else {
                                    player.sendMessage(AlterColorCode("&e[관전] &f당신은 관전 중이 아닙니다."));
                                    return true;
                                }
                            } else if (args[0].equalsIgnoreCase("자동")) {
                                if (PlayerIsRepeat(player) && PlayerIsSpectating(player)) {
                                    player.sendMessage(AlterColorCode("&e[관전] &f당신은 이미 관전 중입니다."));
                                    return true;
                                } else {
                                    player.sendMessage(AlterColorCode(String.format("&e[관전] &f자동 관전을 시작합니다. 자동 변경 시간은 &c%d&f초 입니다.", auto_change_interval)));
                                    Integer task_id = Bukkit.getScheduler().scheduleSyncRepeatingTask(util.plugin, new Runnable() {
                                        @Override
                                        public void run() {

                                            Player target_player = SelectRandomPlayer(player);
                                            Location previous_location = player.getLocation();
                                            SpectatePlayer(player, target_player, previous_location);
                                        }
                                    }, 0, 20L * auto_change_interval);
                                    repeat_task_id.put(player, task_id);
                                    return true;
                                }
                            } else {
                                if (args[0].equalsIgnoreCase(player.getName())) {
                                    player.sendMessage(AlterColorCode("&e[관전] &f자기 자신을 관전할 수 없습니다."));
                                    return true;
                                }
                                Player check_args = Bukkit.getPlayer(args[0]);
                                if (check_args == null) {
                                    player.sendMessage(AlterColorCode("&e[관전] &f플레이어를 찾을 수 없습니다."));
                                    return true;
                                }
                                Player target_player = Bukkit.getPlayer(args[0]);
                                Location previous_location = player.getLocation();
                                SpectatePlayer(player, target_player, previous_location);
                                return true;
                            }
                        } else {

                            Player target_player = SelectRandomPlayer(player);
                            Location previous_location = player.getLocation();
                            SpectatePlayer(player, target_player, previous_location);
                            return true;
                        }
                    } else {
                        player.sendMessage(AlterColorCode("&e[관전] &f당신은 관전되는 중입니다."));
                        return true;
                    }
                } else {
                    player.sendMessage(AlterColorCode("&c[ERROR] &f이 명령어를 사용할 권한이 없습니다."));
                    return true;
                }
            } catch (Exception ex) {
                player.sendMessage("&c[ERROR] &f뭔가 잘못된 것 같은데요...");
                ex.printStackTrace();
            }
        }
        return false;
    }
}

