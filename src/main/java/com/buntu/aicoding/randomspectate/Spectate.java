package com.buntu.aicoding.randomspectate;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Spectate {
    public Player spectating_player;
    public Location spectating_player_previous_location;
    public Player spectated_player;
    public Integer static_task_id;

    public Spectate(Player player, Player target_player, Location previous_location) {
        spectating_player = player;
        spectated_player = target_player;
        spectating_player_previous_location = previous_location;
    }
}
