package org.wayggstar.status;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class Stat {
    private int health;
    private int strength;
    private int agility;

    public Stat(Player player){
        this.health = 0;
        this.strength = 1;
        this.agility = 0;
    }

    public int getHealth(){
        return health;
    }

    public int getStrength(){
        return strength;
    }

    public int getAgility(){
        return agility;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void addHealth(int amount){
        this.health += amount;
    }

    public void addStrength(int amount){
        this.strength += amount;
    }

    public void addAgility(int amount){
        this.agility += amount;
    }

    public void applyStatsToPlayer(Player player) {
        player.setMaxHealth(20 + this.health);
        player.setHealth(player.getMaxHealth());
        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(this.strength);
        double speed = 0.2 + (this.agility * 0.01);
        player.setWalkSpeed((float) Math.min(speed, 0.5));
    }
}

