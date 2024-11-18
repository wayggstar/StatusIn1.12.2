package org.wayggstar.status.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.wayggstar.status.VaultMoney;

public class Balance implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("이 명령어는 플레이어만 사용할 수 있습니다!");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            double balance = VaultMoney.getEconomy().getBalance(player);
            int intBalance = (int) balance;
            player.sendMessage("§b잔액§r :" + ChatColor.GREEN + intBalance + " §e코인");
        }  else if (args[0].equalsIgnoreCase("추가")) {
            if (player.isOp()) {
                if (args.length < 3) {
                    player.sendMessage(ChatColor.RED + "사용법: /코인 추가 [금액] [플레이어]");
                    return false;
                }

                try {
                    int amount = Integer.parseInt(args[1]);
                    String targetPlayerName = args[2];
                    if (amount <= 0) {
                        player.sendMessage(ChatColor.RED + "추가할 금액은 0보다 커야 합니다!");
                        return false;
                    }
                    Player targetPlayer = player.getServer().getPlayer(targetPlayerName);
                    if (targetPlayer == null) {
                        player.sendMessage(ChatColor.RED + "해당 플레이어를 찾을 수 없습니다.");
                        return false;
                    }
                    VaultMoney.getEconomy().depositPlayer(targetPlayer, amount);
                    double newBalance = VaultMoney.getEconomy().getBalance(targetPlayer);
                    int newIntBalance = (int) newBalance;
                    player.sendMessage(ChatColor.GREEN + targetPlayerName + "에게 " + amount + " 코인이 추가되었습니다. 새로운 잔액: " + newBalance + " 코인");
                    targetPlayer.sendMessage(ChatColor.GREEN + "당신의 잔액에 " + amount + " 코인이 추가되었습니다.");

                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "유효한 금액을 입력하세요!");
                    return false;
                }
            } else {
                player.sendMessage(ChatColor.RED + "이 명령어는 OP만 사용할 수 있습니다.");
            }
        }
        return true;
    }
}
