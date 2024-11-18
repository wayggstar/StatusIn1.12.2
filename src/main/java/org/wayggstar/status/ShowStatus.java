package org.wayggstar.status;

import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import me.filoghost.holographicdisplays.api.hologram.VisibilitySettings;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class ShowStatus implements Listener {
    private final JavaPlugin plugin;
    private final Map<Player, Hologram> activeHolograms = new HashMap<>();
    private final Map<Player, Stat> playerStats = new HashMap<>();

    private float YawOfStatus;
    private float PitchOfStatus;

    public ShowStatus(JavaPlugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onInteract(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();
        if (activeHolograms.containsKey(player)) {
            activeHolograms.get(player).delete();
            activeHolograms.remove(player);
        }
        Location eyeLocation = player.getEyeLocation();
        YawOfStatus = player.getEyeLocation().getYaw();
        PitchOfStatus = player.getEyeLocation().getPitch();
        Location hologramLocation = eyeLocation.add(eyeLocation.getDirection().multiply(3));
        hologramLocation.setY(hologramLocation.getY() + 0.7);
        HolographicDisplaysAPI api = HolographicDisplaysAPI.get(plugin);
        Hologram hologram = api.createHologram(hologramLocation);
        VisibilitySettings visibilitySettings = hologram.getVisibilitySettings();
        visibilitySettings.setGlobalVisibility(VisibilitySettings.Visibility.HIDDEN);
        visibilitySettings.setIndividualVisibility(player, VisibilitySettings.Visibility.VISIBLE);

        hologram.getLines().appendText("§b§l<" + ChatColor.GREEN + player.getName() + "님의 상태창§b§l>");

        Stat stat = getPlayerStats(player);

        String health = "[§a체력 §rLv." + stat.getHealth() + "]";
        String strength = "[§4근력 §rLv." + stat.getStrength() + "]";
        String agility = "[§b민첩 §rLv." + stat.getAgility() + "]";
        hologram.getLines().appendText("§b잔액§r :" +ChatColor.GREEN + VaultMoney.getEconomy().getBalance(player) + " §e코인");
        hologram.getLines().appendText(health + " " + strength);
        hologram.getLines().appendText(agility);

        activeHolograms.put(player, hologram);
        stat.applyStatsToPlayer(player);
        player.sendMessage(ChatColor.GREEN + "상태창을 열었습니다.");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player player = e.getPlayer();
        if (e.getFrom().getX() != e.getTo().getX() ||
                e.getFrom().getY() != e.getTo().getY() ||
                e.getFrom().getZ() != e.getTo().getZ()) {
               if (activeHolograms.containsKey(player)) {
                   Hologram hologram = activeHolograms.get(player);
                   hologram.delete();
                   activeHolograms.remove(player);
                   player.sendMessage(ChatColor.RED + "상태창을 닫았습니다.");
               }
        }
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (activeHolograms.containsKey(player)) {
            Hologram hologram = activeHolograms.get(player);
            Location eyeLocation = player.getLocation();
            float yaw = eyeLocation.getYaw();
            float pitch = eyeLocation.getPitch();
            Stat stat = getPlayerStats(player);
            float normalizedYaw = normalizeYaw(yaw);
            float normalizedPitch = normalizePitch(pitch);
            float normalizedYOS = normalizeYaw(YawOfStatus);
            float normalizedPOS = normalizePitch(PitchOfStatus);
            if (isInYawRange(normalizedYaw, normalizeYaw(normalizedYOS - 10), normalizeYaw(normalizedYOS)) &&
                    isInPitchRange(normalizedPitch, normalizePitch(normalizedPOS - 7), normalizePitch(normalizedPOS - 4))) {
                stat.addHealth(1);
                player.sendMessage(ChatColor.GREEN + "체력이 1 증가했습니다!");
            }

            // 근력 증가
            if (isInYawRange(normalizedYaw, normalizeYaw(normalizedYOS - 10), normalizeYaw(normalizedYOS)) &&
                    isInPitchRange(normalizedPitch, normalizePitch(normalizedPOS - 3), normalizePitch(normalizedPOS))) {
                stat.addStrength(1);
                player.sendMessage(ChatColor.GREEN + "근력이 1 증가했습니다!");
            }

            // 민첩 증가
            if (isInYawRange(normalizedYaw, normalizeYaw(normalizedYOS - 10), normalizeYaw(normalizedYOS)) &&
                    isInPitchRange(normalizedPitch, normalizePitch(normalizedPOS + 1), normalizePitch(normalizedPOS + 4))) {
                stat.addAgility(1);
                player.sendMessage(ChatColor.GREEN + "민첩이 1 증가했습니다!");
            }

            updateHologram(player);
            stat.applyStatsToPlayer(player);
        }
    }

    private float normalizeYaw(float yaw) {
        if (yaw < 0) {
            return yaw + 360;
        }
        return yaw;
    }

    private float normalizePitch(float pitch) {
        return pitch + 90;
    }

    private boolean isInYawRange(float yaw, float minYaw, float maxYaw) {
        float normalizedYaw = normalizeYaw(yaw);
        return normalizedYaw >= minYaw && normalizedYaw <= maxYaw;
    }

    private boolean isInPitchRange(float pitch, float minPitch, float maxPitch) {
        float normalizedPitch = normalizePitch(pitch);
        return normalizedPitch >= minPitch && normalizedPitch <= maxPitch;
    }


    private Stat getPlayerStats(Player player) {
        return playerStats.computeIfAbsent(player, Stat::new);
    }

    private void updateHologram(Player player) {
        if (activeHolograms.containsKey(player)) {
            Hologram hologram = activeHolograms.get(player);
            hologram.getLines().clear();
            hologram.getLines().appendText("§b§l<" + ChatColor.GREEN + player.getName() + "님의 상태창§b§l>");

            Stat stat = getPlayerStats(player);

            String health = "[§a체력 §rLv." + stat.getHealth() + "]";
            String strength = "[§4근력 §rLv." + stat.getStrength() + "]";
            String agility = "[§b민첩 §rLv." + stat.getAgility() + "]";

            hologram.getLines().appendText(health);
            hologram.getLines().appendText(strength);
            hologram.getLines().appendText(agility);
        }
    }
}
