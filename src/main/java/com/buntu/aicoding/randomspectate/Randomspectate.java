package com.buntu.aicoding.randomspectate;

import org.bukkit.plugin.java.JavaPlugin;
import static com.buntu.aicoding.randomspectate.util.*;
public final class Randomspectate extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("분투의 랜덤 관전 플러그인이 활성화 되었습니다.");
        System.out.println("Contact via Discord: grradis#0001");
        util.plugin = this;
        getCommand("관전").setExecutor(new CommandListener());
        getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable() {
        System.out.println("분투의 랜덤 관전 플러그인이 비활성화 되었습니다.");
        System.out.println("Contact via Discord: grradis#0001");
    }
}
