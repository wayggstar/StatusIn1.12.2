package org.wayggstar.status;


import org.bukkit.ChatColor;

import org.bukkit.plugin.java.JavaPlugin;
import org.wayggstar.status.commands.CommandManager;


public final class Status extends JavaPlugin {

    @Override
    public void onEnable() {
        VaultMoney.setUpEconomy();
        if (VaultMoney.getEconomy() == null) {
            getLogger().severe("Vault Economy 시스템 연결 실패!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getServer().getPluginManager().registerEvents(new ShowStatus(this), this);
        getLogger().info("Vault Economy 시스템 연결 성공!");
        CommandManager commandManager = new CommandManager(this);
        commandManager.registerCommands();
        getLogger().info(ChatColor.GREEN + "상태창 플러그인 활성화");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "상태창 플러그인 비활성화");
    }
}
