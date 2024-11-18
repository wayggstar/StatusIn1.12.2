package org.wayggstar.status;


import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;


public class VaultMoney{
    private static Economy economy = null;
    private static Chat chat = null;

    public static void setUpEconomy(){
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (rsp != null){
            economy = rsp.getProvider();
            Bukkit.getLogger().info("Vault Economy 시스템 연결 성공!");
        }else {
            Bukkit.getLogger().severe("Vault Economy 시스템을 찾을 수 없습니다!");
        }
    }

    public static Economy getEconomy(){
        return economy;
    }
}
